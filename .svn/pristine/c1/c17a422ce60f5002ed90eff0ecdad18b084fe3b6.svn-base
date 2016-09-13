/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event.compound;

import com.serotonin.web.i18n.LocalizableException;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
public class ConditionParseException extends LocalizableException {
    private static final long serialVersionUID = -1;

    private int from = -1;
    private int to = -1;

    public ConditionParseException(LocalizableMessage message) {
        super(message);
    }

    /**
     * @param message
     *            the human-readable error message
     * @param from
     *            inclusive index of the start of the offending part of the statement
     * @param to
     *            exclusive index of the end of the offending part of the statement
     */
    public ConditionParseException(LocalizableMessage message, int from, int to) {
        super(message);
        this.from = from;
        this.to = to;
    }

    public boolean isRange() {
        return from != -1;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
}
