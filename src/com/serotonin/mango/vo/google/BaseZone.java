package com.serotonin.mango.vo.google;

import java.util.Map;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.json.JsonSerializable;

/**
 * 区域基础字段
 * 
 * @author Administrator
 * 
 */
public class BaseZone implements JsonSerializable {
	/**
	 * 编号
	 */
	@JsonRemoteProperty
	private int id;
	/**
	 * 名称
	 */
	@JsonRemoteProperty
	private String name;
	/**
	 * 注释
	 */
	@JsonRemoteProperty
	private String comment;
	/**
	 * google显示大小
	 */
	@JsonRemoteProperty
	private float range;
	/**
	 * 经度
	 */
	@JsonRemoteProperty
	private float lon;
	/**
	 * 纬度
	 */
	@JsonRemoteProperty
	private float lat;

	public BaseZone() {
		super();
	}

	public BaseZone(int id, String name, String comment, float range, float lon,
			float lat) {
		super();
		this.id = id;
		this.name = name;
		this.comment = comment;
		this.range = range;
		this.lon = lon;
		this.lat = lat;
	}

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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	@Override
	public void jsonDeserialize(JsonReader arg0, JsonObject arg1)
			throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public void jsonSerialize(Map<String, Object> arg0) {
		// TODO Auto-generated method stub

	}

}
