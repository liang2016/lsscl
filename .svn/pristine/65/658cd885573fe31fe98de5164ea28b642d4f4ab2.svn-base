/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.sql;

import com.serotonin.mango.rt.dataSource.PointLocatorRT;
import com.serotonin.mango.vo.dataSource.sql.SqlPointLocatorVO;

/**
 *  
 */
public class SqlPointLocatorRT extends PointLocatorRT {
    private final SqlPointLocatorVO vo;

    public SqlPointLocatorRT(SqlPointLocatorVO vo) {
        this.vo = vo;
    }

    @Override
    public boolean isSettable() {
        return vo.isSettable();
    }

    public SqlPointLocatorVO getVO() {
        return vo;
    }
}
