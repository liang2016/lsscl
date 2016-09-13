package com.serotonin.mango.rt.statistic.common;

import java.util.Calendar;

/**
 * 所有统计线程的父类
 * ****************************************************************************************
 * 在此阐述一下线程统计的流程：															 **
 * 流程:																					 **	
 * 自从服务开启，所有的统计线程都开始运行，每个线程都会先检测上次统计到哪里(lastExecuteTime)	 **
 * 如果lastExecuteTime+周期<=当前时间，则开启线程统计这个时间段的数据。						 **
 * 		统计完毕则再次检测lastExecuteTime+周期<=当前时间，如果是，			 				 **
 * 		则再次开启线程统计这个时间段的数据...一直到统计赶上当前时间，						 **
 * 		即lastExecuteTime+周期>当前时间。 												 **	
 * 如果lastExecuteTime+周期>当前时间 ，则开启定时器，准备在[lastExecuteTime+周期-当前时间]  **
 * 启动线程，执行之前一个周期时间段内的的数据的统计。以后每隔一个周期一次。 	 				 **
 * ****************************************************************************************
 * @author 王金阳
 *
 */
public class StatisticsRT extends Thread{
	
	/**
	 * 统计周期
	 */
	public static final long MS_IN_CYCLE = getMsInCycle();
	//一天
	public static final long MS_IN_CYCLE_DAY = 1000*60*60*24;
	
	/**
	 * 一个周期内一个点应该采集的数据的个数
	 */
	public static final int COUNT_IN_CYCLE = (int) (MS_IN_CYCLE/StatisticsUtil.getCollectCycle());
	
	/**
	 * 线程开关，默认处于开启状态(非切断状态)
	 */
	public boolean off = false;
	
	/**
	 * 需要统计什么时间之后的数据
	 */
	public long startTime = -1L;
	
	/**
	 * 需要统计什么时间之前的数据
	 */
	public long endTime;
	
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	
	/**
	 * 获取周期内的毫秒数
	 * @return 
	 */
	public static long getMsInCycle(){
		int cycle = StatisticsUtil.getStatisticsCycle();
		long ms = -1L;
		if(cycle==1){//秒
			ms = 1000;
		}else if(cycle==2){//分
			ms = 1000*60;
		}else if(cycle==3){//时
			ms = 1000*60*60;
		}else if(cycle==4){//天
			ms = 1000*60*60*24;
		}else if(cycle==5){
			ms = 1000*60*60*24*7;
		}
		return ms;
	}
	
	/**
	 * 获取上一个统计时间
	 */
	public static long getPrevExecuteTime(long ts){
		Calendar cal = Calendar.getInstance();
		if(ts!=-1){
			cal.setTimeInMillis(ts);
		}
		int cycle = StatisticsUtil.getStatisticsCycle();
		if(cycle==1){
			cal.set(Calendar.MILLISECOND,0);
		}else if(cycle==2){
			cal.set(Calendar.MILLISECOND,0);
			cal.set(Calendar.SECOND,0);
		}else if(cycle==3){
			cal.set(Calendar.MILLISECOND,0);
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MINUTE,0); 
		}else if(cycle==4){
			cal.set(Calendar.MILLISECOND,0);
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MINUTE,0); 
			cal.set(Calendar.HOUR_OF_DAY,0);
		}else if(cycle==5){
			cal.set(Calendar.MILLISECOND,0);
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MINUTE,0); 
			cal.set(Calendar.HOUR_OF_DAY,0);
			if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
				cal.set(Calendar.WEEK_OF_YEAR,cal.get(Calendar.WEEK_OF_YEAR)-1);
			}
			cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		}
		return cal.getTimeInMillis();
	}
	 
	
	/**
	 * 获取下一个统计时间
	 */
	public static long getNextExecuteTime(long ts){
		Calendar cal = Calendar.getInstance();
		if(ts!=-1){
			cal.setTimeInMillis(ts);
		}
		cal.set(Calendar.MILLISECOND,0);
		int cycle = StatisticsUtil.getStatisticsCycle();
		if(cycle==1){
			cal.set(Calendar.SECOND,cal.get(Calendar.SECOND)+1); 
		}else if(cycle==2){
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MINUTE,cal.get(Calendar.MINUTE)+1); 
		}else if(cycle==3){
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MINUTE,0); 
			cal.set(Calendar.HOUR_OF_DAY,cal.get(Calendar.HOUR_OF_DAY)+1);
		}else if(cycle==4){
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MINUTE,0); 
			cal.set(Calendar.HOUR_OF_DAY,0);
			cal.set(Calendar.DAY_OF_YEAR,cal.get(Calendar.DAY_OF_YEAR)+1);
		}else if(cycle==5){
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MINUTE,0); 
			cal.set(Calendar.HOUR_OF_DAY,0);
			if(cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY){
				cal.set(Calendar.WEEK_OF_YEAR,cal.get(Calendar.WEEK_OF_YEAR)+1);
			}
			cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
			
		}
		return cal.getTimeInMillis();
	}
	
	
	 
}
