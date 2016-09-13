package com.serotonin.mango.vo;

import java.util.Map;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.json.JsonSerializable;

/**
 * 历史点设备数据
 * 
 * @author 刘建坤
 * 
 */
public class HistoryPointInfo implements JsonSerializable {
	/**
	 * 点设备编号
	 */
	@JsonRemoteProperty
	private int Id;
	/**
	 * 时间
	 */
	@JsonRemoteProperty
	private long time;
	/**
	 * 值
	 */
	@JsonRemoteProperty
	private double value;

	public int getId() {
		return Id;
	}

	public void setId(int pointId) {
		this.Id = pointId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public HistoryPointInfo() {
		super();
	}

	public HistoryPointInfo(int pointId, long time, double value) {
		super();
		this.Id = pointId;
		this.time = time;
		this.value = value;
	}

	@Override
	public void jsonDeserialize(JsonReader jsonReader, JsonObject jsonObject)
			throws JsonException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jsonSerialize(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		
	}
}
