package com.serotonin.mango.rt.statistic.common;
import com.serotonin.mango.Common;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import com.serotonin.mango.vo.acp.ACPVO;
import com.serotonin.mango.db.dao.acp.ACPDao;
import com.serotonin.mango.vo.statistics.RunInSameTimeVO;
import com.serotonin.mango.vo.statistics.ScheduledStatisticVO;
import com.serotonin.mango.vo.statistics.StatisticsScriptVO;
import com.serotonin.mango.rt.statistic.common.GrabForStatisticsDao;
import com.serotonin.mango.db.dao.acp.CompressedAirSystemDao;
import com.serotonin.mango.vo.acp.ACPSystemVO;
import com.serotonin.mango.db.dao.statistics.StatisticsInformationDao;
import com.serotonin.mango.db.dao.statistics.ScheduledStatisticDao;
import com.serotonin.mango.rt.statistic.common.StatisticsUtil;
import java.util.Collections;

/**
 * 节能指标四
 * [        同时开机数量差  max(同时开机数量)-min(同时开机数量)>0 大于0，结果则为0，开机数量差为0，结果则为1    ]
 * @author 王金阳
 *
 */
public class EnergSavingTargetNo4RT extends StatisticsRT{
	
	
	/**
	 * 机器状态
	 */
	public static final int STATUS = StatisticsUtil.STATISTICS_PARAM_ACP_STATUS_RUN_STOP;
	
	public static final int RUN = StatisticsInformationDao.ACP_STATUS_RUN;
	
	
	/**
	 * 机器状态对应的点的ID，每个机器都不同
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
			
			List<ACPVO> acpList = systemDao.getACPsByACSId(systemVO.getId());//获取该系统下的所有空压机
			
			if(acpList==null||acpList.size()==0){
				/**
				 * 该系统下没有空压机，则放弃统计，继续下一个系统的统计
				 */
				continue;
			}
			
			boolean requisite = false;//是否有必要统计
			/**
			 * 系统下所有机器的开机关机对应的点
			 */
			List<Integer> dpids = new ArrayList<Integer>();
			for(ACPVO acpVO:acpList){
				dpid = grabForStatisticsDao.findDataPointIdFromAcpBySpid(acpVO.getId(),STATUS);
				if(dpid!=-1){
					requisite = true;
					dpids.add(dpid);
				}
			}
			
			if(requisite==false){ 
				continue;
			}
			
			/**
			 * 该统计历史上最后一次统计时间的下一个统计时间
			 */
			startTime = grabForStatisticsDao.getLastExecuteTime(StatisticsUtil.ENERGY_SAVING_TARGET_NO4,systemVO.getId())+MS_IN_CYCLE;				
			if(startTime==MS_IN_CYCLE-1){//如果没有找到历史统计记录 
				/**
				 * 则从最早被采集到的点的时间开始
				 */
				startTime = grabForStatisticsDao.getEarliestCollectTimeFromDpids(dpids);
				if(startTime==-1){//如果该点根本没有采集到过数据
					/**********放弃当前系统统计，继续下一个系统***********/
					continue;
				}else{
					startTime = getNextExecuteTime(startTime);	
				}
			}
			
			//循环该系统需要统计的时间段内的每个周期
			while(startTime<=endTime){
				
				executeTime = startTime; 
				
				List<Integer> values = new ArrayList<Integer>();
				values.add(RUN);
				List<RunInSameTimeVO> list = grabForStatisticsDao.getRunInSameTimeCountOfAcpSystemList(systemVO.getId(),STATUS,executeTime-MS_IN_CYCLE,executeTime,values);
				
				/**
				 * 同时开机数量差计算分析
				 * 找到只有一个机器开启的时刻startTs，找到有0台机器开启的时刻endTs。
				 * 计算 startTs+15minute到endTs-15minute之间的同时开机数量差
				 * 
				 */
				
				double result = 1D;
				
				//处理过的结果
				List<RunInSameTimeVO> formatList = formatResult(list);
				
				if(formatList.size()>0){
					result = formatList.get(formatList.size()-1).getCount() - formatList.get(0).getCount();
				}else{
					result = 0D;
				}
				/**
				 * 将结果放进数据库中 
				 */
				/*************************************/
				ScheduledStatisticVO scheduledStatisticVO = new ScheduledStatisticVO(StatisticsUtil.ENERGY_SAVING_TARGET_NO4,result,executeTime,StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM,systemVO.getId());
//					System.out.println("target: 1 	SYSTEM: "+systemVO.getId()+"	"+new Date(executeTime).toLocaleString()+":  "+result);
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
	 * 集体开机后15分钟，集体停机前15分钟数据不做统计
	 * @param list 周期内开机状态集合
	 * @return 格式化后的集合
	 */
	private List<RunInSameTimeVO> formatResult(List<RunInSameTimeVO> list){
		
		List<RunInSameTimeVO> formatList = new ArrayList<RunInSameTimeVO>();
		int ignoreCount = (int)StatisticsUtil.getShutdownUpInterval()/(int)StatisticsUtil.getCollectCycle();
		boolean direction = true;
		long station = 0L;
		for (int i = 0; i < list.size()-1; i++) {
			RunInSameTimeVO vo = list.get(i);
			RunInSameTimeVO nextVO = list.get(i+1);
			if(i!=0){
				if(direction){
					formatList.add(vo);
				}else{
					formatList.remove(vo);
				}
			}
			//集体开机(记录15分钟后的数据)
			if(vo.getCount()==0&&nextVO.getCount()==1){
				i=i+ignoreCount;
				direction=true;
			}
			//集体关机(删除15分钟内的数据)
			if(vo.getCount()==1&&nextVO.getCount()==0&&station!=vo.getTs()){
				i=i-ignoreCount;
				direction=false;
				station = vo.getTs();
			}
		}
		Collections.sort(formatList);
		return formatList;
	}
	
	
	 
	
	/**
	 * 任务：开启[节能指标四]统计线程
	 * @author 王金阳
	 *
	 */
	class ScheduledTask extends TimerTask{
		
		@Override
		public void run(){
			EnergSavingTargetNo4RT energSavingTargetNo4RT = new EnergSavingTargetNo4RT();
			Thread energSavingTargetNo1RTHandler = new Thread(energSavingTargetNo4RT);
			energSavingTargetNo1RTHandler.start();
		}
		
	} 
	
	
}