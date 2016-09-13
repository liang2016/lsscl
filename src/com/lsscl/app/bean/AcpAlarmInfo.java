package com.lsscl.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 报警信息
 * 
 * @author yxx
 * 
 */
public class AcpAlarmInfo implements Serializable {
	private static final long serialVersionUID = 2361562100869629782L;

	private int aid;
	private List<AlarmInfo> infos;

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public List<AlarmInfo> getInfos() {
		return infos;
	}

	public void setInfos(List<AlarmInfo> infos) {
		this.infos = infos;
	}

	@Override
	public String toString() {
		return "AcpAlarmInfo [aid=" + aid + ", infos=" + infos + "]";
	}

	public String toJSON() {
		StringBuilder json = new StringBuilder();
		json.append("\"A"+aid+"\":{");
		int size = infos.size();
		for(int i=0;i<size;i++){
			AlarmInfo info = infos.get(i);
			String ext = (i<size-1)?",":"";
			json.append(info.toJSON()+ext);
		}
		json.append("}");
		return json.toString();
	}

}
