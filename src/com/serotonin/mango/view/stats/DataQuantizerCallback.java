/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.stats;

import com.serotonin.mango.rt.dataImage.types.MangoValue;

/**
 *  
 */
public interface DataQuantizerCallback {
    void quantizedData(MangoValue value, long time);
}
