/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.publish;

import com.serotonin.mango.Common;
import com.serotonin.mango.rt.dataImage.DataPointListener;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.vo.publish.PublishedPointVO;

/**
 *  
 */
public class PublishedPointRT<T extends PublishedPointVO> implements DataPointListener {
    private final T vo;
    private final PublisherRT<T> parent;
    private boolean pointEnabled;

    public PublishedPointRT(T vo, PublisherRT<T> parent) {
        this.vo = vo;
        this.parent = parent;
        Common.ctx.getRuntimeManager().addDataPointListener(vo.getDataPointId(), this);
        pointEnabled = Common.ctx.getRuntimeManager().isDataPointRunning(vo.getDataPointId());
    }

    public void terminate() {
        Common.ctx.getRuntimeManager().removeDataPointListener(vo.getDataPointId(), this);
    }

    public void pointChanged(PointValueTime oldValue, PointValueTime newValue) {
        if (parent.getVo().isChangesOnly())
            parent.publish(vo, newValue);
    }

    public void pointSet(PointValueTime oldValue, PointValueTime newValue) {
        // no op. Everything gets handled in the other methods.
    }

    public void pointUpdated(PointValueTime newValue) {
        if (!parent.getVo().isChangesOnly())
            parent.publish(vo, newValue);
    }

    public void pointBackdated(PointValueTime value) {
        // no op
    }

    public boolean isPointEnabled() {
        return pointEnabled;
    }

    public void pointInitialized() {
        pointEnabled = true;
        parent.pointInitialized(this);
    }

    public void pointTerminated() {
        pointEnabled = false;
        parent.pointTerminated(this);
    }

    public T getVo() {
        return vo;
    }
}
