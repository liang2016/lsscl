/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.vmstat;

import com.serotonin.mango.rt.dataSource.PointLocatorRT;
import com.serotonin.mango.vo.dataSource.vmstat.VMStatPointLocatorVO;

/**
 *  
 */
public class VMStatPointLocatorRT extends PointLocatorRT {
    private final VMStatPointLocatorVO vo;

    public VMStatPointLocatorRT(VMStatPointLocatorVO vo) {
        this.vo = vo;
    }

    @Override
    public boolean isSettable() {
        return vo.isSettable();
    }

    public VMStatPointLocatorVO getPointLocatorVO() {
        return vo;
    }
}
