package com.lsscl.app.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域报警列表消息实体
 * @author yxx
 *
 */
public class ScopeEventsMsgBody extends MsgBody {
	private static final long serialVersionUID = 7380642423699420993L;
	private String level;//级别
	private String startIndex; //开始记录索引
	private String pageSize; //记录数
	private List<ScopeEvent>events = new ArrayList<ScopeEvent>();
	public List<ScopeEvent> getEvents() {
		return events;
	}
	public void setEvents(List<ScopeEvent> events) {
		this.events = events;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
	public String getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	@Override
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"MSGBODY\":{");
		sb.append("\"LEVEL\":"+level+",");
		sb.append("\"STARTINDEX\":"+startIndex+",");
		sb.append("\"PAGESIZE\":"+pageSize+",");
		sb.append("\"ALARMS\":[");
		int size = events.size();
		for(int i=0;i<size;i++){
			String ext = (i==size-1)?"":",";
			sb.append(events.get(i).toJSON()+ext);
		}
		sb.append("]}");
		return sb.toString();
	}
}
