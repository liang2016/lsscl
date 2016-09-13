/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource;

import com.serotonin.web.i18n.LocalizableException;
import com.serotonin.web.i18n.LocalizableMessage;

public class NoMatchException extends LocalizableException {
    private static final long serialVersionUID = 1L;

    public NoMatchException(LocalizableMessage message) {
        super(message);
    }
}
