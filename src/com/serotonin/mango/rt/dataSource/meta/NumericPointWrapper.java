/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.meta;

import java.util.List;

import com.serotonin.mango.rt.dataImage.IDataPoint;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.util.DateUtils;
import com.serotonin.mango.view.stats.AnalogStatistics;

/**
 *  
 */
public class NumericPointWrapper extends AbstractPointWrapper {
    public NumericPointWrapper(IDataPoint point, WrapperContext context) {
        super(point, context);
    }

    public double getValue() {
        MangoValue value = getValueImpl();
        if (value == null)
            return 0;
        return value.getDoubleValue();
    }

    @Override
    public String toString() {
        return "{value=" + getValue() + ", ago(periodType, count), past(periodType, count), prev(periodType, count), "
                + "previous(periodType, count)}";
    }

    public double ago(int periodType) {
        return ago(periodType, 1);
    }

    public double ago(int periodType, int count) {
        long from = DateUtils.minus(context.getRuntime(), periodType, count);
        PointValueTime pvt = point.getPointValueBefore(from);
        if (pvt == null)
            return 0;
        return pvt.getDoubleValue();
    }

    public AnalogStatistics past(int periodType) {
        return past(periodType, 1);
    }

    public AnalogStatistics past(int periodType, int count) {
        long to = context.getRuntime();
        long from = DateUtils.minus(to, periodType, count);
        return getStats(from, to);
    }

    public AnalogStatistics prev(int periodType) {
        return previous(periodType, 1);
    }

    public AnalogStatistics prev(int periodType, int count) {
        return previous(periodType, count);
    }

    public AnalogStatistics previous(int periodType) {
        return previous(periodType, 1);
    }

    public AnalogStatistics previous(int periodType, int count) {
        long to = DateUtils.truncate(context.getRuntime(), periodType);
        long from = DateUtils.minus(to, periodType, count);
        return getStats(from, to);
    }

    private AnalogStatistics getStats(long from, long to) {
        PointValueTime start = point.getPointValueBefore(from);
        List<PointValueTime> values = point.getPointValuesBetween(from, to);
        AnalogStatistics stats = new AnalogStatistics(start, values, from, to);
        return stats;
    }
}
