/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.dataSource.ebro;

import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.rt.dataSource.ebro.EBI25Constants;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.dataSource.DataPointSaveHandler;
import com.serotonin.mango.vo.event.PointEventDetectorVO;
import com.serotonin.mango.web.dwr.beans.EBI25InterfaceUpdater;

/**
 *  
 */
public class EBI25PointSaveHandler implements DataPointSaveHandler {
    @Override
    public void handleSave(DataPointVO point) {
        // The limit point event detectors may have changed. Ensure that the locator limits and the values on the
        // device all match.
        EBI25DataSourceVO ds = (EBI25DataSourceVO) new DataSourceDao().getDataSource(point.getDataSourceId());
        EBI25PointLocatorVO locator = point.getPointLocator();

        PointEventDetectorVO ped;
        ped = EBI25Constants.findDetector(point.getEventDetectors(), true);
        if (ped != null)
            locator.setHighLimit(ped.getLimit());

        ped = EBI25Constants.findDetector(point.getEventDetectors(), false);
        if (ped != null)
            locator.setLowLimit(ped.getLimit());

        EBI25InterfaceUpdater updater = new EBI25InterfaceUpdater();
        updater.updateLogger(ds.getHost(), ds.getPort(), ds.getTimeout(), ds.getRetries(), locator);
    }
}
