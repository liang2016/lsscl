package com.serotonin.mango.rt.statistic.common;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
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
import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.db.dao.scope.ScopeDao;
/**
 * 节能指数
 * [1/3(节能指标E1)/该区域总站房数+1/3(节能指标E2)/(该区域总台份数)+1/3(节能指标E3)/(该区域总台份数)
 * @author 王金阳
 *
 */
public class EnergSavingIndexRT2 extends StatisticsRT{

	/**
	 * P空压机
	 */
	public static final int ACP_P = StatisticsUtil.STATISTICS_PARAM_ACP_P;
	
	/**
	 * P总管
	 */
	public static final int SYSTEM_P = StatisticsUtil.STATISTICS_PARAM_SYSTEM_P3;
	

	@Override
	public void run() {
		
		while(!off){
			
			//上一个统计时间
			endTime = getPrevExecuteTime(-1);
			if(startTime<endTime){
				//if(ready()) 
				executeStatistic();
			}else{
//				//开启定时器，下一个统计时间再次启动
//				Timer timer = new Timer();
//				timer.schedule(new ScheduledTask(),getNextExecuteTime(-1) - (new Date().getTime()+10000));
				//关闭开关，终止线程
				Common.ctx.getRuntimeManager().completedIndex(endTime);
				off = true;
			}
		}
	}
	
