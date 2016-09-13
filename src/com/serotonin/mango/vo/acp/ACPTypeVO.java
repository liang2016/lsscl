package com.serotonin.mango.vo.acp;

/**
 * 空压机型号实体类
 * 
 * @author 王金阳
 * 
 */
public class ACPTypeVO {
	
	public static final int ACP_TYPE = 0;//0表示是空压机型号
	public static final int SYSTEM_TYPE = 1;//1表示是系统类型

	/**
	 * 空压机型号编号
	 */
	private Integer id;
	/**
	 * 空压机型号名称
	 */
	private String typename;
	/**
	 * 空压机型号描述
	 */
	private String description;
	/**
	 * 是机器型号，还是系统点集合
	 */
	private int type;
	/**
	 * 警告个数
	 */
	private String warnCount;
	/**
	 * 报警个数
	 */
	private String alarmCount;
	public String getWarnCount() {
		return warnCount;
	}

	public void setWarnCount(String warnCount) {
		if(type==SYSTEM_TYPE)
			this.warnCount="";
		this.warnCount = warnCount;
	}

	public String getAlarmCount() {
		return alarmCount;
	}

	public void setAlarmCount(String alarmCount) {
		if(type==SYSTEM_TYPE)
			this.alarmCount="";
		this.alarmCount = alarmCount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String tpyename) {
		this.typename = tpyename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ACPTypeVO(Integer id, String typename, String description, int type) {
		this.id = id;
		this.typename = typename;
		this.description = description;
		this.type = type;
	}

	public ACPTypeVO() {
	}

}
