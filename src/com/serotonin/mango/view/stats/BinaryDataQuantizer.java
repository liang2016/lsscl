/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.stats;

import com.serotonin.mango.rt.dataImage.types.BinaryValue;
import com.serotonin.mango.rt.dataImage.types.MangoValue;

/**
 *  
 */
public class BinaryDataQuantizer extends AbstractDataQuantizer {
    private BinaryValue lastValue;

    public BinaryDataQuantizer(long start, long end, int buckets, DataQuantizerCallback callback) {
        super(start, end, buckets, callback);
    }

    @Override
    protected void periodData(MangoValue value) {
        lastValue = (BinaryValue) value;
    }

    @Override
    protected MangoValue donePeriod(int valueCounter) {
        return lastValue;
    }
}
