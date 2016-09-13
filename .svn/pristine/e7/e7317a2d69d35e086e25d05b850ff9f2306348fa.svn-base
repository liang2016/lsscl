/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataImage;

/**
 * A set point source is anything that can cause a set point to occur. For example, a user can use the interface to
 * explicitly set a point, in which case the user is the ser point source. A program could reset a value to 0 every
 * date, making that program the set point source. This information is stored in the database as a point value
 * annotation.
 * 
 *  
 */
public interface SetPointSource {
    public interface Types {
        int USER = 1;
        int EVENT_HANDLER = 2;
        int ANONYMOUS = 3;
        int POINT_LINK = 4;
    }

    public int getSetPointSourceType();

    public int getSetPointSourceId();

    public void raiseRecursionFailureEvent();
}
