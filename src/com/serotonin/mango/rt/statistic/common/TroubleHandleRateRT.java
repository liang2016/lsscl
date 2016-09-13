package com.serotonin.mango.rt.statistic.common;

import java.util.Date;
import java.util.Timer;
import java.util.List;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Calendar;
import com.serotonin.mango.vo.acp.ACPVO;
import com.serotonin.mango.vo.acp.ACPSystemVO;
import com.serotonin.mango.vo.statistics.ScheduledStatisticVO;
import com.serotonin.mango.db.dao.acp.CompressedAirSystemDao;
import com.serotonin.mango.vo.statistics.StatisticsScriptVO;
import com.serotonin.mango.vo.statistics.SumInSameTimeVO;
import com.serotonin.mango.db.dao.acp.ACPTypeDao;
import com.serotonin.mango.db.dao.statistics.ScheduledStatisticDao;
import com.serotonin.mango.db.dao.PointValueDao;
/**
 * 1-(当前存在的不同种类故障的个数/一段时间内不同种类故障的发生数之和）
 * @author 王金阳
 *
 */
public class TroubleHandleRateRT extends StatisticsRT{
	
	
	/**
	 * Warning Code
	 */
	public static final int WARNING_CODE = StatisticsUtil.STATISTICS_PARAM_ACP_WARNING_BY_STOPPAGE;

	/**
	 * 故障报警 对应的点的ID，每个系统都不同
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
		
		ACPTypeDao acpTypeDao=new ACPTypeDao();
		
		long executeTime = -1;//当前执行那个时间的统计
		//初始化时候统计一天的
				//当前统计类型/
		int statisticsType=StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_MACHINE_DAY;//7,8,9,10
		long PRIVATE_MS_IN_CYCLE=1000*60*60*24l;//私用的周期,默认是天	
		while(statisticsType<=10){
			for(ACPSystemVO systemVO:systemList){//循环所有系统
					
					startTime = -1;//每个系统的开始统计时间都要再次去检测上次统计到哪里
					
					/**
					 * 当前系统下空压机台数，如果有0台，则放弃此系统的统计
					 */
					List<ACPVO> acpList = systemDao.getACPsByACSId(systemVO.getId());//获取该系统下的所有空压机
					if(acpList==null||acpList.size()==0){
						continue;
					}
					
					/**
					 * 当前系统下空压机是否都对应的有 故障报警 统计参数的点，如果有0台对应的有，则放弃此系统的统计
					 */
					
					boolean requisite = false;//是否有必要统计
					List<Integer> dpids = new ArrayList<Integer>();
					for(ACPVO acpVO:acpList){
						int dpid1 = grabForStatisticsDao.findDataPointIdFromAcpBySpid(acpVO.getId(),WARNING_CODE);
						if(dpid1!=-1){
							requisite = true;
							dpids.add(dpid1);
							/**
							 * 该统计历史上最后一次统计时间的下一个统计时间
							 */
							long tempStartTime = grabForStatisticsDao.getLastExecuteTime(StatisticsUtil.RATE_OF_TROUBLE_HANDLE,acpVO.getId(),statisticsType)+PRIVATE_MS_IN_CYCLE;				
							if(tempStartTime>startTime){
								startTime=tempStartTime;
							}
						}
					}
					if(requisite==false){ continue; }
					
					if(startTime==PRIVATE_MS_IN_CYCLE-1||startTime<0){//如果没有找到历史统计记录
						/**
						 * 则从相关点最早被采集到的数据的时间开始
						 */
						startTime = grabForStatisticsDao.getEarliestCollectTimeFromDpids(dpids);
						if(startTime==-1){//如果该点根本没有采集到过数据
							/**********放弃当前系统统计，继续下一个系统***********/
							continue;
						}else{
							startTime = getNextExecuteTime(startTime,statisticsType);	
						}
					}
					
