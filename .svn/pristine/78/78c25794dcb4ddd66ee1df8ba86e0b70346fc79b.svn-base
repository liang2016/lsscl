package com.lsscl.app.bean;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * 空压机列表 消息体响应
 * 
 * @author yxx
 * 
 */
public class CompressorListMsgBody extends MsgBody implements Serializable {
	private static final long serialVersionUID = -6742754382193855965L;
	private List<CompressorInfo> compressorInfos;

	public List<CompressorInfo> getCompressorInfos() {
		return compressorInfos;
	}

	public void setCompressorInfos(List<CompressorInfo> compressorInfos) {
		this.compressorInfos = compressorInfos;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(CompressorInfo info:compressorInfos){
			sb.append(info+"\n");
		}
		return sb.toString();
	}

	@Override
	public String toJSON() {
		StringBuilder json = new StringBuilder();
		json.append("\"MSGBODY\":{");
		json.append("\"COMPRESSORINFOS\":[");
		for (int i=0;i<this.compressorInfos.size();i++) {
			CompressorInfo info = this.compressorInfos.get(i);
			json.append(info.toJSON());
			if(i<this.compressorInfos.size()-1)json.append(",");
		}
		json.append("]");
		json.append("}");
		return json.toString();
	}
}
