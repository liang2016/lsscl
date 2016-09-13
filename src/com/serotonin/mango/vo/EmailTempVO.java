package com.serotonin.mango.vo;

public class EmailTempVO {
	
	/**
	 * 编号
	 */
	private int id;
	
	/**
	 * 执行测试邮件的用户ID
	 */
	private int uid;
	
	/**
	 * 测试对象邮件地址
	 */
	private String emailAddress;
	
//	/**
//	 * 用户此时所在范围
//	 */
//	private int currentScopeid;
//	
//	public int getCurrentScopeid() {
//		return currentScopeid;
//	}
//
//	public void setCurrentScopeid(int currentScopeid) {
//		this.currentScopeid = currentScopeid;
//	}

	private long ts;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public EmailTempVO() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public EmailTempVO(int id, int uid, String emailAddress, long ts) {
		this.id = id;
		this.uid = uid;
		this.emailAddress = emailAddress;
		this.ts = ts;
	}
	
}
