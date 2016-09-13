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
public class NotOperator extends LogicalOperator {
    private final LogicalOperator operand;

    public NotOperator(LogicalOperator operand) {
        this.operand = operand;
    }

    @Override
    public boolean evaluate() {
        return !operand.evaluate();
    }

    @Override
    public String toString() {
        return "NOT(" + operand + ")";
    }

    @Override
    protected void appendDetectorKeys(List<String> keys) {
        operand.appendDetectorKeys(keys);
    }

    @Override
    public void initialize() throws LocalizableException {
        operand.initialize();
    }

    @Override
    public void initSource(CompoundEventDetectorRT parent) {
        operand.initSource(parent);
    }

    @Override
    public void terminate(CompoundEventDetectorRT parent) {
        operand.terminate(parent);
    }
}
