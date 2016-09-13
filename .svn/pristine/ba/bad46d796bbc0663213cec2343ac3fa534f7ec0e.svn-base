/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.virtual;

import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataImage.types.NumericValue;
import com.serotonin.mango.vo.dataSource.virtual.IncrementAnalogChangeVO;

public class IncrementAnalogChangeRT extends ChangeTypeRT {
    private final IncrementAnalogChangeVO vo;
    private boolean decrement = false;

    public IncrementAnalogChangeRT(IncrementAnalogChangeVO vo) {
        this.vo = vo;
    }

    @Override
    public MangoValue change(MangoValue currentValue) {
        double newValue = currentValue.getDoubleValue();

        if (vo.isRoll()) {
            newValue += vo.getChange();
            if (newValue > vo.getMax())
                newValue = vo.getMin();
            if (newValue < vo.getMin())
                newValue = vo.getMax();
        }
        else {
            if (decrement) {
                newValue -= vo.getChange();
                if (newValue <= vo.getMin()) {
                    newValue = vo.getMin();
                    decrement = false;
                }
            }
            else {
                newValue += vo.getChange();
                if (newValue >= vo.getMax()) {
                    newValue = vo.getMax();
                    decrement = true;
                }
            }
        }

        return new NumericValue(newValue);
    }
}
