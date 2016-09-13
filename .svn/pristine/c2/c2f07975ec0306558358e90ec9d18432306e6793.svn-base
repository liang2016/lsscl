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
package com.serotonin.mango.web.dwr.beans;

import net.sf.openv4j.DataPoint;

/**
 *
 * @author aploese
 */
public class OpenV4JDataPointBean {

    private final DataPoint p;
    private final String value;

    public OpenV4JDataPointBean(DataPoint p, String value) {
        this.p = p;
        this.value = value;
    }

    public OpenV4JDataPointBean(DataPoint p) {
        this.p = p;
        value = null;
    }

    /**
     * @return the groupName
     */
    public String getGroupName() {
        return p.getGroup().getName();
    }

    /**
     * @return the groupLabel
     */
    public String getGroupLabel() {
        return p.getGroup().getLabel();
    }

    /**
     * @return the name
     */
    public String getName() {
        return p.getName();
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return p.getLabel();
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
