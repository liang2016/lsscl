/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.onewire;

import com.dalsemi.onewire.utils.Address;
import com.serotonin.mango.rt.dataSource.PointLocatorRT;
import com.serotonin.mango.vo.dataSource.onewire.OneWirePointLocatorVO;

/**
 *  
 */
public class OneWirePointLocatorRT extends PointLocatorRT {
    private final OneWirePointLocatorVO vo;
    private final Long address;

    public OneWirePointLocatorRT(OneWirePointLocatorVO vo) {
        this.vo = vo;
        address = Address.toLong(vo.getAddress());
    }

    @Override
    public boolean isSettable() {
        return vo.isSettable();
    }

    public Long getAddress() {
        return address;
    }

    public OneWirePointLocatorVO getVo() {
        return vo;
    }
}
