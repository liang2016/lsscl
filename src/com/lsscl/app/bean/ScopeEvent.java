package com.lsscl.app.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.lsscl.app.util.StringUtil;
import com.serotonin.db.spring.GenericRowMapper;

/**
 * 区域报警事件
 * 
 * @author yxx
 * 
 */
public class ScopeEvent {
	private int id;
	private Long cTime;
	private String message;
	private int pid;// 点id
	private String aid;// 空压机id
	private String fName;// 工厂名称
	private String scopeId;// 区域id

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public Long getcTime() {
		return cTime;
	}

	public void setcTime(Long cTime) {
		this.cTime = cTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getScopeId() {
		return scopeId;
	}

	public void setScopeId(String scopeId) {
		this.scopeId = scopeId;
	}

	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"FNAME\":\"" + fName + "\",");
		sb.append("\"ID\":" + this.id + ",");
		sb.append("\"SID\":" + this.scopeId + ",");
		String msg = message != null ? message.replace("\\", "\\\\")
				.replace("\"", "\\\"").replace("\r", "\\r")
				.replace("\n", "\\n") : "";
		sb.append("\"MSG\":\"" + msg + "\",");
		sb.append("\"CT\":\"" + cTime + "\"}");
		return sb.toString();
	}
}