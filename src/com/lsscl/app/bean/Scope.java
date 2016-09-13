package com.lsscl.app.bean;

/**
 * 区域实体
 * 
 * @author yxx
 * 
 */
public class Scope {
	private int id;
	private int parentId;
	private String name;
	private int type;
	private int L1,L2,L3;//0、1、2、3级报警数量

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getL1() {
		return L1;
	}

	public void setL1(int l1) {
		L1 = l1;
	}

	public int getL2() {
		return L2;
	}

	public void setL2(int l2) {
		L2 = l2;
	}

	public int getL3() {
		return L3;
	}

	public void setL3(int l3) {
		L3 = l3;
	}

	/**
	 * 区域树形列表
	 * @return
	 */
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"ID\":"+id+",");
		sb.append("\"NAME\":\""+name+"\",");
		sb.append("\"TYPE\":"+type+",");
		sb.append("\"L1\":"+L1+",");
		sb.append("\"L2\":"+L2+",");
		sb.append("\"L3\":"+L3+"}");
		return sb.toString();
	}

	/**
	 * 区域关系表
	 * @return
	 */
	public Object toJSON2() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"ID\":"+id+",");
		sb.append("\"NAME\":\""+name+"\",");
		sb.append("\"TYPE\":"+type+",");
		sb.append("\"PID\":"+parentId+"}");
		return sb.toString();
	}
}
