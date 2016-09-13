/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.spinwave;

import com.serotonin.mango.rt.dataSource.PointLocatorRT;
import com.serotonin.mango.vo.dataSource.spinwave.BaseSpinwavePointLocatorVO;

/**
 *  
 */
public class SpinwavePointLocatorRT extends PointLocatorRT {
    private final BaseSpinwavePointLocatorVO vo;

    public SpinwavePointLocatorRT(BaseSpinwavePointLocatorVO vo) {
        this.vo = vo;
    }

    @Override
    public boolean isSettable() {
        return vo.isSettable();
    }

    public BaseSpinwavePointLocatorVO getPointLocatorVO() {
        return vo;
    }
}
