/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event.detectors;

import com.serotonin.mango.Common;
import com.serotonin.mango.rt.dataImage.PointValueTime;

/**
 * This is a base class for all subclasses that need to schedule timeouts for them to become active.
 * 
 *  
 */
abstract public class TimeDelayedEventDetectorRT extends TimeoutDetectorRT {
    @Override
    synchronized protected void scheduleJob(long fromTime) {
        if (getDurationMS() > 0)
            super.scheduleJob(fromTime + getDurationMS());
        else
            // Otherwise call the event active immediately.
            setEventActive(true);
    }

    @Override
    synchronized protected void unscheduleJob() {
        // Check whether there is a tolerance duration.
        if (getDurationMS() > 0)
            super.unscheduleJob();

        // Reset the eventActive if it is on
        if (isEventActive())
            setEventActive(false);
    }

    abstract void setEventActive(boolean b);

    @Override
    public void initialize() {
        super.initialize();
        initializeState();
    }

    protected void initializeState() {
        int pointId = vo.njbGetDataPoint().getId();
        PointValueTime latest = Common.ctx.getRuntimeManager().getDataPoint(pointId).getPointValue();

        if (latest != null)
            pointChanged(null, latest);
    }

    @Override
    public void scheduleTimeoutImpl(long fireTime) {
        setEventActive(true);
    }
}
