/*
 *   LssclM2M - http://www.lsscl.com
 *   Copyright (C) 2010 Arne Pl\u00f6se
 *   @author Arne Pl\u00f6se
 *
 *    
 *    
 *    
 *    
 *
 *    
 *    
 *    
 *    
 *
 *    
 *   
 */
package com.serotonin.mango.rt.dataSource.openv4j;

import net.sf.openv4j.DataPoint;

import com.serotonin.mango.rt.dataSource.PointLocatorRT;
import com.serotonin.mango.vo.dataSource.openv4j.OpenV4JPointLocatorVO;

// No need to encapsulate as string like vo
public class OpenV4JPointLocatorRT extends PointLocatorRT {
    private final OpenV4JPointLocatorVO vo;
    private final DataPoint dataPoint;

    public OpenV4JPointLocatorRT(OpenV4JPointLocatorVO vo) {
        this.vo = vo;
        dataPoint = DataPoint.valueOf(vo.getDataPointName());
    }

    @Override
    public boolean isSettable() {
        return vo.isSettable();
    }

    /**
     * @return the vo
     */
    public OpenV4JPointLocatorVO getVo() {
        return vo;
    }

    /**
     * @return the dataPoint
     */
    public DataPoint getDataPoint() {
        return dataPoint;
    }
}
