package com.serotonin.mango.vo.acp;


/**
 * 压缩空气系统 实体类
 * 
 * @author 王金阳
 * 
 */
public class ACPSystemVO {
	/**
	 * 系统编号
	 */
	private Integer id;
	/**
	 * 输出编号
	 */
	private String xid;
	/**
	 * 系统名称
	 */
	private String systemname;

	/**
	 * 所属工厂
	 */
	private Integer factoryId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getXid() {
		return xid;
	}

	public void setXid(String xid) {
		this.xid = xid;
	}

	public String getSystemname() {
		return systemname;
	}

	public void setSystemname(String systemname) {
		this.systemname = systemname;
	}

	public ACPSystemVO() {
	}

	public Integer getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	public ACPSystemVO(Integer id, String xid, String systemname,
			Integer factoryId) {
		super();
		this.id = id;
		this.xid = xid;
		this.systemname = systemname;
		this.factoryId = factoryId;
	}

}
