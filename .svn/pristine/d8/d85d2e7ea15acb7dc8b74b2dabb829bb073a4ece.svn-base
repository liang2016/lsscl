/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event.compound;

import java.util.List;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.mango.Common;
import com.serotonin.mango.rt.event.SimpleEventDetector;
import com.serotonin.web.i18n.LocalizableException;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
public class EventDetectorWrapper extends LogicalOperator {
    private final String detectorKey;
    private SimpleEventDetector source;

    public EventDetectorWrapper(String detectorKey) {
        this.detectorKey = detectorKey;
    }

    @Override
    public boolean evaluate() {
        if (source == null)
            throw new ShouldNeverHappenException("No runtime object available");
        return source.isEventActive();
    }

    @Override
    public String toString() {
        return detectorKey;
    }

    @Override
    public void initialize() throws LocalizableException {
        source = Common.ctx.getRuntimeManager().getSimpleEventDetector(detectorKey);
        if (source == null)
            throw new LocalizableException(new LocalizableMessage("compoundDetectors.initError.wrapper", detectorKey));
    }

    @Override
    public void initSource(CompoundEventDetectorRT parent) {
        source.addListener(parent);
    }

    @Override
    public void terminate(CompoundEventDetectorRT parent) {
        if (source != null)
            source.removeListener(parent);
    }

    @Override
    protected void appendDetectorKeys(List<String> keys) {
        keys.add(detectorKey);
    }
}
