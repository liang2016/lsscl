package com.serotonin.mango.vo.statistics;

/**
 * 统计进度信息类
 * @author 王金阳
 *
 */
public class StatisticsProgressVO {

	/**
	 * 脚本信息
	 */
	private StatisticsScriptVO scriptVO;
	
	/**
	 * 机器/系统
	 */
	private String unit;
	
	/**
	 * 最近一次统计时间
	 */
	private long statisticTime;
	
	/**
	 * (是否正在运行)正在运行/等待下一次运行
	 */
	private boolean running;
	
	
	public StatisticsScriptVO getScriptVO() {
		return scriptVO;
	}

	public void setScriptVO(StatisticsScriptVO scriptVO) {
		this.scriptVO = scriptVO;
	}

	public Object getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public long getStatisticTime() {
		return statisticTime;
	}

	public void setStatisticTime(long statisticTime) {
		this.statisticTime = statisticTime;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public StatisticsProgressVO() {
	}

	public StatisticsProgressVO(StatisticsScriptVO scriptVO, String unit,
			long statisticTime, boolean running) {
		this.scriptVO = scriptVO;
		this.unit = unit;
		this.statisticTime = statisticTime;
		this.running = running;
	}

}
