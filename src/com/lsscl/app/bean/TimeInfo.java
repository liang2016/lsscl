package com.lsscl.app.bean;

import java.io.Serializable;

/**
 * 时间信息
 * 
 * @author yxx
 * 
 */
public class TimeInfo implements Serializable {
	private static final long serialVersionUID = 1958814218638137710L;

	private String run;// 运行时间
	private String load;// 加载时间
	private String nextMaintenanceTime;// 下次保养时间（可空）


	public TimeInfo(String run, String load, String nextMaintenanceTime) {
		this.run = run;
		this.load = load;
		this.nextMaintenanceTime = nextMaintenanceTime;
	}

	
	public TimeInfo() {
	}


	public String getRun() {
		return run;
	}

	public void setRun(String run) {
		this.run = run;
	}

	public String getLoad() {
		return load;
	}

	public void setLoad(String load) {
		this.load = load;
	}

	public String getNextMaintenanceTime() {
		return nextMaintenanceTime;
	}

	public void setNextMaintenanceTime(String nextMaintenanceTime) {
		this.nextMaintenanceTime = nextMaintenanceTime;
	}


	@Override
	public String toString() {
		return "TimeInfo [run=" + run + ", load=" + load
				+ ", nextMaintenanceTime=" + nextMaintenanceTime + "]";
	}

}
