/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.virtual;

import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataImage.types.MultistateValue;
import com.serotonin.mango.vo.dataSource.virtual.RandomMultistateChangeVO;

public class RandomMultistateChangeRT extends ChangeTypeRT {
    private final RandomMultistateChangeVO vo;

    public RandomMultistateChangeRT(RandomMultistateChangeVO vo) {
        this.vo = vo;
    }

    @Override
    public MangoValue change(MangoValue currentValue) {
        int newValue = RANDOM.nextInt(vo.getValues().length);
        return new MultistateValue(vo.getValues()[newValue]);
    }
}
