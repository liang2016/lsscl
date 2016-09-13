/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.http;

import com.serotonin.web.taglib.DateFunctions;

/**
 *  
 */
public class HttpReceiverPointSample {
    private final String key;
    private final String value;
    private final long time;

    public HttpReceiverPointSample(String key, String value, long time) {
        this.key = key;
        this.value = value;
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public long getTime() {
        return time;
    }

    public String getPrettyTime() {
        if (time == 0)
            return null;
        return DateFunctions.getTime(time);
    }
}
