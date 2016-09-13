package com.serotonin.mango.vo.acp;

import com.serotonin.mango.vo.DataPointVO;

/**
 * 空压机系统成员 实体类
 * @author 王金阳
 *
 */
public class ACPMembersVO {
	
	/**
	 * 空压机系统
	 */
	private ACPVO acpVO;
	
	/**
	 * 空压机属性
	 */
	private ACPAttrVO acpAttrVO;
	
	/**
	 * 统计参数
	 */
	private StatisticsParamVO statisticsParamVO;
	
	/**
	 * 数据点
	 */
	private DataPointVO dataPointVO;

	public ACPVO getAcpVO() {
		return acpVO;
	}

	public void setAcpVO(ACPVO acpVO) {
		this.acpVO = acpVO;
	}

	public ACPAttrVO getAcpAttrVO() {
		return acpAttrVO;
	}

	public void setAcpAttrVO(ACPAttrVO acpAttrVO) {
		this.acpAttrVO = acpAttrVO;
	}

	public StatisticsParamVO getStatisticsParamVO() {
		return statisticsParamVO;
	}

	public void setStatisticsParamVO(StatisticsParamVO statisticsParamVO) {
		this.statisticsParamVO = statisticsParamVO;
	}

	public DataPointVO getDataPointVO() {
		return dataPointVO;
	}

	public void setDataPointVO(DataPointVO dataPointVO) {
		this.dataPointVO = dataPointVO;
	}

	public ACPMembersVO(ACPVO acpVO, ACPAttrVO acpAttrVO,
			StatisticsParamVO statisticsParamVO, DataPointVO dataPointVO) {
		this.acpVO = acpVO;
		this.acpAttrVO = acpAttrVO;
		this.statisticsParamVO = statisticsParamVO;
		this.dataPointVO = dataPointVO;
	}
	
	public ACPMembersVO() {
		
	}

}
