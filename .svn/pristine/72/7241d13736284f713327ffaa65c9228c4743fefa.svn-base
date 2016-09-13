/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.conversion;

/**
 *  
 */
public class LinearConversion implements Conversion {
    private final double slope;
    private final double intersect;

    public LinearConversion(double slope, double intersect) {
        this.slope = slope;
        this.intersect = intersect;
    }

    public LinearConversion getInverse() {
        return new LinearConversion(1 / slope, -intersect / slope);
    }

    public double convert(double value) {
        return slope * value + intersect;
    }
}
