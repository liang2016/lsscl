package com.lsscl.app.bean;

import java.util.Map;

/**
 * 空压机属性名称
 * 
 * @author yxx
 * 
 */
public class AttrNamesMsgBody extends MsgBody {

	/**
	 * 
	 */
	private static final long serialVersionUID = 437110727734483264L;
	private Map<String, String> attrs;

	public Map<String, String> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, String> attrs) {
		this.attrs = attrs;
	}

	@Override
	public String toJSON() {
		StringBuilder json = new StringBuilder();
		json.append("\"MSGBODY\":{");
		int i=0;
		for (String key : attrs.keySet()) {
			String ext = (i<attrs.keySet().size()-1)?"\",":"\"";
			json.append("\""+key+"\":\""+attrs.get(key)+ext);
			i++;
		}
		json.append("}");
		return json.toString();
	}
	
	@Override
	public String toString() {
		return "AttrNamesMsgBody [attrs=" + attrs + "]";
	}

}
