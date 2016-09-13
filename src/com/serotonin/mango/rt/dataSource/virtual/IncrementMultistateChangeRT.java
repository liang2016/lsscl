/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.virtual;

import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataImage.types.MultistateValue;
import com.serotonin.mango.vo.dataSource.virtual.IncrementMultistateChangeVO;

public class IncrementMultistateChangeRT extends ChangeTypeRT {
    private final IncrementMultistateChangeVO vo;
    private boolean decrement;

    public IncrementMultistateChangeRT(IncrementMultistateChangeVO vo) {
        this.vo = vo;
    }

    @Override
    public MangoValue change(MangoValue currentValue) {
        // Get the current index.
        int currentInt = currentValue.getIntegerValue();
        int index = -1;
        for (int i = 0; i < vo.getValues().length; i++) {
            if (vo.getValues()[i] == currentInt) {
                index = i;
                break;
            }
        }

        if (index == -1)
            return new MultistateValue(vo.getValues()[0]);

        if (vo.isRoll()) {
            index++;
            if (index >= vo.getValues().length)
                index = 0;
        }
        else {
            if (decrement) {
                index--;
                if (index == -1) {
                    index = 1;
                    decrement = false;
                }
            }
            else {
                index++;
                if (index == vo.getValues().length) {
                    index = vo.getValues().length - 2;
                    decrement = true;
                }
            }
        }

        return new MultistateValue(vo.getValues()[index]);
    }
}
