/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.report;

/**
 *  
 */
public interface ReportDataStreamHandler {
    /**
     * Called before the data for the given point is provided. A point may not have any data, so calls to setData are
     * not guaranteed.
     * 
     * @param pointInfo
     */
    void startPoint(ReportPointInfo pointInfo);

    /**
     * Provides a single data value for the current point.
     * 
     * @param rdv
     */
    void pointData(ReportDataValue rdv);
    void pointData(ReportDataValue rdv,ReportDataValue oldRdv);

    /**
     * Indicates that the last of the information has been sent, i.e. the other methods will no longer be called. Useful
     * for cleanup operations.
     */
    void done();
}
