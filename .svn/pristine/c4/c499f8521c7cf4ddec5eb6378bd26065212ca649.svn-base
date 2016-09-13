package com.serotonin.mango.vo.acp;

import com.serotonin.mango.vo.DataPointVO;

public class AcpMetaVo {
	/*
	 * 空压机类型id
	 */
	private Integer id;
	/**
	 * 空压机类型id
	 */
	private Integer acpTypeId;

	public Integer getAcpTypeId() {
		return acpTypeId;
	}

	public void setAcpTypeId(Integer acpTypeId) {
		this.acpTypeId = acpTypeId;
	}

	/**
	 * 空压机类型名称
	 */
	private String metaName;
	/**
	 * 空压机类型元数据 这里引用他的父类
	 */
	private DataPointVO dp;

	public AcpMetaVo() {
		super();
	}

	public AcpMetaVo(Integer id, String metaName, DataPointVO dp) {
		super();
		this.id = id;
		this.metaName = metaName;
		this.dp = dp;
	}
public AcpMetaVo(Integer id, Integer acpTypeId, String metaName, DataPointVO dp) {
	super();
	this.id = id;
	this.acpTypeId = acpTypeId;
	this.metaName = metaName;
	this.dp = dp;
}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMetaName() {
		return metaName;
	}

	public void setMetaName(String metaName) {
		this.metaName = metaName;
	}

	public DataPointVO getDp() {
		return dp;
	}

	public void setDp(DataPointVO dp) {
		this.dp = dp;
	}
}
