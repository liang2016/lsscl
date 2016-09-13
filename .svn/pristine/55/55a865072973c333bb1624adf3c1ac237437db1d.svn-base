package com.serotonin.mango.rt.statistic.common;
import com.serotonin.mango.Common;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.serotonin.mango.vo.acp.ACPVO;
import com.serotonin.mango.db.dao.acp.ACPDao;
import com.serotonin.mango.vo.statistics.ScheduledStatisticVO;
import com.serotonin.mango.vo.statistics.StatisticsScriptVO;
import com.serotonin.mango.rt.statistic.common.GrabForStatisticsDao;
import com.serotonin.mango.db.dao.acp.CompressedAirSystemDao;
import com.serotonin.mango.vo.acp.ACPSystemVO;
import com.serotonin.mango.db.dao.statistics.ScheduledStatisticDao;
import com.serotonin.mango.Common;
/**
 * 节能指标五
 * [        系统流量<系统统计参数>      ]
 * [系统流量周期内的所有值中 (maxValue-minValue)/(maxValue+minValue)  ]
 * @author 王金阳
 *
 */
public class EnergSavingTargetNo5RT extends StatisticsRT{
	
	/**
	 * 系统流量 统计参数的ID
	 */
	public static final int FLOW = StatisticsUtil.STATISTICS_PARAM_SYSTEM_FLOW;
	
	/**
	 * 系统流量对应的点的ID，每个机器都不同
	 */
	private int dpid;
	
	@Override
	public void run() {
		
		while(!off){
			
			//上一个统计时间
			endTime = getPrevExecuteTime(-1);
			if(startTime<endTime){
				executeStatistic();
			}else{
				Common.ctx.getRuntimeManager().completedOneTarget(endTime);
				if(Common.ctx.getRuntimeManager().indexCanStatistics(endTime)){
					Common.ctx.getRuntimeManager().indexStatistics();
				}
				//开启定时器，下一个统计时间再次启动
				Timer timer = new Timer();
				timer.schedule(new ScheduledTask(),getNextExecuteTime(-1) - new Date().getTime());
				//关闭开关，终止线程
				off = true;
			}
		}
	}
	
	/**
	 * 统计startTime到endTime之间的数据
	 */
	public void executeStatistic(){
		
		/**
		 * 是以系统为统计单位的
		 */
		CompressedAirSystemDao systemDao = new CompressedAirSystemDao();
		
		List<ACPSystemVO> systemList = systemDao.getAllACPSystem();//获取所有系统
		
		GrabForStatisticsDao grabForStatisticsDao = new GrabForStatisticsDao();
		
		ScheduledStatisticDao scheduledStatisticDao = new ScheduledStatisticDao();
		
		long executeTime = -1;//当前执行那个时间的统计
		
		for(ACPSystemVO systemVO:systemList){//循环所有系统
			
			startTime = -1;//每个系统的开始统计时间都要再次去检测上次统计到哪里
			
			/**
			 * 当前系统FLOW对应的点的ID
			 */
			dpid = grabForStatisticsDao.findDataPointIdFromSystemBySpid(systemVO.getId(),FLOW);
			if(dpid==-1){ // 该系统没有对应的FLOW数据点
				/**********放弃当前系统统计，继续下一个系统***********/
				continue;
			}else{
				/**
				 * 该统计历史上最后一次统计时间的下一个统计时间
				 */
				startTime = grabForStatisticsDao.getLastExecuteTime(StatisticsUtil.ENERGY_SAVING_TARGET_NO5,systemVO.getId())+MS_IN_CYCLE;				
				if(startTime==MS_IN_CYCLE-1){//如果没有找到历史统计记录 
					/**
					 * 则从最早被采集到的点的时间开始
					 */
					startTime = grabForStatisticsDao.getFirstCollectTime(dpid);
					if(startTime==-1){//如果该点根本没有采集到过数据
						/**********放弃当前系统统计，继续下一个系统***********/
						continue;
					}else{
						startTime = getNextExecuteTime(startTime);
					}
				}
			}	
			
			//循环该系统需要统计的时间段内的每个周期
			while(startTime<=endTime){
				executeTime = startTime; 
				double result = getFLOWWaveValue(executeTime);
				if(result==-1d){
//					//准备下一个周期的统计
//					startTime+=MS_IN_CYCLE;
//					continue;
					//如果周期内没有数据那么波动就为0
					result = 0D;
				}
				
				/**
				 * 将结果放进数据库中 
				 */
				/*************************************/
				ScheduledStatisticVO scheduledStatisticVO = new ScheduledStatisticVO(StatisticsUtil.ENERGY_SAVING_TARGET_NO5,result,executeTime,StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM,systemVO.getId());
//				System.out.println("target: 1 	SYSTEM: "+systemVO.getId()+"	"+new Date(executeTime).toLocaleString()+":  "+result);
				/*************************************/
				scheduledStatisticDao.save(scheduledStatisticVO);
				//准备下一个周期的统计
				startTime+=MS_IN_CYCLE;
			}
		}
		//经过这次统计，预定统计应该是执行到endTime
		startTime = endTime;
	}
	
	/**
	 * 获取该系统属性FLOW在时间executeTime前一个周期内的波动值
	 * @param executeTime 当前执行时间
	 * @return FLOW波动值
	 */
	private double getFLOWWaveValue(long executeTime){
		
		GrabForStatisticsDao grabForStatisticsDao = new GrabForStatisticsDao();
		//获取该点在executeTime之前一个周期的的所有值;
		List<Double> values = grabForStatisticsDao.getValuesByDataPointIdBewteenTimes(dpid,executeTime-MS_IN_CYCLE,executeTime);
		//如果有数据丢失，直接返回，不在操作
		//if(values.size()!=COUNT_IN_CYCLE) return -1D;
		if(values.size()==0) return -1D;
		//一个小时内最大的值
		double minValue = values.get(0);
		if(minValue<0){
			minValue=0;
		}
		//一个小时内最小的值
		double maxValue = values.get(values.size()-1);
		//返回波动
		return (double)(maxValue-minValue)/(double)((maxValue+minValue)/2);
	}
	
	/**
	 * 任务：开启[节能指标五]统计线程
	 * @author 王金阳
	 *
	 */
	class ScheduledTask extends TimerTask{
		
		@Override
		public void run(){
			EnergSavingTargetNo5RT energSavingTargetNo5RT = new EnergSavingTargetNo5RT();
			Thread energSavingTargetNo1RTHandler = new Thread(energSavingTargetNo5RT);
			energSavingTargetNo1RTHandler.start();
		}
		
	} 
	
	
}