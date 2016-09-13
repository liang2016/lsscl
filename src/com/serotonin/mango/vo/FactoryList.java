package com.serotonin.mango.vo;

/**
 * 工厂列表
 * 
 * @author Administrator
 * 
 */
public class FactoryList {
	/**
	 * 工厂编号
	 */
	private int id;
	/**
	 * 工厂名称
	 */
	private String name;
	/**
	 * 所属区域
	 */
	private String zone;
	/**
	 * 所属子区域
	 */
	private String subZone;
	/**
	 * 工厂备注
	 */
	private String comment;
	/**
	 * 工厂事件总数
	 */
	private int events;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getSubZone() {
		return subZone;
	}

	public void setSubZone(String subZone) {
		this.subZone = subZone;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getEvents() {
		return events;
	}

	public void setEvents(int events) {
		this.events = events;
	}

	public FactoryList(int id, String name, String zone, String subZone,
			String comment, int events) {
		super();
		this.id = id;
		this.name = name;
		this.zone = zone;
		this.subZone = subZone;
		this.comment = comment;
		this.events = events;
	}

	public FactoryList() {
		super();
	}

}
