package com.serotonin.mango.vo.statistics;

import com.serotonin.mango.vo.acp.ACPAttrVO;

/**
 * 表示 统计参数对应机器型号的数据点
 * 
 * @author Administrator
 * 
 */
public class ACPAttrStatisticsVO {
	/**
	 * 统计参数
	 */
	private StatisticsVO statisticsVO;
	/**
	 * 机器型号点
	 */
	private ACPAttrVO attrVO;

	public StatisticsVO getStatisticsVO() {
		return statisticsVO;
	}

	public void setStatisticsVO(StatisticsVO statisticsVO) {
		this.statisticsVO = statisticsVO;
	}

	public ACPAttrVO getAttrVO() {
		return attrVO;
	}

	public void setAttrVO(ACPAttrVO attrVO) {
		this.attrVO = attrVO;
	}

	public ACPAttrStatisticsVO(StatisticsVO statisticsVO, ACPAttrVO attrVO) {
		super();
		this.statisticsVO = statisticsVO;
		this.attrVO = attrVO;
	}

	public ACPAttrStatisticsVO() {
		super();
	}
}
