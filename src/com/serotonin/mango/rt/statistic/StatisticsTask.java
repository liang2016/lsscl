package com.serotonin.mango.rt.statistic;

import java.util.TimerTask;

import com.serotonin.mango.Common;
import com.serotonin.mango.vo.statistics.StatisticsScriptVO;

/**
 * 任务：在下一个整点时刻开启一个线程统计一个小时内的数据
 * @author 王金阳
 *
 */
public class StatisticsTask extends TimerTask{
	
	/**
	 * 任务中开启的线程数统计的脚本对象
	 */
	private StatisticsScriptVO scriptVO;
	/**
	 * 本次统计的统计时间
	 */
	private long statisticTime;
	
	/**
	 * 通过构造方法，把参数传递进来
	 * @param scriptVO 被统计的脚本信息
	 * @param statisticTime 统计时间
	 */
	public StatisticsTask(StatisticsScriptVO scriptVO, long statisticTime) {
		this.scriptVO = scriptVO;
		this.statisticTime = statisticTime;
	}

	@Override
	public void run() {
		Common.ctx.getRuntimeManager().startScriptStatistics(scriptVO,statisticTime);
	}
	
	public StatisticsScriptVO getScriptVO() {
		return scriptVO;
	}

	public void setScriptVO(StatisticsScriptVO scriptVO) {
		this.scriptVO = scriptVO;
	}

	public long getStatisticTime() {
		return statisticTime;
	}

	public void setStatisticTime(long statisticTime) {
		this.statisticTime = statisticTime;
	}
	
}
