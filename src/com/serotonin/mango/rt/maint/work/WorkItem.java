/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.maint.work;

/**
 *  
 * 
 */
public interface WorkItem {
    /**
     * Uses a thread pool to immediately execute a process.
     */
    int PRIORITY_HIGH = 1;

    /**
     * Uses a single thread to execute processes sequentially. Assumes that processes will complete in a reasonable time
     * so that other processes do not have to wait long.
     */
    int PRIORITY_MEDIUM = 2;

    /**
     * Uses a single thread to execute processes sequentially. Assumes that processes can wait indefinately to run
     * without consequence.
     */
    int PRIORITY_LOW = 3;

    void execute();

    int getPriority();
}
