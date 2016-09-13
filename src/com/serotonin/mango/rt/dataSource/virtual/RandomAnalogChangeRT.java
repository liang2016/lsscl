/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.virtual;

import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataImage.types.NumericValue;
import com.serotonin.mango.vo.dataSource.virtual.RandomAnalogChangeVO;

public class RandomAnalogChangeRT extends ChangeTypeRT {
    private final RandomAnalogChangeVO vo;

    public RandomAnalogChangeRT(RandomAnalogChangeVO vo) {
        this.vo = vo;
    }

    @Override
    public MangoValue change(MangoValue currentValue) {
        double newValue = RANDOM.nextDouble();
        return new NumericValue((vo.getMax() - vo.getMin()) * newValue + vo.getMin());
    }
}
