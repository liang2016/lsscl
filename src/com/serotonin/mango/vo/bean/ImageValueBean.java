/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.bean;

/**
 *  
 */
public class ImageValueBean {
    private final String time;
    private final String uri;

    public ImageValueBean(String time, String uri) {
        this.time = time;
        this.uri = uri;
    }

    public String getTime() {
        return time;
    }

    public String getUri() {
        return uri;
    }
}
