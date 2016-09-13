/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.galil;

import com.serotonin.messaging.IncomingResponseMessage;

/**
 *  
 */
public class GalilResponse implements IncomingResponseMessage {
    private final boolean errorResponse;
    private final byte[] messageData;

    public GalilResponse() {
        errorResponse = true;
        messageData = new byte[0];
    }

    public GalilResponse(byte[] messageData) {
        errorResponse = false;
        this.messageData = messageData;
    }

    // public byte[] getMessageData() {
    // return messageData;
    // }

    public boolean isErrorResponse() {
        return errorResponse;
    }

    public String getResponseData() {
        return new String(messageData, GalilDataSourceRT.CHARSET).trim();
    }
}
