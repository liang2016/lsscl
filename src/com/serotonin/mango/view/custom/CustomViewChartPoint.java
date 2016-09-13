/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.custom;

import com.serotonin.mango.vo.DataPointVO;

/**
 *  
 */
public class CustomViewChartPoint {
    private final DataPointVO dataPointVO;
    private final String color;

    public CustomViewChartPoint(DataPointVO dataPointVO, String color) {
        this.dataPointVO = dataPointVO;
        this.color = color;
    }

    public DataPointVO getDataPointVO() {
        return dataPointVO;
    }

    public String getColor() {
        return color;
    }
}
