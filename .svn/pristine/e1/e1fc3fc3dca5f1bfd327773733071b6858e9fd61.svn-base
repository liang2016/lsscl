/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo;

import com.serotonin.web.taglib.DateFunctions;

public class UserComment {
	public static final int TYPE_EVENT = 1;
	public static final int TYPE_POINT = 2;
	public static final int TYPR_HEALTH = 3;
	// Configuration fields
	private int userId;
	private long ts;
	private String comment;

	// Relational fields
	private String username;

	public String getPrettyTime() {
		return DateFunctions.getTime(ts);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
