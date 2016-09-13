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
public class DataPointExtendedNameComparator implements Comparator<DataPointVO> {
    public static final DataPointExtendedNameComparator instance = new DataPointExtendedNameComparator();

    public int compare(DataPointVO dp1, DataPointVO dp2) {
        if (StringUtils.isEmpty(dp1.getExtendedName()))
            return -1;
        return dp1.getExtendedName().compareToIgnoreCase(dp2.getExtendedName());
    }
}
