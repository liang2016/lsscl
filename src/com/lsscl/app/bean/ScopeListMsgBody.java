package com.lsscl.app.bean;

import java.util.List;

/**
 * 区域列表消息实体
 * 
 * @author yxx
 * 
 */
public class ScopeListMsgBody extends MsgBody {
	private static final long serialVersionUID = -3026327625828424976L;
	private List<Scope> scopes;

	public List<Scope> getScopes() {
		return scopes;
	}

	public void setScopes(List<Scope> scopes) {
		this.scopes = scopes;
	}

	@Override
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"MSGBODY\":[");
		int size = scopes.size();
		for(int i=0;i<size;i++){
			Scope scope = scopes.get(i);
			String ext = (i<size-1)?",":"";
			sb.append(scope.toJSON()+ext);
		}
		sb.append("]");
		return sb.toString();
	}
}
