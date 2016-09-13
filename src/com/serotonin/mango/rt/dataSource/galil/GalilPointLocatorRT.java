/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.galil;

import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataSource.PointLocatorRT;
import com.serotonin.web.i18n.LocalizableException;

/**
 *  
 */
public class GalilPointLocatorRT extends PointLocatorRT {
    private final PointTypeRT pointType;

    public GalilPointLocatorRT(PointTypeRT pointType) {
        this.pointType = pointType;
    }

    public PointTypeRT getPointType() {
        return pointType;
    }

    @Override
    public boolean isSettable() {
        return pointType.isSettable();
    }

    public GalilRequest getPollRequest() {
        return pointType.getPollRequest();
    }

    public MangoValue parsePollResponse(String data, String pointName) throws LocalizableException {
        return pointType.parsePollResponse(data, pointName);
    }

    public GalilRequest getSetRequest(MangoValue value) {
        return pointType.getSetRequest(value);
    }

    public MangoValue parseSetResponse(String data) throws LocalizableException {
        return pointType.parseSetResponse(data);
    }
}
