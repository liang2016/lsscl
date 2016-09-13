package com.lsscl.app.bean;

import java.io.Serializable;
import java.util.List;

public class AlarmHistoryMsgBody extends MsgBody implements Serializable {

	private static final long serialVersionUID = -3814444052502195677L;

	private List<AcpAlarmInfo> alarmInfos;

	private int flag;//0=数据下载完成 ，1=数据未取完整
	
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public List<AcpAlarmInfo> getAlarmInfos() {
		return alarmInfos;
	}

	public void setAlarmInfos(List<AcpAlarmInfo> alarmInfos) {
		this.alarmInfos = alarmInfos;
	}

	@Override
	public String toJSON() {
		StringBuilder json = new StringBuilder();
		json.append("MSGBODY:{");
		int size = alarmInfos.size();
		for(int i=0;i<size;i++){
			AcpAlarmInfo info = alarmInfos.get(i);
			String ext = (i<size-1)?",":"";
			json.append(info.toJSON()+ext);
		}
		json.append("}");
		return json.toString();
	}
	
	@Override
	public String toString() {
		return "AlarmHistoryMsgBody [alarmInfos=" + alarmInfos + "]";
	}

}
