/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.http;

import com.serotonin.mango.rt.dataSource.PointLocatorRT;
import com.serotonin.mango.vo.dataSource.http.HttpImagePointLocatorVO;

/**
 *  
 */
public class HttpImagePointLocatorRT extends PointLocatorRT {
    private final HttpImagePointLocatorVO vo;

    public HttpImagePointLocatorRT(HttpImagePointLocatorVO vo) {
        this.vo = vo;
    }

    @Override
    public boolean isSettable() {
        return vo.isSettable();
    }

    public HttpImagePointLocatorVO getVo() {
        return vo;
    }
}
