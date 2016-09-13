/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr.beans;

import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.web.i18n.LocalizableMessage;

public class DataPointBean {
    private int id;
    private String name;
    private boolean settable;
    private int dataType;
    private final LocalizableMessage dataTypeMessage;
    private final String chartColour;

    public DataPointBean(DataPointVO vo) {
        id = vo.getId();
        name = vo.getExtendedName();
        settable = vo.getPointLocator().isSettable();
        dataType = vo.getPointLocator().getDataTypeId();
        dataTypeMessage = vo.getDataTypeMessage();
        chartColour = vo.getChartColour();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSettable() {
        return settable;
    }

    public void setSettable(boolean settable) {
        this.settable = settable;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public LocalizableMessage getDataTypeMessage() {
        return dataTypeMessage;
    }

    public String getChartColour() {
        return chartColour;
    }
}
