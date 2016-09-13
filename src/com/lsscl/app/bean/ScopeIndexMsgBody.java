package com.lsscl.app.bean;

import java.util.HashMap;
import java.util.Map;


public class ScopeIndexMsgBody extends MsgBody {
	private static final long serialVersionUID = -2288098820924557661L;
	private Map<String, String> map = new HashMap<String, String>();

	public void put(String key, String value) {
		map.put(key, value);
	}

	@Override
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"MSGBODY\":{");
		for(String key:map.keySet()){
			sb.append("\""+key+"\":\""+map.get(key)+"\",");
		}
		if(map.size()>0)sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("}");
		return sb.toString();
	}
}
