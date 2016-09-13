package com.serotonin.mango.vo;

import java.util.Map;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.json.JsonSerializable;

/**
 * 工厂实体
 * 
 * @author Administrator
 * 
 */
public class Factory implements JsonSerializable {
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
	 * 经度
	 */
	@JsonRemoteProperty
	private float lon;
	/**
	 * 纬度
	 */
	@JsonRemoteProperty
	private float lat;
	/**
	 * 所属区域
	 */
	@JsonRemoteProperty
	private int zoneId;
	/**
	 * 子区域
	 */
	@JsonRemoteProperty
	private int SZId;

	public Factory() {
		super();
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

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public int getSZId() {
		return SZId;
	}

	public void setSZId(int id) {
		SZId = id;
	}

	public Factory(int id, String name, String comment, float lon, float lat,
			int zoneId, int id2) {
		super();
		this.id = id;
		this.name = name;
		this.comment = comment;
		this.lon = lon;
		this.lat = lat;
		this.zoneId = zoneId;
		SZId = id2;
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
