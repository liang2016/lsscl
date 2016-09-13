/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event.detectors;

import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.vo.event.PointEventDetectorVO;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
public class NoChangeDetectorRT extends DifferenceDetectorRT {
    public NoChangeDetectorRT(PointEventDetectorVO vo) {
        this.vo = vo;
    }

    @Override
    public void pointChanged(PointValueTime oldValue, PointValueTime newValue) {
        pointData();
    }

    @Override
    public LocalizableMessage getMessage() {
        return new LocalizableMessage("event.detector.noChange", vo.njbGetDataPoint().getName(),
                getDurationDescription());
    }
}
