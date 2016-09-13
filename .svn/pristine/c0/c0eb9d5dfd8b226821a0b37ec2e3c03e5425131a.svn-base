/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.ebro;

import com.serotonin.modbus4j.ExceptionResult;

/**
 *  
 */
public class ExceptionResultException extends Exception {
    private static final long serialVersionUID = 1L;

    private final String key;
    private final ExceptionResult exceptionResult;

    public ExceptionResultException(String key, ExceptionResult exceptionResult) {
        this.key = key;
        this.exceptionResult = exceptionResult;
    }

    public String getKey() {
        return key;
    }

    public ExceptionResult getExceptionResult() {
        return exceptionResult;
    }
}
