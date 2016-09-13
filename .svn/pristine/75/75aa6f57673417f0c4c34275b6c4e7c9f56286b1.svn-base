/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.meta;

import com.serotonin.mango.util.DateUtils;

/**
 *  
 */
class WrapperContext {
    private final long runtime;

    WrapperContext(long runtime) {
        this.runtime = runtime;
    }

    public long getRuntime() {
        return runtime;
    }

    public long millisInPrev(int periodType) {
        return millisInPrevious(periodType, 1);
    }

    public long millisInPrevious(int periodType) {
        return millisInPrevious(periodType, 1);
    }

    public long millisInPrev(int periodType, int count) {
        return millisInPrevious(periodType, count);
    }

    public long millisInPrevious(int periodType, int count) {
        long to = DateUtils.truncate(runtime, periodType);
        long from = DateUtils.minus(to, periodType, count);
        return to - from;
    }

    public long millisInPast(int periodType) {
        return millisInPast(periodType, 1);
    }

    public long millisInPast(int periodType, int count) {
        long from = DateUtils.minus(runtime, periodType, count);
        return runtime - from;
    }

    @Override
    public String toString() {
        return "{millisInPast(periodType, count), millisInPrev(periodType, count), "
                + "millisInPrevious(periodType, count)}";
    }

    public String getHelp() {
        return toString();
    }
}
