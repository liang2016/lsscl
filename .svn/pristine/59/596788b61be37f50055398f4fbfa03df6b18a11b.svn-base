package com.lsscl.app.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * 消息体
 * 
 * @author yxx
 * 
 */
public class MsgBody implements Serializable {
	private static final long serialVersionUID = 5353253151999977454L;
	protected String msg;
	private Map<String, AcpAlarmInfo> alarms;

	public Map<String, AcpAlarmInfo> getAlarms() {
		return alarms;
	}

	public void setAlarms(Map<String, AcpAlarmInfo> alarms) {
		this.alarms = alarms;
	}

	public MsgBody() {
	}

	public MsgBody(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 转为json字符串
	 * @return
	 */
	public String toJSON() {
		if(this.alarms==null||this.alarms.size()==0)return null;
		StringBuilder json = new StringBuilder();
		json.append("\"MSGBODY\":{");
		int size = this.alarms.keySet().size();
		int i=0;
		for(String key:this.alarms.keySet()){
			String ext = (i<size-1)?",":"";
			AcpAlarmInfo alarmInfo = this.alarms.get(key);
			json.append(alarmInfo.toJSON()+ext);
			i++;
		}
		json.append("}");
		return json.toString();
	}

	
}
