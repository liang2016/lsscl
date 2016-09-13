/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataImage;

import java.util.Comparator;

/**
 *  
 */
public class PvtTimeComparator implements Comparator<PointValueTime> {
    @Override
    public int compare(PointValueTime o1, PointValueTime o2) {
        long diff = o1.getTime() - o2.getTime();
        if (diff < 0)
            return -1;
        if (diff > 0)
            return 1;
        return 0;
    }
}