	/**
	 * 统计startTime到endTime之间的数据
	 */
	public void executeStatistic(){
		
//		/**
//		 * 工厂为单位
//		 */
//		CompressedAirSystemDao systemDao = new CompressedAirSystemDao();
//		GrabForStatisticsDao grabForStatisticsDao = new GrabForStatisticsDao();
//		ScheduledStatisticDao scheduledStatisticDao = new ScheduledStatisticDao();
//		
//		List<ScopeVO> factorys= new ScopeDao().getFactoryByHq();//这里是总部统计 获取所有工厂
//		Double target1ValueMax;
//		Double target1ValueMin;
//		List<Double> target2Values;
//		long executeTime = -1;//当前执行那个时间的统计
//		List<Integer> systemDpids = new ArrayList<Integer>();
//		List<Integer> acpDpids = new ArrayList<Integer>();
//		int systemCount=0;
//		int acpCount=0;
//		for (ScopeVO factory : factorys) {
//			List<ACPSystemVO> systemList = systemDao.getACPSystemVOByfactoryId(factory.getId());//根据工厂编号获取所有系统
//			for(ACPSystemVO systemVO:systemList){//循环所有系统
//				startTime = -1;//每个系统的开始统计时间都要再次去检测上次统计到哪里
//				/**
//				 * 该统计历史上最后一次统计时间的下一个统计时间
//				 */
//				startTime = grabForStatisticsDao.getLastExecuteTime(StatisticsUtil.ENERGY_SAVING_INDEX,systemVO.getId())+MS_IN_CYCLE;
//				if(startTime==MS_IN_CYCLE-1){//如果没有找到历史统计记录 
//					/**
//					 * 节能指标1.2.3.4.5 统计最早时间
//					 */
//					List<Integer> targetsId = new ArrayList<Integer>();
//					targetsId.add(StatisticsUtil.ENERGY_SAVING_TARGET_NO1);
//					targetsId.add(StatisticsUtil.ENERGY_SAVING_TARGET_NO2);
//					startTime = grabForStatisticsDao.getFirstStatisticTime(targetsId);
//					if(startTime==-1){//如果该点根本没有采集到过数据
//						/**********放弃当前系统统计，继续下一个系统***********/
//						continue;
//					}else{
//						startTime= startTime;
//					}
//				}
//				//取节能指数3
//				/**
//				 * 当前系统P总管对应的点是否存在，如果不存在，则放弃此系统的统计
//				 */
//				//取出系统总管压力的数据点ID
//				int systemP = grabForStatisticsDao.findDataPointIdFromSystemBySpid(systemVO.getId(),SYSTEM_P);
//				if(systemP!=-1)
//					systemDpids.add(systemP);
//				else{
//					continue;
//				}
//				
//				/**
//				 * 当前系统下空压机台数，如果有0台，则放弃此系统的统计
//				 */
//				List<ACPVO> acpList = systemDao.getACPsByACSId(systemVO.getId());//获取该系统下的所有空压机
//				if(acpList==null||acpList.size()==0){
//					continue;
//				}
//				
//				/**
//				 * 当前系统下空压机是否都对应的有 P空压机统计参数的点，如果有0台对应的有，则放弃此系统的统计
//				 */
//				
//				boolean requisite = false;//是否有必要统计
//				List<Integer> dpids = new ArrayList<Integer>();
//				for(ACPVO acpVO:acpList){
//					int dpid = grabForStatisticsDao.findDataPointIdFromAcpBySpid(acpVO.getId(),ACP_P);
//					if(dpid!=-1){
//						requisite = true;
//						acpDpids.add(dpid);
//						acpCount++;
//					}
//				}
//				if(requisite==false){ 
//					continue; 
//				}
//				
//				
//				while(startTime<=endTime){//循环该系统需要统计的时间段内的每个周期
//					executeTime = startTime; 
//					Double target1ValueMaxTemp = grabForStatisticsDao.getStatisticValueByScriptMax(StatisticsUtil.ENERGY_SAVING_TARGET_NO1,systemVO.getId(),executeTime-(StatisticsUtil.getCollectCycle()/2),executeTime+(StatisticsUtil.getCollectCycle()/2));
//					Double target1ValueMinTemp = grabForStatisticsDao.getStatisticValueByScriptMin(StatisticsUtil.ENERGY_SAVING_TARGET_NO1,systemVO.getId(),executeTime-(StatisticsUtil.getCollectCycle()/2),executeTime+(StatisticsUtil.getCollectCycle()/2));
//					if(target1ValueMaxTemp>target1ValueMax)
//						target1ValueMax=target1ValueMaxTemp;
//					if(target1ValueMinTemp<target1ValueMin)
//						target1ValueMin=target1ValueMinTemp;
//					Double target2Value = grabForStatisticsDao.getStatisticValueByScript(StatisticsUtil.ENERGY_SAVING_TARGET_NO2,systemVO.getId(),executeTime-(StatisticsUtil.getCollectCycle()/2),executeTime+(StatisticsUtil.getCollectCycle()/2));
//					target2Values.add(target2Value);
//				}
//				systemCount++;
//			}
//			//所有系统总管压力
//			double headerDuctPressure = grabForStatisticsDao.getAvgValueByDataPointBewteenTimes(systemDpids,executeTime-MS_IN_CYCLE,executeTime);
//			double dischargePressure = grabForStatisticsDao.getAvgValueByDataPointBewteenTimes(acpDpids,executeTime-MS_IN_CYCLE,executeTime);	
//			int targetE1=0;
//			int targetE2=0;
//			int targetE3=0;
//			Double target2=0D;
//			Double result=0D;
//			if(systemCount>0){
//				if((target1ValueMax-target1ValueMin)/target1ValueMax>0.1)
//					targetE1=1;
//				for (int i = 0; i < target2Values.size(); i++) {
//					target2+=target2Values.get(i);
//				}
//				if(target2/target2Values.size()<0.8)
//					targetE2=1;
//				if((dischargePressure-headerDuctPressure)/headerDuctPressure>0.1)
//					targetE3=1;
//				result=(double)1/(3*targetE1)/(systemCount)+1/(3*targetE2)/(acpCount)+1/(3*targetE3)/(acpCount);
//			}
//		//	ScheduledStatisticVO scheduledStatisticVO = new ScheduledStatisticVO(StatisticsUtil.ENERGY_SAVING_INDEX,result,executeTime,StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM,systemVO.getId());
////				System.out.println("target: 1 	SYSTEM: "+systemVO.getId()+"	"+new Date(executeTime).toLocaleString()+":  "+result);
//					/*************************************/
//		//	scheduledStatisticDao.save(scheduledStatisticVO);
//		}
//				//准备下一个周期的统计
//				startTime+=MS_IN_CYCLE;
//		//经过这次统计，预定统计应该是执行到endTime
		startTime = endTime;
	}
	
	/**
	 * 任务：开启[节能指数]统计线程
	 * @author 王金阳
	 *
	 */
	class ScheduledTask extends TimerTask{
		
		@Override
		public void run(){
			EnergSavingIndexRT energSavingIndexRT = new EnergSavingIndexRT();
			Thread energSavingTargetNo1RTHandler = new Thread(energSavingIndexRT);
			energSavingTargetNo1RTHandler.start();
		}
		
	}

}