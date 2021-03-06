/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.pop3;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataSource.PointLocatorRT;
import com.serotonin.mango.vo.dataSource.pop3.Pop3PointLocatorVO;
import com.serotonin.util.StringUtils;

/**
 *  
 */
public class Pop3PointLocatorRT extends PointLocatorRT {
    private final boolean findInSubject;
    private final Pattern valuePattern;
    private final boolean ignoreIfMissing;
    private final int dataTypeId;
    private String binary0Value;
    private DecimalFormat valueFormat;
    private final Pattern timePattern;
    private final boolean useReceivedTime;
    private final SimpleDateFormat timeFormat;
    private final boolean settable;

    public Pop3PointLocatorRT(Pop3PointLocatorVO vo) {
        findInSubject = vo.isFindInSubject();
        valuePattern = Pattern.compile(vo.getValueRegex());
        ignoreIfMissing = vo.isIgnoreIfMissing();
        dataTypeId = vo.getDataTypeId();

        if (dataTypeId == DataTypes.BINARY)
            binary0Value = vo.getValueFormat();
        else if (dataTypeId == DataTypes.NUMERIC && !StringUtils.isEmpty(vo.getValueFormat()))
            valueFormat = new DecimalFormat(vo.getValueFormat());

        useReceivedTime = vo.isUseReceivedTime();
        if (!useReceivedTime && !StringUtils.isEmpty(vo.getTimeRegex())) {
            timePattern = Pattern.compile(vo.getTimeRegex());
            timeFormat = new SimpleDateFormat(vo.getTimeFormat());
        }
        else {
            timePattern = null;
            timeFormat = null;
        }

        settable = vo.isSettable();
    }

    @Override
    public boolean isSettable() {
        return settable;
    }

    public boolean isFindInSubject() {
        return findInSubject;
    }

    public Pattern getValuePattern() {
        return valuePattern;
    }

    public boolean isIgnoreIfMissing() {
        return ignoreIfMissing;
    }

    public DecimalFormat getValueFormat() {
        return valueFormat;
    }

    public int getDataTypeId() {
        return dataTypeId;
    }

    public String getBinary0Value() {
        return binary0Value;
    }

    public boolean isUseReceivedTime() {
        return useReceivedTime;
    }

    public Pattern getTimePattern() {
        return timePattern;
    }

    public SimpleDateFormat getTimeFormat() {
        return timeFormat;
    }
}
