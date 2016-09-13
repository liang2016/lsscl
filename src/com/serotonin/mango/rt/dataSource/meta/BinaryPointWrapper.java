/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.meta;

import com.serotonin.mango.rt.dataImage.IDataPoint;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.util.DateUtils;

/**
 *  
 */
class BinaryPointWrapper extends DistinctPointWrapper {
    public BinaryPointWrapper(IDataPoint point, WrapperContext context) {
        super(point, context);
    }

    public boolean getValue() {
        MangoValue value = getValueImpl();
        if (value == null)
            return false;
        return value.getBooleanValue();
    }

    @Override
    public String toString() {
        return "{value=" + getValue() + ", ago(periodType, count), past(periodType, count), prev(periodType, count), "
                + "previous(periodType, count)}";
    }

    public boolean ago(int periodType) {
        return ago(periodType, 1);
    }

    public boolean ago(int periodType, int count) {
        long from = DateUtils.minus(context.getRuntime(), periodType, count);
        PointValueTime pvt = point.getPointValueBefore(from);
        if (pvt == null)
            return false;
        return pvt.getBooleanValue();
    }
}
