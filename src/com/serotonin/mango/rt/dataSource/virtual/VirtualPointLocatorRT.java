/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.virtual;

import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataSource.PointLocatorRT;

public class VirtualPointLocatorRT extends PointLocatorRT {
    private final ChangeTypeRT changeType;
    private MangoValue currentValue;
    private final boolean settable;

    public VirtualPointLocatorRT(ChangeTypeRT changeType, MangoValue startValue, boolean settable) {
        this.changeType = changeType;
        currentValue = startValue;
        this.settable = settable;
    }

    public ChangeTypeRT getChangeType() {
        return changeType;
    }

    public MangoValue getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(MangoValue currentValue) {
        this.currentValue = currentValue;
    }

    public void change() {
        currentValue = changeType.change(currentValue);
    }

    @Override
    public boolean isSettable() {
        return settable;
    }
}
