package com.lsscl.app.bean;

import java.io.Serializable;

/**
 * 登录响应消息
 * @author yxx
 *
 */
public class LoginMsgBody extends MsgBody implements Serializable{
	private static final long serialVersionUID = -8722116898695545324L;
	private Integer userflag;//用户区分0=渠道用户；1=工厂用户;2=匿名登录用户
	private Integer scopeId;//区域id
	private Integer defaultScopeId;//默认工厂
	private String username;//登录用户名
	private String scopename;//区域名称
	
	public String getScopename() {
		return scopename;
	}
	public void setScopename(String scopename) {
		this.scopename = scopename;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getScopeId() {
		return scopeId;
	}
	public void setScopeId(Integer scopeId) {
		this.scopeId = scopeId;
	}
	public Integer getUserflag() {
		return userflag;
	}
	
	public Integer getDefaultScopeId() {
		return defaultScopeId;
	}
	public void setDefaultScopeId(Integer defaultScopeId) {
		this.defaultScopeId = defaultScopeId;
	}
	/**
	 * 用户区分0=渠道用户；1=工厂用户;2=匿名登录用户
	 * @param userflag
	 */
	public void setUserflag(Integer userflag) {
		this.userflag = userflag;
	}
	
	@Override
	public String toJSON() {
		StringBuilder json = new StringBuilder();
		json.append("\"MSGBODY\":{");
		json.append("\"USERFLAG\":"+this.userflag+",");
		json.append("\"SCOPEID\":"+this.scopeId+",");
		json.append("\"DEFAULTSCOPEID\":"+this.defaultScopeId+",");
		json.append("\"SNAME\":\""+this.scopename+"\",");
		json.append("\"USERNAME\":\""+this.username+"\"");
		json.append("}");
		return json.toString();
	}
}
