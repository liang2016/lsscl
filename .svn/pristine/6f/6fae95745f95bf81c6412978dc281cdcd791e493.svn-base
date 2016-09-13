/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.virtual;

import com.serotonin.mango.rt.dataImage.types.BinaryValue;
import com.serotonin.mango.rt.dataImage.types.MangoValue;

public class RandomBooleanChangeRT extends ChangeTypeRT {
    @Override
    public MangoValue change(MangoValue currentValue) {
        return new BinaryValue(RANDOM.nextBoolean());
    }
}
