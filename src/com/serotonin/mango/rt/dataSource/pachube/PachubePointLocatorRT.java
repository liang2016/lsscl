/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.pachube;

import com.serotonin.mango.rt.dataSource.PointLocatorRT;
import com.serotonin.mango.vo.dataSource.pachube.PachubePointLocatorVO;

public class PachubePointLocatorRT extends PointLocatorRT {
    private final int feedId;
    private final String dataStreamId;
    private final int dataTypeId;
    private final String binary0Value;
    private final boolean settable;

    public PachubePointLocatorRT(PachubePointLocatorVO vo) {
        feedId = vo.getFeedId();
        dataStreamId = vo.getDataStreamId();
        dataTypeId = vo.getDataTypeId();
        binary0Value = vo.getBinary0Value();
        settable = vo.isSettable();
    }

    public int getFeedId() {
        return feedId;
    }

    public String getDataStreamId() {
        return dataStreamId;
    }

    @Override
    public boolean isSettable() {
        return settable;
    }

    public int getDataTypeId() {
        return dataTypeId;
    }

    public String getBinary0Value() {
        return binary0Value;
    }
}
