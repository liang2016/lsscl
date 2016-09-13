/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.comparators;

import java.util.ResourceBundle;

import com.serotonin.mango.vo.DataPointVO;

public class DataPointComparator extends BaseComparator<DataPointVO> {
    private static final int SORT_NAME = 1;
    private static final int SORT_DS_NAME = 2;
    private static final int SORT_ENABLED = 4;
    private static final int SORT_DATA_TYPE = 5;
    private static final int SORT_CONFIG = 6;

    private final ResourceBundle bundle;

    public DataPointComparator(ResourceBundle bundle, String sortField, boolean descending) {
        this.bundle = bundle;

        if ("name".equals(sortField))
            sortType = SORT_NAME;
        else if ("dsName".equals(sortField))
            sortType = SORT_DS_NAME;
        else if ("enabled".equals(sortField))
            sortType = SORT_ENABLED;
        else if ("dataType".equals(sortField))
            sortType = SORT_DATA_TYPE;
        else if ("config".equals(sortField))
            sortType = SORT_CONFIG;
        this.descending = descending;
    }

    public int compare(DataPointVO dp1, DataPointVO dp2) {
        int result = 0;
        if (sortType == SORT_NAME)
            result = dp1.getName().compareTo(dp2.getName());
        else if (sortType == SORT_DS_NAME)
            result = dp1.getDataSourceName().compareTo(dp2.getDataSourceName());
        else if (sortType == SORT_ENABLED)
            result = new Boolean(dp1.isEnabled()).compareTo(new Boolean(dp2.isEnabled()));
        else if (sortType == SORT_DATA_TYPE) {
            String s1 = dp1.getDataTypeMessage().getLocalizedMessage(bundle);
            String s2 = dp2.getDataTypeMessage().getLocalizedMessage(bundle);
            result = s1.compareTo(s2);
        }
        else if (sortType == SORT_CONFIG) {
            String s1 = dp1.getConfigurationDescription().getLocalizedMessage(bundle);
            String s2 = dp2.getConfigurationDescription().getLocalizedMessage(bundle);
            result = s1.compareTo(s2);
        }

        if (descending)
            return -result;
        return result;
    }
}
