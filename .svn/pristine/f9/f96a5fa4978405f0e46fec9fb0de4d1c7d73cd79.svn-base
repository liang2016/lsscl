package com.serotonin.mango.vo.statistics;

/**
 * 指数排序
 * 
 * @author 王金阳
 * 
 */
public class IndexOrderVO implements Comparable<IndexOrderVO> {
	private int id;
	private String scopeName;
	private Double value;

	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public int compareTo(IndexOrderVO indexOrder) {
		int result = value.compareTo(indexOrder.value);
		return result == 0 ? scopeName
				.compareTo(((IndexOrderVO) indexOrder).scopeName) : result;
	}

	public IndexOrderVO() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public IndexOrderVO(int id, String scopeName, Double value) {
		super();
		this.id = id;
		this.scopeName = scopeName;
		this.value = value;
	}
}
