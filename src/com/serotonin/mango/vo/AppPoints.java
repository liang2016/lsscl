package com.serotonin.mango.vo;

import java.util.Map;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.json.JsonSerializable;

public class AppPoints<T extends Appacpinfo<?>> implements JsonSerializable {

	@JsonRemoteProperty
	private int id;

	@JsonRemoteProperty
	private int pointId;

	@JsonRemoteProperty
	private String name;
	
	@JsonRemoteProperty
	private Integer aid;//空压机id
	
	@JsonRemoteProperty
	private DataPointVO dataPointVo;
	
	
	public AppPoints() {
		super();
	}

	public AppPoints(int id, int pointId, String name) {
		super();
		this.id = id;
		this.pointId = pointId;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPointId() {
		return pointId;
	}

	public void setPointId(int pointId) {
		this.pointId = pointId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataPointVO getDataPointVo() {
		return dataPointVo;
	}

	public void setDataPointVo(DataPointVO dataPointVo) {
		this.dataPointVo = dataPointVo;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	@Override
	public void jsonSerialize(Map<String, Object> map) {
		map.put("id", this.id);
		map.put("name", this.name);
	}

	@Override
	public void jsonDeserialize(JsonReader jsonreader, JsonObject jsonobject)
			throws JsonException {
	}

}
