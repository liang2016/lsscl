package com.serotonin.mango.web.mvc.form;

public class ScopeForm {
	/**
	 * 编号
	 */
	private Integer id;
	/**
	 * 名称
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
	 * 描述
	 */
	private String description;
	/**
	 * 上级编号
	 */
	private Integer parentId;
	/**
	 * 行业类型
	 */
	private Integer tradeId;
	/**
	 * 范围类型
	 */
	private Integer scopetype;

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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getTradeId() {
		return tradeId;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	public Integer getScopetype() {
		return scopetype;
	}

	public void setScopetype(Integer scopetype) {
		this.scopetype = scopetype;
	}
}
