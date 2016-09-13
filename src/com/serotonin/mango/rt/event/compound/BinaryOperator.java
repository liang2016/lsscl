/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event.compound;

import java.util.List;

import com.serotonin.web.i18n.LocalizableException;

/**
 *  
 */
abstract public class BinaryOperator extends LogicalOperator {
    protected LogicalOperator operand1;
    protected LogicalOperator operand2;

    public BinaryOperator(LogicalOperator operand1, LogicalOperator operand2) {
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    @Override
    protected void appendDetectorKeys(List<String> keys) {
        operand1.appendDetectorKeys(keys);
        operand2.appendDetectorKeys(keys);
    }

    @Override
    public void initialize() throws LocalizableException {
        operand1.initialize();
        operand2.initialize();
    }

    @Override
    public void initSource(CompoundEventDetectorRT parent) {
        operand1.initSource(parent);
        operand2.initSource(parent);
    }

    @Override
    public void terminate(CompoundEventDetectorRT parent) {
        operand1.terminate(parent);
        operand2.terminate(parent);
    }
}
