/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr.beans;

import com.serotonin.util.StringUtils;

abstract public class BasePointState implements Cloneable {
    private String id;
    private String change;
    private String chart;
    private String messages;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public void removeEqualValue(BasePointState that) {
        if (StringUtils.isEqual(change, that.change))
            change = null;
        if (StringUtils.isEqual(chart, that.chart))
            chart = null;
        if (StringUtils.isEqual(messages, that.messages))
            messages = null;
    }

    public boolean isEmpty() {
        return change == null && chart == null && messages == null;
    }
}
