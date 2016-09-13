/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.stats;

import com.serotonin.mango.rt.dataImage.types.MangoValue;

/**
 *  
 */
abstract public class AbstractDataQuantizer {
    private final long start;
    private final int buckets;
    private final long duration;
    private final DataQuantizerCallback callback;

    private int periodCounter;
    private double periodFrom;
    private double periodTo;
    private int valueCounter;

    public AbstractDataQuantizer(long start, long end, int buckets, DataQuantizerCallback callback) {
        this.start = start;
        this.buckets = buckets;
        duration = end - start;
        this.callback = callback;

        periodFrom = start;
        calculatePeriodTo();
    }

    private void calculatePeriodTo() {
        periodTo = periodFrom + ((double) duration) / buckets * ++periodCounter;
    }

    public void data(MangoValue value, long time) {
        while (time >= periodTo) {
            done();
            periodFrom = periodTo;
            periodTo = start + ((double) duration) / buckets * ++periodCounter;
        }

        valueCounter++;
        periodData(value);
    }

    public void done() {
        if (valueCounter > 0) {
            callback.quantizedData(donePeriod(valueCounter), (long) ((periodFrom + periodTo) / 2));
            valueCounter = 0;
        }
    }

    abstract protected void periodData(MangoValue value);

    abstract protected MangoValue donePeriod(int valueCounter);
}
