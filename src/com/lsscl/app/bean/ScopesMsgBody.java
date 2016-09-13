package com.lsscl.app.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有区域列表
 * @author yxx
 *
 */
public class ScopesMsgBody extends MsgBody{
	private static final long serialVersionUID = -3675249958298423540L;
	private String version;//版本不同时，更新区域列表
	private List<Scope>scopes = new ArrayList<Scope>();
	
	public List<Scope> getScopes() {
		return scopes;
	}

	public void setScopes(List<Scope> scopes) {
		this.scopes = scopes;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toJSON() {
		StringBuilder sb = new StringBuilder("\"MSGBODY\":{");
		sb.append("\"VERSION\":"+version+",");
		sb.append("\"SCOPES\":[");
		int size = scopes.size();
		for(int i=0;i<size;i++){
			Scope scope = scopes.get(i);
			String ext = (i==size-1)?"":",";
			sb.append(scope.toJSON2()+ext);
		}
		sb.append("]}");
		return sb.toString();
	}

}
