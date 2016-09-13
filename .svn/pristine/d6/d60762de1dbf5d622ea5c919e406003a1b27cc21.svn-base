/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr;

import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.vo.hierarchy.PointFolder;
import com.serotonin.mango.vo.hierarchy.PointHierarchy;

/**
 *  
 * 
 */
public class PointHierarchyDwr {
    public PointFolder getPointHierarchy(int scopeId) {
        DataPointDao dataPointDao = new DataPointDao();
        PointHierarchy ph = dataPointDao.getPointHierarchy(scopeId);
        return ph.getRoot();
    }

    public PointFolder savePointHierarchy(PointFolder rootFolder) {
        new DataPointDao().savePointHierarchy(rootFolder);
        return rootFolder;
    }
}
