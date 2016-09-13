/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.event;

/**
 *  
 */
abstract public class SimpleEventDetectorVO {
    public static final String POINT_EVENT_DETECTOR_PREFIX = "P";
    public static final String SCHEDULED_EVENT_PREFIX = "S";

    abstract public String getEventDetectorKey();
}
