package com.lsscl.app.bean;

import java.io.Serializable;

import com.lsscl.app.util.StringUtil;

/**
 * 响应
 * 
 * @author yxx
 * 
 */
public class RSP implements Serializable {
	private static final long serialVersionUID = -8814547901126789491L;

	private String msgId;
	private int result;
	private String error="";
	private MsgBody msgBody;
	private String rspTime=StringUtil.getCurrentDate();
	

	public String getRspTime() {
		return rspTime;
	}

	public void setRspTime(String rspTime) {
		this.rspTime = rspTime;
	}

	public RSP(String msgId) {
		this.msgId = msgId;
	}

	public RSP() {
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public MsgBody getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(MsgBody msgBody) {
		this.msgBody = msgBody;
	}

	/**
	 * 将当前对象转为json字符串
	 * @return
	 */
	public String toJSON(){
		StringBuilder json = new StringBuilder();
		json.append("{\"RSP\":{");
		json.append("\"MSGID\":\""+this.msgId+"\",");
		json.append("\"RESULT\":"+this.result+",");
		json.append("\"RSPTIME\":\""+StringUtil.getCurrentDate()+"\",");
		json.append("\"ERROR\":\""+this.error+"\"");
		if(this.msgBody!=null&&this.msgBody.toJSON()!=null){
			json.append(","+this.msgBody.toJSON());
		}
		json.append("}}");
		return json.toString();
	}
	
	@Override
	public String toString() {
		return "RSP [msgId=" + msgId + ", result=" + result + ", error="
				+ error + ", msgBody=" + msgBody + "]";
	}

}
