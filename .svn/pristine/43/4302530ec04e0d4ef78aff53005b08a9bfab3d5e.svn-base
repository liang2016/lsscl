package com.lsscl.app.bean;

/**
 * 报警信息
 * 
 * @author yxx
 * 
 */
public class AlarmInfo {
	private String pid;
	private String cTime;
	private String eTime;
	private String content;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getcTime() {
		return cTime;
	}

	public void setcTime(String cTime) {
		this.cTime = cTime;
	}

	public String geteTime() {
		return eTime;
	}

	public void seteTime(String eTime) {
		this.eTime = eTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "AlarmInfo [pid=" + pid + ", cTime=" + cTime + ", eTime="
				+ eTime + ", content=" + content + "]\n";
	}

	public Object toJSON() {
		StringBuilder json = new StringBuilder();
		json.append("\"T"+pid+"\":{");
		json.append("\"CT\":"+cTime+",");
		json.append("\"ET\":"+eTime+"");
		json.append("}");
		return json.toString();
	}

}
