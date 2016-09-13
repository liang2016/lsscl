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
import com.serotonin.mango.view.stats.ValueChangeCounter;

/**
 *  
 */
public class AlphanumericPointWrapper extends AbstractPointWrapper {
    public AlphanumericPointWrapper(IDataPoint point, WrapperContext context) {
        super(point, context);
    }

    public String getValue() {
        MangoValue value = getValueImpl();
        if (value == null)
            return "";
        return value.getStringValue();
    }

    @Override
    public String toString() {
        return "{value=" + getValue() + ", ago(periodType, count)}";
    }

    public String ago(int periodType) {
        return ago(periodType, 1);
    }

    public String ago(int periodType, int count) {
        long from = DateUtils.minus(context.getRuntime(), periodType, count);
        PointValueTime pvt = point.getPointValueBefore(from);
        if (pvt == null)
            return null;
        return pvt.getValue().getStringValue();
    }

    public ValueChangeCounter past(int periodType) {
        return past(periodType, 1);
    }

    public ValueChangeCounter past(int periodType, int count) {
        long to = context.getRuntime();
        long from = DateUtils.minus(to, periodType, count);
        return getStats(from, to);
    }

    public ValueChangeCounter prev(int periodType) {
        return previous(periodType, 1);
    }

    public ValueChangeCounter prev(int periodType, int count) {
        return previous(periodType, count);
    }

    public ValueChangeCounter previous(int periodType) {
        return previous(periodType, 1);
    }

    public ValueChangeCounter previous(int periodType, int count) {
        long to = DateUtils.truncate(context.getRuntime(), periodType);
        long from = DateUtils.minus(to, periodType, count);
        return getStats(from, to);
    }

    private ValueChangeCounter getStats(long from, long to) {
        PointValueTime start = point.getPointValueBefore(from);
        List<PointValueTime> values = point.getPointValuesBetween(from, to);
        ValueChangeCounter stats = new ValueChangeCounter(start, values);
        return stats;
    }
}
