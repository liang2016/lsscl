/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.mango.rt.dataImage.SetPointSource;

/**
 *  
 */
public class AnonymousUser implements SetPointSource {
    public int getSetPointSourceId() {
        return 0;
    }

    public int getSetPointSourceType() {
        return SetPointSource.Types.ANONYMOUS;
    }

    @Override
    public void raiseRecursionFailureEvent() {
        throw new ShouldNeverHappenException("");
    }
}
