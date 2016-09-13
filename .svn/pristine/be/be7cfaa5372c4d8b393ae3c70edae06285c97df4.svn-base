/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event.detectors;

import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.view.text.TextRenderer;
import com.serotonin.mango.vo.event.PointEventDetectorVO;
import com.serotonin.web.i18n.LocalizableMessage;

public class PointChangeDetectorRT extends PointEventDetectorRT {
    private MangoValue oldValue;
    private MangoValue newValue;

    public PointChangeDetectorRT(PointEventDetectorVO vo) {
        this.vo = vo;
    }

    @Override
    protected LocalizableMessage getMessage() {
        return new LocalizableMessage("event.detector.changeCount", vo.njbGetDataPoint().getName(),
                formatValue(oldValue), formatValue(newValue));
    }

    private String formatValue(MangoValue value) {
        return vo.njbGetDataPoint().getTextRenderer().getText(value, TextRenderer.HINT_SPECIFIC);
    }

    @Override
    public void pointChanged(PointValueTime oldValue, PointValueTime newValue) {
    	this.oldValue = PointValueTime.getValue(oldValue);
        this.newValue = newValue.getValue();
        if(newValue.getValue().getIntegerValue()!=0)
        	raiseEvent(newValue.getTime(), createEventContext());
        else
        	returnToNormal(newValue.getTime());
    }

    public boolean isEventActive() {
        return false;
    }
}
