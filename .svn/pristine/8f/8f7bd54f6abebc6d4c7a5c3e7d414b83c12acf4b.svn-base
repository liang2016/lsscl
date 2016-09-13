/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.virtual;

import com.serotonin.mango.rt.dataImage.DataPointRT;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.SetPointSource;
import com.serotonin.mango.rt.dataSource.PollingDataSource;
import com.serotonin.mango.vo.dataSource.virtual.VirtualDataSourceVO;

public class VirtualDataSourceRT extends PollingDataSource {
    public VirtualDataSourceRT(VirtualDataSourceVO vo) {
        super(vo);
        setPollingPeriod(vo.getUpdatePeriodType(), vo.getUpdatePeriods(), false);
    }

    @Override
    public void doPoll(long time) {
        for (DataPointRT dataPoint : dataPoints) {
            VirtualPointLocatorRT locator = dataPoint.getPointLocator();

            // Change the point values according to their definitions.
            locator.change();

            // Update the data image with the new value.
            dataPoint.updatePointValue(new PointValueTime(locator.getCurrentValue(), time));
        }
    }

    @Override
    public void setPointValue(DataPointRT dataPoint, PointValueTime valueTime, SetPointSource source) {
        VirtualPointLocatorRT l = dataPoint.getPointLocator();
        l.setCurrentValue(valueTime.getValue());
        dataPoint.setPointValue(valueTime, source);
    }

    @Override
    public void addDataPoint(DataPointRT dataPoint) {
        if (dataPoint.getPointValue() != null) {
            VirtualPointLocatorRT locator = dataPoint.getPointLocator();
            locator.setCurrentValue(dataPoint.getPointValue().getValue());
        }

        super.addDataPoint(dataPoint);
    }
}
