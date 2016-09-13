/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.galil;

import com.serotonin.messaging.OutgoingRequestMessage;

/**
 *  
 */
public class GalilRequest implements OutgoingRequestMessage {
    private final String data;

    public GalilRequest(String data) {
        this.data = data + "\r\n";
    }

    @Override
    public boolean expectsResponse() {
        return true;
    }

    public byte[] getMessageData() {
        return data.getBytes(GalilDataSourceRT.CHARSET);
    }
}
