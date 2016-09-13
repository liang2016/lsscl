/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataImage;

import java.util.List;

/**
 *  
 */
public interface IDataPoint {
    List<PointValueTime> getLatestPointValues(int limit);

    void updatePointValue(PointValueTime newValue);

    void updatePointValue(PointValueTime newValue, boolean async);

    void setPointValue(PointValueTime newValue, SetPointSource source);

    PointValueTime getPointValue();

    PointValueTime getPointValueBefore(long time);

    List<PointValueTime> getPointValues(long since);

    List<PointValueTime> getPointValuesBetween(long from, long to);

    int getDataTypeId();
}
