/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.stats;

import com.serotonin.mango.rt.dataImage.types.MangoValue;

/**
 *  
 */
public class StartsAndRuntime {
    MangoValue value;
    int starts;
    long runtime;
    double proportion;

    public Object getValue() {
        if (value == null)
            return null;
        return value.getObjectValue();
    }

    public MangoValue getMangoValue() {
        return value;
    }

    public long getRuntime() {
        return runtime;
    }

    public double getProportion() {
        return proportion;
    }

    public double getPercentage() {
        return proportion * 100;
    }

    public int getStarts() {
        return starts;
    }

    void calculateRuntimePercentage(long duration) {
        proportion = ((double) runtime) / duration;
    }

    public String getHelp() {
        return toString();
    }

    @Override
    public String toString() {
        return "{value: " + value + ", starts: " + starts + ", runtime: " + runtime + ", proportion: " + proportion
                + ", percentage: " + getPercentage() + "}";
    }
}
