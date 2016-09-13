package com.serotonin.mango.vo.statistics;

public class SumInSameTimeVO {
	
	private long ts;
	
	private double value;
	
	private int count;

	public SumInSameTimeVO() {
	}

	public SumInSameTimeVO(long ts, double value, int count) {
		this.ts = ts;
		this.value = value;
		this.count = count;
	}
	
	public SumInSameTimeVO(long ts, double value) {
		super();
		this.ts = ts;
		this.value = value;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	

}
