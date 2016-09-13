package com.lsscl.app.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * 报警推送实体
 * @author Administrator
 *
 */
public class PushEvents {
	private String deviceToken;// 令牌(IOS)
	private String flag;//推送类型 声音、振动、指定时间内有声
	private Set<String> scopenames = new HashSet<String>();// 报警工厂名称
	private int deviceType;//设备类型0：IOS   1：Andorid
	private String userId;//Android百度云推送userid
	private String channelId;

	public PushEvents(Set<String> set, QC loginQc) {
		String deviceType = loginQc.getMsgBody().get("DEVICETYPE");
		String imsi = loginQc.getImsi();
		String imei = loginQc.getImei();
		String msgFlag = loginQc.getMsgBody().get("MSGTYPE");
		if(deviceType!=null&&!"".equals(deviceType)){//Android
			this.userId = imsi;
			this.channelId = imei;
			this.flag = msgFlag;
			this.deviceType = Integer.parseInt(deviceType);
		}else{//IOS
			this.deviceToken = imsi;
			this.flag = imei;
			this.scopenames = set;
		}
		this.scopenames = set;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public Set<String> getScopenames() {
		return scopenames;
	}

	public void setScopenames(Set<String> scopenames) {
		this.scopenames = scopenames;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String message(){
		StringBuilder sb = new StringBuilder();
		if(scopenames.size()==0){
			sb.append("您有一条报警");
			return sb.toString();
		}else{
			sb.append("您有来自 ");
			for(String name:scopenames){
				sb.append(name+"，");
			}
			sb.deleteCharAt(sb.lastIndexOf("，"));
			sb.append(" 的报警");
		}
		return sb.toString();
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
}
