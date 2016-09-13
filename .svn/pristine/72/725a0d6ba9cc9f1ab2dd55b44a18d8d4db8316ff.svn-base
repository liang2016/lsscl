package com.serotonin.mango.rt.statistic.common;


import java.util.List;
import java.util.Date;
import java.util.Timer;
import java.util.ArrayList;
import java.util.TimerTask;
import com.serotonin.mango.vo.acp.ACPVO;
import com.serotonin.mango.db.dao.acp.ACPDao;
import com.serotonin.mango.vo.acp.ACPSystemVO;
import com.serotonin.mango.vo.statistics.StatisticsScriptVO;
import com.serotonin.mango.vo.statistics.ScheduledStatisticVO;
import com.serotonin.mango.db.dao.acp.CompressedAirSystemDao;
import com.serotonin.mango.db.dao.statistics.ScheduledStatisticDao;
import com.serotonin.mango.Common;
/**
 * 
 * [ 存入(L1+L2….+Ln)/n ]
 * @author 王金阳
 *
 */
public class EnergSavingTargetNo2RT extends StatisticsRT{
	
	/**
	 * 机器电流
	 */
	public static final int POWER_TIME = StatisticsUtil.STATISTICS_PARAM_ACP_ELECTRICITY;
	
	/**
	 * 机器的运行时间
	 */
	//public static final int RUN_TIME = StatisticsUtil.STATISTICS_PARAM_ACP_RUN_TIME;
	
	
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
	public void executeStatistic() {
		
		/**
		 * 是以系统为统计单位的
		 */
		CompressedAirSystemDao systemDao = new CompressedAirSystemDao();
		
		List<ACPSystemVO> systemList = systemDao.getAllACPSystem();//获取所有系统
		
		GrabForStatisticsDao grabForStatisticsDao = new GrabForStatisticsDao();
		
		ScheduledStatisticDao scheduledStatisticDao = new ScheduledStatisticDao();
		
		ACPDao acpDao = new ACPDao();
		
		long executeTime = -1;//当前执行那个时间的统计
		
		for(ACPSystemVO systemVO:systemList){//循环所有系统
			
			startTime = -1;//每个系统的开始统计时间都要再次去检测上次统计到哪里
			
			List<ACPVO> acpList = systemDao.getACPsByACSId(systemVO.getId());//获取该系统下的所有空压机
			if(acpList==null || acpList.size()==0){
				continue;
			} 
			boolean requisite = false;//当前系统是否需要统计
			
			/**
			 * 系统下所有机器的全载时间和运行时间对应的点
			 */
			List<Integer> dpids = new ArrayList<Integer>();
			
			for(ACPVO acpVO:acpList){
				//当前空压机 电流对应的点
				int powerDpid = grabForStatisticsDao.findDataPointIdFromAcpBySpid(acpVO.getId(),POWER_TIME);
				//当前空压机 运行时间对应 的点
			//	int runtimeDpid = grabForStatisticsDao.findDataPointIdFromAcpBySpid(acpVO.getId(),RUN_TIME);
				
				if(powerDpid!=-1){
					/**
					 * 当前系统只有下面有一台空压机有相对应的点，就需要统计
					 */
					requisite = true;
					dpids.add(powerDpid);
					//dpids.add(runtimeDpid);
				}
			}
			
			if(requisite==false){
				continue;
			}
			
			/**
			 * 这个系统上一次统计到什么时候了
			 */
			startTime = grabForStatisticsDao.getLastExecuteTime(StatisticsUtil.ENERGY_SAVING_TARGET_NO2,systemVO.getId())+MS_IN_CYCLE;
			
			//以前没有统计记录，现在是第一次
			if(startTime==MS_IN_CYCLE-1){
				//如果没有过统计历史记录，则以此系统下所有机 器中对应 全载时间 和 运行时间 的点最早被采集到数据的时间
				startTime = grabForStatisticsDao.getEarliestCollectTimeFromDpids(dpids);
				if(startTime==-1){
					//该系统下空压机虽然有对应的点，可是每个点都没有数据，退出当前系统，继续下一个系统
					continue;
				}else{
					startTime = getNextExecuteTime(startTime);
				}
			}
			/**
			 * 统计这个系统
			 */
			while(startTime<=endTime){
				executeTime = startTime; 
				long ladenTime = 0L;
				//long runtime = 0L;
				Integer L=0;//记录结果之和 (L1+…Ln)
				Integer N=0;//记录L的个数 n
				for(ACPVO acpVO:acpList){
					
					//当前空压机 电流对应的点
					int powerDpidAcp = grabForStatisticsDao.findDataPointIdFromAcpBySpid(acpVO.getId(),POWER_TIME);
					//当前空压机 运行时间对应 的点
					//int runtimeDpid = grabForStatisticsDao.findDataPointIdFromAcpBySpid(acpVO.getId(),RUN_TIME);
					//取出空压机的功率
					double power=grabForStatisticsDao.findAcpPowerByid(acpVO.getId());
					if(powerDpidAcp==-1||power==-1){
						continue;//忽略这台机器
					}
					
					List<Double> powerValues = grabForStatisticsDao.getValuesByDataPointIdBewteenTimes(powerDpidAcp,executeTime-MS_IN_CYCLE,executeTime);
					
					//List<Double> runTimeValues = grabForStatisticsDao.getValuesByDataPointIdBewteenTimes(runtimeDpid,executeTime-MS_IN_CYCLE,executeTime);
					
					//if(ladenTimeValues!=null&&runTimeValues!=null&&ladenTimeValues.size()==runTimeValues.size()&&runTimeValues.size()==COUNT_IN_CYCLE){
					if(powerValues!=null&&powerValues.size()>0){
						for (int i = 0; i < powerValues.size(); i++) {
							if(powerValues.get(i)/(power/0.6)>0.5)
								L++;
								N++;
							}
						}
					else
						continue;
						//ladenTime += (ladenTimeValues.get(ladenTimeValues.size()-1)-ladenTimeValues.get(0));
						//runtime += (runTimeValues.get(runTimeValues.size()-1)-runTimeValues.get(0));
					
				}
				/**
				 * 
				if(ladenTime==0L&&runtime==0L||ladenTime>runtime){
					startTime+=MS_IN_CYCLE;
					continue;
				}
				 */
				if(N<1){
					startTime+=MS_IN_CYCLE;
					continue;
				}
				double result=L/N;

				/**
				 * 将结果放进数据库中 
				 */
				/*************************************/
				ScheduledStatisticVO scheduledStatisticVO = new ScheduledStatisticVO(StatisticsUtil.ENERGY_SAVING_TARGET_NO2,result,executeTime,StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM,systemVO.getId());
//				System.out.println("target: 1 	SYSTEM: "+systemVO.getId()+"	"+new Date(executeTime).toLocaleString()+":  "+result);
				/*************************************/
				scheduledStatisticDao.save(scheduledStatisticVO);
				
				//准备下一个周期的统计
				startTime+=MS_IN_CYCLE;
		}
	}	//经过这次统计，预定统计应该是执行到endTime
	startTime = endTime;
}

/**
 * 任务：开启[节能指标一]统计线程
 * @author 王金阳
 *
 */
class ScheduledTask extends TimerTask{
	
		@Override
		public void run(){
			EnergSavingTargetNo2RT energSavingTargetNo2RT = new EnergSavingTargetNo2RT();
			Thread energSavingTargetNo1RTHandler = new Thread(energSavingTargetNo2RT);
			energSavingTargetNo1RTHandler.start();
		}
		
	} 
}
