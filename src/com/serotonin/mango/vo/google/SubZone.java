package com.serotonin.mango.vo.google;

import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.json.JsonSerializable;

/**
 * 子区域
 * 
 * @author 刘建坤
 * 
 */
public class SubZone extends BaseZone implements JsonSerializable {
	/**
	 * 所属区域编号
	 */
	@JsonRemoteProperty
	private int zId;

	public int getZId() {
		return zId;
	}

	public void setZId(int id) {
		zId = id;
	}

	public SubZone() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SubZone(int id, String name, String comment, float range, float lon,
			float lat) {
		super(id, name, comment, range, lon, lat);
		// TODO Auto-generated constructor stub
	}

}
