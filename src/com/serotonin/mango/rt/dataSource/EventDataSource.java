/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.mango.rt.dataImage.DataPointRT;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.SetPointSource;
import com.serotonin.mango.vo.dataSource.DataSourceVO;

abstract public class EventDataSource extends DataSourceRT {
    protected List<DataPointRT> dataPoints = new ArrayList<DataPointRT>();

    public EventDataSource(DataSourceVO<?> vo) {
        super(vo);
    }

    @Override
    public void addDataPoint(DataPointRT dataPoint) {
        synchronized (pointListChangeLock) {
            // Remove any existing instances of the points.
            dataPoints.remove(dataPoint);
            dataPoints.add(dataPoint);
        }
    }

    @Override
    public void removeDataPoint(DataPointRT dataPoint) {
        synchronized (pointListChangeLock) {
            dataPoints.remove(dataPoint);
        }
    }

    @Override
    public void setPointValue(DataPointRT dataPoint, PointValueTime valueTime, SetPointSource source) {
        // Typically, event based data sources cannot set point values, so don't make subclasses implement this.
    }
}
