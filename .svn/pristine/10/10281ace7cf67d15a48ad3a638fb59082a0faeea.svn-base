/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo;

import java.util.Comparator;

import com.serotonin.util.StringUtils;

/**
 *  
 */
public class DataPointNameComparator implements Comparator<DataPointVO> {
    public static final DataPointNameComparator instance = new DataPointNameComparator();

    public int compare(DataPointVO dp1, DataPointVO dp2) {
        if (StringUtils.isEmpty(dp1.getName()))
            return -1;
        return dp1.getName().compareToIgnoreCase(dp2.getName());
    }
}
