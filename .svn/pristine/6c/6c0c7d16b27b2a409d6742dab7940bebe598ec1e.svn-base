package com.lsscl.app.bean;

import java.io.Serializable;

/**
 * 首页响应消息体
 * 
 * @author yxx
 * 
 */
public class IndexMsgBody extends MsgBody implements Serializable {
	private static final long serialVersionUID = -1051040492309780457L;

	private String username;
	private String factoryName;
	private String time;
	private String power;// 功率
	private Integer total;// 总数
	private Integer open;//开启
	private Integer close;//关闭
	private Integer alarm;//报警
	private String lastAlarmTime;//上次报警时间

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getOpen() {
		return open;
	}

	public void setOpen(Integer open) {
		this.open = open;
	}

	public Integer getClose() {
		return close;
	}

	public void setClose(Integer close) {
		this.close = close;
	}

	public Integer getAlarm() {
		return alarm;
	}

	public void setAlarm(Integer alarm) {
		this.alarm = alarm;
	}

	public String getLastAlarmTime() {
		return lastAlarmTime;
	}

	public void setLastAlarmTime(String lastAlarmTime) {
		this.lastAlarmTime = lastAlarmTime;
	}
	
	@Override
	public String toJSON() {
		StringBuilder json = new StringBuilder();
		json.append("\"MSGBODY\":{");
		json.append("\"USERNAME\":\""+this.username+"\",");
		json.append("\"FACTORYNAME\":\""+this.factoryName+"\",");
		json.append("\"TIME\":\""+this.time+"\",");
		json.append("\"POWER\":\""+this.power+"\",");
		json.append("\"TOTAL\":"+this.total+",");
		json.append("\"OPEN\":"+this.open+",");
		json.append("\"CLOSE\":"+this.close+",");
		json.append("\"ALARM\":"+this.alarm+",");
		json.append("\"LASTALARMTIME\":\""+this.lastAlarmTime+"\"");
		json.append("}");
		return json.toString();
	}

	@Override
	public String toString() {
		return "IndexMsgBody [username=" + username + ", factoryName="
				+ factoryName + ", time=" + time + ", power=" + power
				+ ", total=" + total + ", open=" + open + ", close=" + close
				+ ", alarm=" + alarm + ", lastAlarmTime=" + lastAlarmTime + "]";
	}
	
}
