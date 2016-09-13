package com.serotonin.mango.vo;

import java.util.Map;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonSerializable;

public class Appacpinfo<T extends Appacpinfo<?>> implements JsonSerializable {

	public Appacpinfo() {
		super();
	}

	private int id;

	private int scopeId;

	private String name;
	
	private Float power;
    private String type;//空压机型号

	public Appacpinfo(int id, int scopeId, String name,String type) {
		this.id = id;
		this.scopeId = scopeId;
		this.name = name;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getScopeId() {
		return scopeId;
	}

	public void setScopeId(int scopeId) {
		this.scopeId = scopeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Float getPower() {
		return power;
	}

	public void setPower(Float power) {
		this.power = power;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
