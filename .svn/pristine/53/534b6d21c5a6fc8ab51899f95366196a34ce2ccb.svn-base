package com.serotonin.mango.vo.scope;

public class Zone {
	/**
	 * 规定scopetype字段:总部为0,区域类型编号为1,子区域类型编号为2,工厂类型编号为3
	 * 
	 * @author 王金阳
	 * 
	 */
	public interface ScopeTypes {
		int HQ = 0;
		int ZONE = 1;
		int SUBZONE = 2;
		int FACTORY = 3;
	}

	/**
	 * 范围编号
	 */
	private Integer id;
	/**
	 * 范围名称
	 */
	private String scopename;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 范围在地图上现实图标的经度
	 */
	private double lon;

	/**
	 * 范围在地图上现实图标的纬度
	 */
	private double lat;

	/**
	 * 放大倍数
	 */
	private int enlargenum;

	/**
	 * 范围描述
	 */
	private String description;

	/**
	 * 上级范围编号(总部-->区域-->子区域-->工厂)
	 * 
	 */
	private int parentScopeId;
	/**
	 * 范围类型(是区域，子区域，工厂)
	 */
	private Integer scopetype;

	/**
	 * 行业类型
	 */
	private int tradeid;

	public int getTradeid() {
		return tradeid;
	}

	public void setTradeid(int tradeid) {
		this.tradeid = tradeid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getScopename() {
		return scopename;
	}

	public void setScopename(String scopename) {
		this.scopename = scopename;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public int getEnlargenum() {
		return enlargenum;
	}

	public void setEnlargenum(int enlargenum) {
		this.enlargenum = enlargenum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getParentScopeId() {
		return parentScopeId;
	}

	public void setParentScopeId(int parentScopeId) {
		this.parentScopeId = parentScopeId;
	}

	public Integer getScopetype() {
		return scopetype;
	}

	public void setScopetype(Integer scopetype) {
		this.scopetype = scopetype;
	}

}