					//循环该系统需要统计的时间段内的每个周期
					while(startTime<=endTime){
						executeTime = startTime; 
						double result,result1,result2 = 0D;
						for(int i=0;i < acpList.size();i++){
							ACPVO acpVO = acpList.get(i);
							//取出当前空压机型号 的所有警报*0.2 报警*1
							String warnCount=acpTypeDao.getAcpWarnOrAlarmCount(acpVO.getId(),0,"warnCount");
							String alarmCount=acpTypeDao.getAcpWarnOrAlarmCount(acpVO.getId(),0,"alarmCount");
							if(warnCount=="0"||alarmCount=="0"){
								continue;
							}
							//这里要分割 警告和报警
							String sbWarn[]=warnCount.split(",");
							String sbAlarm[]=alarmCount.split(",");
							double codeAll=sbWarn.length*0.2+sbAlarm.length*1; //分母
							if(codeAll==0){
								continue;
							}
							int dpid1 = grabForStatisticsDao.findDataPointIdFromAcpBySpid(acpVO.getId(),WARNING_CODE);
							if(dpid1==-1){
								continue;
							}
							
							
							
							/*****取分母***/
							double denominator1,denominator2=0D;
							//WARNING_CODE
							denominator1=grabForStatisticsDao.getCountOfAllByPointId(dpid1,getPreviousExecuteTime(executeTime,statisticsType),executeTime,warnCount,false);
							//ALARM_CODE
							denominator2=grabForStatisticsDao.getCountOfAllByPointId(dpid1,getPreviousExecuteTime(executeTime,statisticsType),executeTime,alarmCount,false);
							if(denominator1==-1&&denominator2==-1){
								long count=new PointValueDao().dateRangeCount(dpid1,executeTime,endTime);
								if(count<1){
									break;
								}
								else
									continue;
							}
							if(denominator1==-1){
								denominator1=0D;
							}
							if(denominator2==-1){
								denominator2=0D;
							}
							result1=(denominator1*0.2+denominator2*1);//这里是分母
							if(result1!=0D){
								/*****取分子***/
								double molecular1=0D;//分子 1
								//TODO:
								molecular1=grabForStatisticsDao.getValueByDataPointIdBewteenTimes(dpid1,getPreviousExecuteTime(executeTime,statisticsType),executeTime);
								if(molecular1!=-1){
									String code[]=warnCount.split(",");
									for (int j = 0; j < code.length; j++) {
										if(code[j].equals(Double.toString(molecular1))){
											result2=1*0.2;
											break;
										}
									}
									if(result2==0D){
										 code=alarmCount.split(",");
										 for (int k = 0; k < code.length; k++) {
												if(code[k].equals(Double.toString(molecular1))){
													result2=1*1;
													break;
												}
										}
									}
								}
								
								result=1-(result2/result1);
							}
							else{
								result=1D-0D;
							}
							/**
							 * 将结果放进数据库中 
							 */
							/*************************************/
							ScheduledStatisticVO scheduledStatisticVO = new ScheduledStatisticVO(StatisticsUtil.RATE_OF_TROUBLE_HANDLE,result,executeTime,statisticsType,acpVO.getId());
			//					System.out.println("target: 1 	SYSTEM: "+systemVO.getId()+"	"+new Date(executeTime).toLocaleString()+":  "+result);
							/*************************************/
							scheduledStatisticDao.save(scheduledStatisticVO);
						}
						
						//准备下一个周期的统计
						startTime=getNextExecuteTime(startTime,statisticsType);
					}
				}
				statisticsType++;
				startTime = -1;
				if(statisticsType==8){
					PRIVATE_MS_IN_CYCLE=1000*60*60*24*7l;//周
				}
				else if(statisticsType==9){//月
					PRIVATE_MS_IN_CYCLE=1000*60*60*24*30l;
				}
				else if(statisticsType==10){//季度
					PRIVATE_MS_IN_CYCLE=1000*60*60*24*30*3l;
				}
				else{
					PRIVATE_MS_IN_CYCLE=1000*60*60*24l;
				}
				if(statisticsType>10){
					break;
				}
			}
				//经过这次统计，预定统计应该是执行到endTime
			startTime = endTime;
			
	}

	/**
	 * 获取下一个统计时间
	 */
	public static long getNextExecuteTime(long ts,int PRIVATE_MS_IN_CYCLE){
		Calendar cal = Calendar.getInstance();
		if(ts!=-1){
			cal.setTimeInMillis(ts);
		}
		cal.set(Calendar.MILLISECOND,0);
		if(PRIVATE_MS_IN_CYCLE==7){//天
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MINUTE,0); 
			cal.set(Calendar.HOUR_OF_DAY,0);
			cal.set(Calendar.DAY_OF_YEAR,cal.get(Calendar.DAY_OF_YEAR)+1);
		}else if(PRIVATE_MS_IN_CYCLE==8){//周
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MINUTE,0); 
			cal.set(Calendar.HOUR_OF_DAY,0);
			if(cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY){
				cal.set(Calendar.WEEK_OF_YEAR,cal.get(Calendar.WEEK_OF_YEAR)+1);
			}
			cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
			
		}
		else if(PRIVATE_MS_IN_CYCLE==9){//月
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MINUTE,0); 
			cal.set(Calendar.HOUR_OF_DAY,0);
			if(cal.get(Calendar.DAY_OF_MONTH)!=1){
				cal.set(Calendar.DAY_OF_MONTH,1);
			}
			cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)+1);
			
		}
		else if(PRIVATE_MS_IN_CYCLE==10){//季度
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MINUTE,0); 
			cal.set(Calendar.HOUR_OF_DAY,0);
			cal.set(Calendar.DAY_OF_MONTH,1);
			int month=cal.get(Calendar.MONTH);
			if(month>=1&&month<=3){//当前是第一季度,下一个季度是5
					cal.set(Calendar.MONTH,4);
				}
			if(month>=4&&month<=6){//当前是第一季度,下一个季度是8
					cal.set(Calendar.MONTH,7);
			}
			if(month>=7&&month<=9){//当前是第一季度,下一个季度是11
					cal.set(Calendar.MONTH,10);
				}
			if(month>=10&&month<=12){//当前是第一季度,下一个季度是2
					cal.set(Calendar.MONTH,1);
					cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)+1);
			}
		}
		return cal.getTimeInMillis();
	}
	
	/**
	 * 获取前一个统计时间
	 */
	public static long getPreviousExecuteTime(long ts,int PRIVATE_MS_IN_CYCLE){
		Calendar cal = Calendar.getInstance();
		if(ts!=-1){
			cal.setTimeInMillis(ts);
		}
		cal.set(Calendar.MILLISECOND,0);
		if(PRIVATE_MS_IN_CYCLE==7){//天
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MINUTE,0); 
			cal.set(Calendar.HOUR_OF_DAY,0);
			cal.set(Calendar.DAY_OF_YEAR,cal.get(Calendar.DAY_OF_YEAR)-1);
		}else if(PRIVATE_MS_IN_CYCLE==8){//周
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MINUTE,0); 
			cal.set(Calendar.HOUR_OF_DAY,0);
			if(cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY){
				cal.set(Calendar.WEEK_OF_YEAR,cal.get(Calendar.WEEK_OF_YEAR)-1);
			}
			cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
			
		}
		else if(PRIVATE_MS_IN_CYCLE==9){//月
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MINUTE,0); 
			cal.set(Calendar.HOUR_OF_DAY,0);
			if(cal.get(Calendar.DAY_OF_MONTH)!=1){
				cal.set(Calendar.DAY_OF_MONTH,1);
			}
			cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)-1);
			
		}
		else if(PRIVATE_MS_IN_CYCLE==10){//季度
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MINUTE,0); 
			cal.set(Calendar.HOUR_OF_DAY,0);
			cal.set(Calendar.DAY_OF_MONTH,1);
			int month=cal.get(Calendar.MONTH);
			if(month>=1&&month<=3){//当前是第一季度,上一个季度是10
					cal.set(Calendar.MONTH,10);
				}
			if(month>=4&&month<=6){//当前是2季度,上一个季度是1
					cal.set(Calendar.MONTH,1);
			}
			if(month>=7&&month<=9){//当前是第3季度,上一个季度是4
					cal.set(Calendar.MONTH,4);
				}
			if(month>=10&&month<=12){//当前是第4季度,上一个季度是7
					cal.set(Calendar.MONTH,7);
					cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)+1);
			}
		}
		return cal.getTimeInMillis();
	}
	/**
	 * 任务：开启[故障处理率]统计线程
	 * @author 王金阳
	 *
	 */
	class ScheduledTask extends TimerTask{
		
		@Override
		public void run(){
			TroubleHandleRateRT troubleHandleRateRT = new TroubleHandleRateRT();
			Thread troubleHandleRateRTHandler = new Thread(troubleHandleRateRT);
			troubleHandleRateRTHandler.start();
		}
		
	}
	
}
