/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.util;

import com.serotonin.json.JsonException;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
public class LocalizableJsonException extends JsonException {
    private static final long serialVersionUID = 1L;

    private final LocalizableMessage msg;

    public LocalizableJsonException(LocalizableMessage msg) {
        this.msg = msg;
    }

    public LocalizableJsonException(String key, Object... args) {
        msg = new LocalizableMessage(key, args);
    }

    public LocalizableMessage getMsg() {
        return msg;
    }
}
