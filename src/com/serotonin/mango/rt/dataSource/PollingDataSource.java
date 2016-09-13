/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.mango.Common;
import com.serotonin.mango.rt.dataImage.DataPointRT;
import com.serotonin.mango.util.timeout.TimeoutClient;
import com.serotonin.mango.util.timeout.TimeoutTask;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.timer.FixedRateTrigger;
import com.serotonin.timer.TimerTask;
import com.serotonin.web.taglib.DateFunctions;

abstract public class PollingDataSource extends DataSourceRT implements
		TimeoutClient {
	private final Log LOG = LogFactory.getLog(PollingDataSource.class);

	private final DataSourceVO<?> vo;
	protected List<DataPointRT> dataPoints = new ArrayList<DataPointRT>();
	protected boolean pointListChanged = false;
	private long pollingPeriodMillis = 300000; // Default to 5 minutes just to
												// have something here默认为5分钟
	private boolean quantize;// 量化
	private TimerTask timerTask;
//	private TimerTask timerTask2;
	private volatile Thread jobThread;
//	private volatile Thread resumeJobThread;
	private long jobThreadStartTime;
	private long logJobThreadStartTime;
	private boolean forceSourceRead = false;
	private long endTime;

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public boolean isForceSourceRead() {
		return forceSourceRead;
	}

	public void setForceSourceRead(boolean forceSourceRead) {
		this.forceSourceRead = forceSourceRead;
	}

	public PollingDataSource(DataSourceVO<?> vo) {
		super(vo);
		this.vo = vo;
	}

	public void setPollingPeriod(int periodType, int periods, boolean quantize) {
		pollingPeriodMillis = Common.getMillis(periodType, periods);
		this.quantize = quantize;
	}

	private void checkforce() {
		if (forceSourceRead)
			if (jobThreadStartTime > endTime) {
				setForceSourceRead(false);
				jobThreadStartTime = -1;
			}
	}

//	public void resumeScheduleTimeout(long fireTime) {
//		checkforce();
//		if (timerTask2 == null || timerTask2.isCancelled())
//			return;
//		if (resumeJobThread != null) {
//			// There is another poll still running, so abort this one.
//			LOG.warn(vo.getName()
//					+ ": poll at "
//					+ DateFunctions
//							.getFullSecondTime(jobThreadStartTime * 1000)
//					+ " aborted because a previous poll started at "
//					+ DateFunctions.getFullSecondTime(logJobThreadStartTime)
//					+ " is still running");
//			return;
//		}
//		// LOG.warn("resumeScheduleTimeout------------------"+Thread.currentThread()+",firetime:"+fireTime);
//		try {
//			resumeJobThread = Thread.currentThread();
//			logJobThreadStartTime = fireTime;
//			// Check if there were changes to the data points list.
//			synchronized (pointListChangeLock) {
//				updateChangedPoints();
//				doPoll(fireTime);
//			}
//		} finally {
//			resumeJobThread = null;
//		}
//	}

//	public void canlceTimerTask2() {
//		if (resumeJobThread != null) {
//			try {
//				resumeJobThread.join(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			resumeJobThread = null;
//		}
//		if (timerTask2 != null)
//			timerTask2.cancel();
//	}

	public void canlceTimerTask() {
		if (jobThread != null) {
			try {
				jobThread.join(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jobThread = null;
		}
		if (timerTask != null) {
			timerTask.cancel();
		}
	}

	public void scheduleTimeout(long fireTime) {
		checkforce();
		if (timerTask == null || timerTask.isCancelled())
			return;
		if (jobThread != null) {
			// There is another poll still running, so abort this one.
			// LOG.warn(vo.getName() + ": poll at " +
			// DateFunctions.getFullSecondTime(jobThreadStartTime*1000)
			// + " aborted because a previous poll started at "
			// + DateFunctions.getFullSecondTime(logJobThreadStartTime) +
			// " is still running");
			return;
		}
		// LOG.warn("scheduleTimeout------------------"+Thread.currentThread()+",firetime:"+fireTime);
		try {
			jobThread = Thread.currentThread();
			logJobThreadStartTime = fireTime;
			// Check if there were changes to the data points list.
			synchronized (pointListChangeLock) {
				updateChangedPoints();
				doPoll(fireTime);
			}
		} finally {
			jobThread = null;
		}
	}

	protected void setNextPoll() {
		// if(jobThread!=null)
		// jobThread = null;
		this.canlceTimerTask();
//		this.canlceTimerTask2();

//		timerTask = new TimeoutTask(0, this);
		timerTask = new TimeoutTask(new FixedRateTrigger(0,
				pollingPeriodMillis), this);
//		beginPolling();
//		super.beginPolling();
	}

	// 是否为初始化
	protected boolean checkStart() {
		if (jobThreadStartTime < 1) {
			return true;
		}
		return false;
	}

	// nextjob的时间
	protected long setNextTime(long interval) {
		return jobThreadStartTime += interval;
	}

	protected long setJobThreadStartTime(long pollTime) {
		return jobThreadStartTime = pollTime;
	}

	abstract protected void doPoll(long time);

	protected void updateChangedPoints() {
		synchronized (pointListChangeLock) {
			if (addedChangedPoints.size() > 0) {
				// Remove any existing instances of the points.
				dataPoints.removeAll(addedChangedPoints);
				dataPoints.addAll(addedChangedPoints);
				addedChangedPoints.clear();
				pointListChanged = true;
			}
			if (removedPoints.size() > 0) {
				dataPoints.removeAll(removedPoints);
				removedPoints.clear();
				pointListChanged = true;
			}
		}
	}

	//
	//
	// Data source interface
	//
	@Override
	public void beginPolling() {
//		if (timerTask != null) {
//			if (!timerTask.isCancelled())
//			{
//				return;
//			}
//
//		}
		this.canlceTimerTask();
		
		// Quantize the start.
		long delay = 0;
		if (quantize)
			delay = pollingPeriodMillis
					- (System.currentTimeMillis() % pollingPeriodMillis);
		timerTask = new TimeoutTask(new FixedRateTrigger(delay,
				pollingPeriodMillis), this);
//		timerTask = new TimeoutTask(new FixedRateTrigger(0,
//				pollingPeriodMillis), this);
		super.beginPolling();
	}

	@Override
	public void terminate() {
		if (timerTask != null)
			timerTask.cancel();
//		if (timerTask2 != null)
//			timerTask2.cancel();
		super.terminate();
	}

	@Override
	public void joinTermination() {
		super.joinTermination();

		Thread localThread = jobThread;
//		Thread localThread2 = resumeJobThread;
		if (localThread != null) {
			try {
				localThread.join(30000); // 30 seconds
			} catch (InterruptedException e) { /* no op */
			}
			if (jobThread != null) {
				throw new ShouldNeverHappenException(
						"Timeout waiting for data source to stop: id="
								+ getId() + ", type=" + getClass()
								+ ", stackTrace="
								+ Arrays.toString(localThread.getStackTrace()));
			}
		}
//		if (localThread2 != null) {
//			try {
//				localThread2.join(30000); // 30 seconds
//			} catch (InterruptedException e) { /* no op */
//			}
//			if (resumeJobThread != null) {
//				throw new ShouldNeverHappenException(
//						"Timeout waiting for data source to stop: id="
//								+ getId() + ", type=" + getClass()
//								+ ", stackTrace="
//								+ Arrays.toString(localThread2.getStackTrace()));
//			}
//		}
	}
}
