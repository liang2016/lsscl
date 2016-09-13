/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event.compound;

import java.util.LinkedList;
import java.util.List;

import com.serotonin.web.i18n.LocalizableException;

/**
 *  
 */
abstract public class LogicalOperator {
    abstract public boolean evaluate();

    abstract public void initialize() throws LocalizableException;

    abstract public void initSource(CompoundEventDetectorRT parent);

    abstract public void terminate(CompoundEventDetectorRT parent);

    public List<String> getDetectorKeys() {
        List<String> keys = new LinkedList<String>();
        appendDetectorKeys(keys);
        return keys;
    }

    abstract protected void appendDetectorKeys(List<String> keys);
}
