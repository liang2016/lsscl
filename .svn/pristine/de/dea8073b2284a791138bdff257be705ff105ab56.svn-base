/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.meta;

import java.util.List;

import com.serotonin.mango.rt.dataImage.IDataPoint;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.util.DateUtils;
import com.serotonin.mango.view.stats.StartsAndRuntimeList;

/**
 *  
 */
abstract public class DistinctPointWrapper extends AbstractPointWrapper {
    public DistinctPointWrapper(IDataPoint point, WrapperContext context) {
        super(point, context);
    }

    public StartsAndRuntimeList past(int periodType) {
        return past(periodType, 1);
    }

    public StartsAndRuntimeList past(int periodType, int count) {
        long to = context.getRuntime();
        long from = DateUtils.minus(to, periodType, count);
        return getStats(from, to);
    }

    public StartsAndRuntimeList prev(int periodType) {
        return previous(periodType, 1);
    }

    public StartsAndRuntimeList prev(int periodType, int count) {
        return previous(periodType, count);
    }

    public StartsAndRuntimeList previous(int periodType) {
        return previous(periodType, 1);
    }

    public StartsAndRuntimeList previous(int periodType, int count) {
        long to = DateUtils.truncate(context.getRuntime(), periodType);
        long from = DateUtils.minus(to, periodType, count);
        return getStats(from, to);
    }

    private StartsAndRuntimeList getStats(long from, long to) {
        PointValueTime start = point.getPointValueBefore(from);
        List<PointValueTime> values = point.getPointValuesBetween(from, to);
        StartsAndRuntimeList stats = new StartsAndRuntimeList(start, values, from, to);
        return stats;
    }
}
