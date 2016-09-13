/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr.beans;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.util.StringUtils;

/**
 *  
 */
public class CustomComponentState implements Cloneable {
    private int id;
    private String value;
    private Long time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public CustomComponentState clone() {
        try {
            return (CustomComponentState) super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new ShouldNeverHappenException(e);
        }
    }

    public void removeEqualValue(CustomComponentState that) {
        if (StringUtils.isEqual(value, that.value))
            value = null;
        if (StringUtils.isEqual(time, that.time))
            time = null;
    }

    public boolean isEmpty() {
        return value == null && time == null;
    }
}
