/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event.detectors;

import com.serotonin.mango.Common;
import com.serotonin.mango.rt.dataImage.PointValueTime;

/**
 *  
 */
abstract public class DifferenceDetectorRT extends TimeDelayedEventDetectorRT {
    /**
     * State field. Whether the event is currently active or not. This field is used to prevent multiple events being
     * raised during the duration of a single state detection.
     */
    protected boolean eventActive;

    protected long lastChange;

    public boolean isEventActive() {
        return eventActive;
    }

    synchronized protected void pointData() {
        lastChange = System.currentTimeMillis();
        if (!eventActive)
            unscheduleJob();
        else
            setEventActive(false);
        scheduleJob(lastChange);
    }

    @Override
    public void initializeState() {
        // Get historical data for the point out of the database.
        int pointId = vo.njbGetDataPoint().getId();
        PointValueTime latest = Common.ctx.getRuntimeManager().getDataPoint(pointId).getPointValue();
        if (latest != null)
            lastChange = latest.getTime();
        else
            // The point may be new or not logged, so don't go active immediately.
            lastChange = System.currentTimeMillis();

        if (lastChange + getDurationMS() < System.currentTimeMillis())
            // Nothing has happened in the time frame, so set the event active.
            setEventActive(true);
        else
            // Otherwise, set the timeout.
            scheduleJob(lastChange);
    }

    @Override
    synchronized public void setEventActive(boolean b) {
        eventActive = b;
        if (eventActive)
            // Raise the event.
            raiseEvent(lastChange + getDurationMS(), createEventContext());
        else
            // Deactivate the event.
            returnToNormal(lastChange);
    }
}
