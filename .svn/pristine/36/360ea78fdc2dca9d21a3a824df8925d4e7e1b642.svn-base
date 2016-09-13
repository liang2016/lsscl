/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.nmea;

import com.serotonin.mango.rt.dataSource.PointLocatorRT;
import com.serotonin.mango.vo.dataSource.nmea.NmeaPointLocatorVO;

/**
 *  
 */
public class NmeaPointLocatorRT extends PointLocatorRT {
    private final NmeaPointLocatorVO vo;

    public NmeaPointLocatorRT(NmeaPointLocatorVO vo) {
        this.vo = vo;
    }

    @Override
    public boolean isSettable() {
        return vo.isSettable();
    }

    public String getMessageName() {
        return vo.getMessageName();
    }

    public int getFieldIndex() {
        return vo.getFieldIndex();
    }

    public int getDataTypeId() {
        return vo.getDataTypeId();
    }

    public String getBinary0Value() {
        return vo.getBinary0Value();
    }
}
