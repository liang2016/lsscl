/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.http;

/**
 *  
 */
public interface HttpMulticastListener {
    String[] getIpWhiteList();

    String[] getDeviceIdWhiteList();

    void data(HttpReceiverData data);

    void ipWhiteListError(String message);
}
