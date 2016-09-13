/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.report;

import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.view.stats.IValueTime;

/**
 *  
 */
public class ReportDataValue implements IValueTime {
    private int reportPointId;
    private MangoValue value;
    private long time;
    private String annotation;

    public ReportDataValue() {
        // no op
    }

    public ReportDataValue(MangoValue value, long time) {
        this.value = value;
        this.time = time;
    }

    public int getReportPointId() {
        return reportPointId;
    }

    public void setReportPointId(int reportPointId) {
        this.reportPointId = reportPointId;
    }

    public MangoValue getValue() {
        return value;
    }

    public void setValue(MangoValue value) {
        this.value = value;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    @Override
    public String toString() {
        return value + "@" + time;
    }
}
