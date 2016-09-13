/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event.detectors;

import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.view.text.TextRenderer;
import com.serotonin.mango.vo.event.PointEventDetectorVO;
import com.serotonin.web.i18n.LocalizableMessage;

public class MultistateStateDetectorRT extends StateDetectorRT {
    public MultistateStateDetectorRT(PointEventDetectorVO vo) {
        this.vo = vo;
    }

    @Override
    public LocalizableMessage getMessage() {
        String name = vo.njbGetDataPoint().getName();
        String prettyText = vo.njbGetDataPoint().getTextRenderer().getText(vo.getMultistateState(),
                TextRenderer.HINT_SPECIFIC);
        LocalizableMessage durationDescription = getDurationDescription();

        if (durationDescription == null)
            return new LocalizableMessage("event.detector.state", name, prettyText);
        return new LocalizableMessage("event.detector.periodState", name, prettyText, durationDescription);
    }

    @Override
    protected boolean stateDetected(PointValueTime newValue) {
        int newMultistate = newValue.getIntegerValue();
        return newMultistate == vo.getMultistateState();
    }
}
