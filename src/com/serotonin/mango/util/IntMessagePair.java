/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.util;

import java.io.Serializable;

import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
public class IntMessagePair implements Serializable {
    private static final long serialVersionUID = -1;

    private int key;
    private LocalizableMessage message;

    public IntMessagePair() {
        // no op
    }

    public IntMessagePair(int key, LocalizableMessage message) {
        this.key = key;
        this.message = message;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public LocalizableMessage getMessage() {
        return message;
    }

    public void setMessage(LocalizableMessage message) {
        this.message = message;
    }
}
