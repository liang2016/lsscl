package com.lsscl.app.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScopeTreeMsgBody extends MsgBody {
	private static final long serialVersionUID = 8359285861136445764L;
	private List<Map<String, String>> scopes = new ArrayList<Map<String, String>>();

	public List<Map<String, String>> getScopes() {
		return scopes;
	}

	public void setScopes(List<Map<String, String>> scopes) {
		this.scopes = scopes;
	}

	@Override
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"MSGBODY\":[");
		for(Map<String,String> m:scopes){
			sb.append("{");
			for(String key:m.keySet()){
				sb.append("\""+key+"\":"+m.get(key)+",");
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
			sb.append("},");
		}
		if(scopes.size()>0)sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("]");
		return sb.toString();
	}
}
