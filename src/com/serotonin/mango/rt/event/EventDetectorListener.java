/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event;

/**
 *  
 */
public interface EventDetectorListener {
    void eventDetectorStateChanged(long time);

    void eventDetectorTerminated(SimpleEventDetector source);
}
