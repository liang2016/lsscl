/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataImage;

import java.util.List;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.PointValueDao;

/**
 *  
 */
public class PointValueFacade {
    private final int dataPointId;
    private final DataPointRT point;
    private final PointValueDao pointValueDao;

    public PointValueFacade(int dataPointId) {
        this.dataPointId = dataPointId;
        point = Common.ctx.getRuntimeManager().getDataPoint(dataPointId);
        pointValueDao = new PointValueDao();
    }

    public List<PointValueTime> getPointValues(long since) {
        if (point != null)
            return point.getPointValues(since);
        return pointValueDao.getPointValues(dataPointId, since);
    }

    public PointValueTime getPointValueBefore(long time) {
        if (point != null)
            return point.getPointValueBefore(time);
        return pointValueDao.getPointValueBefore(dataPointId, time);
    }

    public PointValueTime getPointValueAt(long time) {
        if (point != null)
            return point.getPointValueAt(time);
        return pointValueDao.getPointValueAt(dataPointId, time);
    }

    public PointValueTime getPointValue() {
        if (point != null)
            return point.getPointValue();
        return pointValueDao.getLatestPointValue(dataPointId);
    }

    public List<PointValueTime> getPointValuesBetween(long from, long to) {
        if (point != null)
            return point.getPointValuesBetween(from, to);
        return pointValueDao.getPointValuesBetween(dataPointId, from, to);
    }

    public List<PointValueTime> getLatestPointValues(int limit) {
        if (point != null)
            return point.getLatestPointValues(limit);
        return pointValueDao.getLatestPointValues(dataPointId, limit);
    }
}
