/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.virtual;

import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataImage.types.NumericValue;
import com.serotonin.mango.vo.dataSource.virtual.BrownianChangeVO;

public class BrownianChangeRT extends ChangeTypeRT {
    private final BrownianChangeVO vo;

    public BrownianChangeRT(BrownianChangeVO vo) {
        this.vo = vo;
    }

    @Override
    public MangoValue change(MangoValue currentValue) {
        double change = RANDOM.nextDouble() * vo.getMaxChange() * 2 - vo.getMaxChange();
        double newValue = currentValue.getDoubleValue() + change;
        if (newValue > vo.getMax())
            newValue = vo.getMax();
        if (newValue < vo.getMin())
            newValue = vo.getMin();
        return new NumericValue(newValue);
    }
}
