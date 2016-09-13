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
import com.serotonin.mango.vo.statistics.SumInSameTimeVO;
import java.util.ArrayList;
import com.serotonin.mango.db.dao.statistics.ScheduledStatisticDao;
/**
 * 节能指标三
 * [        (P空压机的和/空压机数量-P总管)/P总管 -10%总管      ]
 * @author 王金阳
 *
 */
public class EnergSavingTargetNo3RT extends StatisticsRT{
	
	/**
	 * P空压机
	 */
	public static final int ACP_P = StatisticsUtil.STATISTICS_PARAM_ACP_P;
	
	/**
	 * P总管
	 */
	public static final int SYSTEM_P = StatisticsUtil.STATISTICS_PARAM_SYSTEM_P3;
	
	/**
	 * 机器电流
	 */
	public static final int ACP_ELECTRICITY = StatisticsUtil.STATISTICS_PARAM_ACP_ELECTRICITY;
	
	
	
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
			
			/**
			 * 当前系统P总管对应的点是否存在，如果不存在，则放弃此系统的统计
			 */
			int systemP = grabForStatisticsDao.findDataPointIdFromSystemBySpid(systemVO.getId(),SYSTEM_P);
			if(systemP==-1){
				continue;
			}
			
			/**
			 * 当前系统下空压机台数，如果有0台，则放弃此系统的统计
			 */
			List<ACPVO> acpList = systemDao.getACPsByACSId(systemVO.getId());//获取该系统下的所有空压机
			if(acpList==null||acpList.size()==0){
				continue;
			}
			
			/**
			 * 当前系统下空压机是否都对应的有 P空压机统计参数的点，如果有0台对应的有，则放弃此系统的统计
			 */
			
