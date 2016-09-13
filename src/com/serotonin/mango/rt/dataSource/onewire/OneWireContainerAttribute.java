/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.onewire;

import com.serotonin.mango.vo.dataSource.onewire.OneWirePointLocatorVO;

/**
 *  
 */
public class OneWireContainerAttribute {
    private int id;
    private String description;
    private int startIndex;
    private int length;

    public OneWireContainerAttribute(int id) {
        this.id = id;
        description = OneWirePointLocatorVO.getAttributeDescription(id);
    }

    public OneWireContainerAttribute(int id, int startIndex, int length) {
        this(id);
        this.startIndex = startIndex;
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
