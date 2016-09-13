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
public class MultistatePointWrapper extends DistinctPointWrapper {
    public MultistatePointWrapper(IDataPoint point, WrapperContext context) {
        super(point, context);
    }

    public int getValue() {
        MangoValue value = getValueImpl();
        if (value == null)
            return 0;
        return value.getIntegerValue();
    }

    @Override
    public String toString() {
        return "{value=" + getValue() + ", ago(periodType, count), past(periodType, count), prev(periodType, count), "
                + "previous(periodType, count)}";
    }

    public int ago(int periodType) {
        return ago(periodType, 1);
    }

    public int ago(int periodType, int count) {
        long from = DateUtils.minus(context.getRuntime(), periodType, count);
        PointValueTime pvt = point.getPointValueBefore(from);
        if (pvt == null)
            return 0;
        return pvt.getIntegerValue();
    }
}
