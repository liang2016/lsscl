/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.http;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.util.StringUtils;

/**
 *  
 */
public class HttpReceiverData {
    private String remoteIp;
    private String deviceId;
    private long time = -1;
    private final List<HttpReceiverPointSample> data = new ArrayList<HttpReceiverPointSample>();
    private final List<String> unconsumedKeys = new ArrayList<String>();

    public List<HttpReceiverPointSample> getData() {
        return data;
    }

    public void addData(String key, String value, long time) {
        // Protect against XSS attacks.
        value = StringUtils.escapeLT(value);
        data.add(new HttpReceiverPointSample(key, value, time));
        unconsumedKeys.add(key);
    }

    public void consume(String key) {
        unconsumedKeys.remove(key);
    }

    public List<String> getUnconsumedKeys() {
        return unconsumedKeys;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
