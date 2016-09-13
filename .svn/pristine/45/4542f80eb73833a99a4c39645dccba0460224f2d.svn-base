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
public class EnergSavingIndexRT extends StatisticsRT{

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
		
		/**
		 * 是以工厂单位的
		 */
		CompressedAirSystemDao systemDao = new CompressedAirSystemDao();
		List<ScopeVO> factorys= new ScopeDao().getFactoryByHq();//这里是总部统计 获取所有工厂
		GrabForStatisticsDao grabForStatisticsDao = new GrabForStatisticsDao();
		
		ScheduledStatisticDao scheduledStatisticDao = new ScheduledStatisticDao();
		
		long executeTime = -1;//当前执行那个时间的统计
		for (ScopeVO factory : factorys) {
			int systemCount=0;
			int acpCount=0;
			List<Integer> systemDpids = new ArrayList<Integer>();
			List<Integer> acpDpids = new ArrayList<Integer>();
			Double target1ValueMax=0D;
			Double target1ValueMin=0D;
			List<Double> target2Values=new ArrayList<Double>();
			int E1=0;
			int E2=0;
			int E3=0;
			startTime = -1;//每个系统的开始统计时间都要再次去检测上次统计到哪里
			
			/**
			 * 该统计历史上最后一次统计时间的下一个统计时间
			 */
			startTime = grabForStatisticsDao.getLastExecuteTime(StatisticsUtil.ENERGY_SAVING_INDEX,factory.getId())+MS_IN_CYCLE;				

			if(startTime==MS_IN_CYCLE-1){//如果没有找到历史统计记录 
				/**
				 * 节能指标1.2.3.4.5 统计最早时间
				 */
				List<Integer> targetsIds = new ArrayList<Integer>();
				targetsIds.add(StatisticsUtil.ENERGY_SAVING_TARGET_NO1);
				targetsIds.add(StatisticsUtil.ENERGY_SAVING_TARGET_NO2);
				targetsIds.add(StatisticsUtil.ENERGY_SAVING_TARGET_NO3);
				startTime = grabForStatisticsDao.getFirstStatisticTime(targetsIds);
				if(startTime==-1){//如果该点根本没有采集到过数据
					/**********放弃当前系统统计，继续下一个系统***********/
					continue;
				}else{
					startTime= startTime;
				}
			}
			
			List<ACPSystemVO> systemList = systemDao.getACPSystemVOByfactoryId(factory.getId());//根据工厂编号获取所有系统
			for(ACPSystemVO systemVO:systemList){//循环所有系统
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
				/**
				 * 当前系统P总管对应的点是否存在，如果不存在，则放弃此系统的统计
				 */
				//取出系统总管压力的数据点ID
				int systemP = grabForStatisticsDao.findDataPointIdFromSystemBySpid(systemVO.getId(),SYSTEM_P);
				if(systemP==-1)
					continue;
				

				List<Integer> dpids = new ArrayList<Integer>();
				for(ACPVO acpVO:acpList){
					int dpid = grabForStatisticsDao.findDataPointIdFromAcpBySpid(acpVO.getId(),ACP_P);
					if(dpid!=-1){
						acpDpids.add(acpVO.getId());
						acpCount++;//几个空压机
					}
				}

				
				
				systemCount++;//几个系统
				systemDpids.add(systemVO.getId());
			}
			//循环该系统需要统计的时间段内的每个周期
			while(startTime<=endTime){
				executeTime = startTime; 
				for (int i = 0; i < systemDpids.size(); i++) {
					//TODO:这里的时间应该是一天内
					Double target1ValueMaxTemp = grabForStatisticsDao.getStatisticValueByScriptMax(StatisticsUtil.ENERGY_SAVING_TARGET_NO1,systemDpids.get(i),executeTime-(MS_IN_CYCLE_DAY/2),executeTime+(MS_IN_CYCLE_DAY/2));
					Double target1ValueMinTemp = grabForStatisticsDao.getStatisticValueByScriptMin(StatisticsUtil.ENERGY_SAVING_TARGET_NO1,systemDpids.get(i),executeTime-(MS_IN_CYCLE_DAY/2),executeTime+(MS_IN_CYCLE_DAY/2));
					//E1 temp
					if(target1ValueMaxTemp==null||target1ValueMinTemp==null){//如果没有E1
						continue;
					}
					if(target1ValueMaxTemp>target1ValueMax)
						target1ValueMax=target1ValueMaxTemp;
					if(target1ValueMinTemp<target1ValueMin)
						target1ValueMin=target1ValueMinTemp;
					//E2 temp
					Double target2Value = grabForStatisticsDao.getStatisticValueByScript(StatisticsUtil.ENERGY_SAVING_TARGET_NO2,systemDpids.get(i),executeTime-(MS_IN_CYCLE_DAY/2),executeTime+(MS_IN_CYCLE_DAY/2));
					if(target2Value!=null)
						target2Values.add(target2Value);
				}
				
				if((target1ValueMax-target1ValueMin)/target1ValueMax>0.1){
					E1=1;
				}
				//TODO:这里的时间应该是一天的内
				if(target2Values.size()==0){
					startTime+=MS_IN_CYCLE_DAY;
					continue;
				}
				for (Double e2 : target2Values) {
					if(e2<0.8){//如果有一个小于0.8 E2就等于1,终止循环
						E2=1;
						break;
					}
				}
				
				
				//TODO:这里的时间应该是一天的内
				Double target3P3Avg = grabForStatisticsDao.getStatisticValueByScriptAvg(StatisticsUtil.ENERGY_SAVING_TARGET_NO3,StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM_SYS,systemDpids,executeTime-(MS_IN_CYCLE_DAY/2),executeTime+(MS_IN_CYCLE_DAY/2));
				Double target3P2Avg = grabForStatisticsDao.getStatisticValueByScriptAvg(StatisticsUtil.ENERGY_SAVING_TARGET_NO3,StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM_ACP,acpDpids,executeTime-(MS_IN_CYCLE_DAY/2),executeTime+(MS_IN_CYCLE_DAY/2));
				if(target3P3Avg==null||target3P2Avg==null){
					startTime+=MS_IN_CYCLE_DAY;
					continue;
				}
				if((target3P2Avg-target3P3Avg)/target3P3Avg>0.1){
					E3=1;
				}
				double r1,r2,r3=0;
				if(systemCount<1||E1==0)
					r1=0;
				else
					r1=(double)1/(3*E1)/(systemCount);
				if(acpCount<1||E2==0)
					r2=0;
				else
					r2=(double)1/(3*E2)/(acpCount);
				if(acpCount<1||E3==0)
					r3=0;
				else
					r2=(double)1/(3*E3)/(acpCount);
				
				double result =r1+r2+r3;
				ScheduledStatisticVO scheduledStatisticVO = new ScheduledStatisticVO(StatisticsUtil.ENERGY_SAVING_INDEX,result,executeTime,StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_FACTORY,factory.getId());
				scheduledStatisticDao.save(scheduledStatisticVO);
				startTime+=MS_IN_CYCLE_DAY;
			}
			//准备下一个周期的统计
		}
			
		//经过这次统计，预定统计应该是执行到endTime
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