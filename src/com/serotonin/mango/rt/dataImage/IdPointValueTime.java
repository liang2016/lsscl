/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataImage;

import com.serotonin.mango.rt.dataImage.types.MangoValue;

public class IdPointValueTime extends PointValueTime {
    private static final long serialVersionUID = 1L;

    private final int dataPointId;

    public IdPointValueTime(int dataPointId, MangoValue value, long time) {
        super(value, time);
        this.dataPointId = dataPointId;
    }

    public int getDataPointId() {
        return dataPointId;
    }
}
