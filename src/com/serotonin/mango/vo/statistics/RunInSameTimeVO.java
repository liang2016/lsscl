package com.serotonin.mango.vo.statistics;

import java.lang.Comparable;;

public class RunInSameTimeVO implements Comparable<RunInSameTimeVO>{
	
	private long ts;
	
	private int count;

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}


	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
	public RunInSameTimeVO() {
	}

	public RunInSameTimeVO(long ts, int count) {
		this.ts = ts;
		this.count = count;
	}

	public int compareTo(RunInSameTimeVO vo) {
		if(count>vo.getCount()){
			return 1;
		}else if(count==vo.getCount()){
			return 0;			
		}else{
			return -1;
		}
	}
	
}