			boolean requisite = false;//是否有必要统计
			List<Integer> dpids = new ArrayList<Integer>();
			for(ACPVO acpVO:acpList){
				int dpid = grabForStatisticsDao.findDataPointIdFromAcpBySpid(acpVO.getId(),ACP_P);
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
			startTime = grabForStatisticsDao.getLastExecuteTime(StatisticsUtil.ENERGY_SAVING_TARGET_NO3,systemVO.getId())+MS_IN_CYCLE;				
			if(startTime==MS_IN_CYCLE-1){//如果没有找到历史统计记录
				dpids.add(systemP);
				/**
				 * 则从相关点最早被采集到的数据的时间开始
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
				
				//当前系统当前周期内所有空压机电流值和最大的时刻
				long  maxValueTime = grabForStatisticsDao.getTimeOfValueIsMax(systemVO.getId(),ACP_ELECTRICITY,executeTime-MS_IN_CYCLE,executeTime);
				double totalDischargePressure = 0D;//空压机排气压力之和
				double totalCount = 0;//空压机有效个数
				if(maxValueTime!=-1){
					for(ACPVO acpVO:acpList){
						int dpid_dischargePressure = grabForStatisticsDao.findDataPointIdFromAcpBySpid(acpVO.getId(),ACP_P);
						if(dpid_dischargePressure==-1){continue;}
						List<Double> dischargePressureList = grabForStatisticsDao.getValuesByDataPointIdBewteenTimes(dpid_dischargePressure,maxValueTime-StatisticsUtil.getCollectCycle()/2,maxValueTime+StatisticsUtil.getCollectCycle()/2);
						if(dischargePressureList!=null&&dischargePressureList.size()>0){
							double dischargePressure = dischargePressureList.get(0);
							totalDischargePressure+=dischargePressure;
							totalCount++;
						}
					}
					List<Double> valueList = grabForStatisticsDao.getValuesByDataPointIdBewteenTimes(systemP,maxValueTime-StatisticsUtil.getCollectCycle()/2,maxValueTime+StatisticsUtil.getCollectCycle()/2);
					if(valueList!=null&&valueList.size()>0){
						Double headerDuctPressure2 = valueList.get(0);
						if(headerDuctPressure2!=null&&totalCount>0){
							double yajiang = totalDischargePressure/totalCount-(double)headerDuctPressure2;
							yajiang = Math.abs(yajiang);
							ScheduledStatisticVO scheduledStatisticVO1 = new ScheduledStatisticVO(StatisticsUtil.SYSTEM_YAJIANG,yajiang,executeTime,StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM,systemVO.getId());
							scheduledStatisticDao.save(scheduledStatisticVO1);
						}
					}
				}
				
				//系统下所有机器的排气压力的和
				totalDischargePressure = 0D;
				//系统的总管压力
				double headerDuctPressure = grabForStatisticsDao.getAvgValueByDataPointBewteenTimes(systemP,executeTime-MS_IN_CYCLE,executeTime);
				if(headerDuctPressure==-1d){
					//准备下一个周期的统计
					startTime+=MS_IN_CYCLE;
					continue;
				}
				//系统下空压机集合
				List<Integer> acpDischargePressures=new ArrayList<Integer>();
				for(ACPVO acpVO:acpList){
					int dpid_dischargePressure = grabForStatisticsDao.findDataPointIdFromAcpBySpid(acpVO.getId(),ACP_P);
					if(dpid_dischargePressure==-1){continue;}
					acpDischargePressures.add(dpid_dischargePressure);
					//当前空压机的压力
					double dischargePressure = grabForStatisticsDao.getAvgValueByDataPointBewteenTimes(dpid_dischargePressure,executeTime-MS_IN_CYCLE,executeTime);
					if(headerDuctPressure!=0D){
						//将计算结果保存进数据库
						ScheduledStatisticVO scheduledStatisticVOAcp = new ScheduledStatisticVO(StatisticsUtil.ENERGY_SAVING_TARGET_NO3,dischargePressure,executeTime,StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM_ACP,acpVO.getId());
						ScheduledStatisticVO scheduledStatisticVOSys = new ScheduledStatisticVO(StatisticsUtil.ENERGY_SAVING_TARGET_NO3,headerDuctPressure,executeTime,StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM_SYS,systemVO.getId());
						scheduledStatisticDao.save(scheduledStatisticVOAcp);
						scheduledStatisticDao.save(scheduledStatisticVOSys);
					}
				}
				//取出系统下所有空压机压力
				//double dischargePressure = grabForStatisticsDao.getAvgValueByDataPointBewteenTimes(acpDischargePressures,executeTime-MS_IN_CYCLE,executeTime);
				//计算公式中被除数不能为0。
//				if(acpDischargePressures.size()!=0){
//					if(headerDuctPressure!=0D){
//						//将计算结果保存进数据库
//						ScheduledStatisticVO scheduledStatisticVOAcp = new ScheduledStatisticVO(StatisticsUtil.ENERGY_SAVING_TARGET_NO3,dischargePressure,executeTime,StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM_ACP,systemVO.getId());
//						ScheduledStatisticVO scheduledStatisticVOSys = new ScheduledStatisticVO(StatisticsUtil.ENERGY_SAVING_TARGET_NO3,headerDuctPressure,executeTime,StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM_SYS,systemVO.getId());
//						scheduledStatisticDao.save(scheduledStatisticVOAcp);
//						scheduledStatisticDao.save(scheduledStatisticVOSys);
//					}
//				}
				
				//准备下一个周期的统计
				startTime+=MS_IN_CYCLE;
			}
		}
		//经过这次统计，预定统计应该是执行到endTime
		startTime = endTime;
		
	}
	 
	
	/**
	 * 任务：开启[节能指标三]统计线程
	 * @author 王金阳
	 *
	 */
	class ScheduledTask extends TimerTask{
		
		@Override
		public void run(){
			EnergSavingTargetNo3RT energSavingTargetNo3RT = new EnergSavingTargetNo3RT();
			Thread energSavingTargetNo1RTHandler = new Thread(energSavingTargetNo3RT);
			energSavingTargetNo1RTHandler.start();
		}
		
	} 
	
	
}