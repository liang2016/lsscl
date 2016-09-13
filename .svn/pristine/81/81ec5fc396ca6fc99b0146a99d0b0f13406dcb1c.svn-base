package com.lsscl.app.bean;

import java.io.Serializable;

/**
 * 空压机信息
 * 
 * @author yxx
 * 
 */
public class CompressorInfo implements Serializable {
	private static final long serialVersionUID = -6568795303274881078L;
	private String compressorId;//空压机id
	private String compressorName;// 空压机名
	private String exhausTemperature;// 排气温度
	private String exhausPressure;// 排气压力
	private String alarmFlag="0";// 0=正常运行 1=停机 2=有警报
	private String runState="0";//运行状态 1=运行  0=停止

	public String getCompressorId() {
		return compressorId;
	}

	public void setCompressorId(String compressorId) {
		this.compressorId = compressorId;
	}

	public String getCompressorName() {
		return compressorName;
	}

	public void setCompressorName(String compressorName) {
		this.compressorName = compressorName;
	}


	public String getExhausTemperature() {
		return exhausTemperature;
	}

	public void setExhausTemperature(String exhausTemperature) {
		this.exhausTemperature = exhausTemperature;
	}

	public String getExhausPressure() {
		return exhausPressure;
	}

	public void setExhausPressure(String exhausPressure) {
		this.exhausPressure = exhausPressure;
	}

	public String getAlarmFlag() {
		return alarmFlag;
	}

	/**
	 * 0=正常运行 1=停机 2=有警报
	 * @param alarmFlag
	 */
	public void setAlarmFlag(String alarmFlag) {
		this.alarmFlag = alarmFlag;
	}

	
	public String getRunState() {
		return runState;
	}

	public void setRunState(String runState) {
		this.runState = runState;
	}

	@Override
	public String toString() {
		return "CompressorInfo [compressorName=" + compressorName
				+ ", exhausTemperature=" + exhausTemperature
				+ ", exhausPressure=" + exhausPressure + ", alarmFlag="
				+ alarmFlag + "]";
	}

	/**
	 * 转为json字符串
	 * @return
	 */
	public Object toJSON() {
		StringBuilder json = 
  new StringBuilder("{");
		json.append("\"COMPRESSORID\":"+this.compressorId+",");
		json.append("\"COMPRESSORNAME\":\""+this.compressorName+"\",");
		json.append("\"EXHAUSTEMPERATURE\":\""+this.exhausTemperature+"\",");
		json.append("\"EXHAUSPRESSURE\":\""+this.exhausPressure+"\",");
		json.append("\"ALARMFLAG\":"+this.alarmFlag+",");
		json.append("\"RUNSTATE\":"+this.runState+"");
		json.append("}");
		return json.toString();
	}
	
}
