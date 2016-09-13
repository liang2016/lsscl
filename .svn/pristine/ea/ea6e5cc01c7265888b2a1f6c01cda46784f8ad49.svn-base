/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.galil;

import com.serotonin.messaging.IncomingMessage;
import com.serotonin.messaging.MessageParser;
import com.serotonin.util.queue.ByteQueue;

/**
 *  
 */
public class GalilMessageParser implements MessageParser {
    private static final byte[] MESSAGE_END = ":".getBytes(GalilDataSourceRT.CHARSET);
    private static final byte[] ERROR_RESPONSE = "?".getBytes(GalilDataSourceRT.CHARSET);

    public IncomingMessage parseMessage(ByteQueue queue) {
        int end = queue.indexOf(MESSAGE_END);

        if (end == -1) {
            end = queue.indexOf(ERROR_RESPONSE);
            if (end == -1)
                return null;

            queue.pop(end + ERROR_RESPONSE.length);
            return new GalilResponse();
        }

        // Pop off the message
        byte[] data = new byte[end];
        queue.pop(data);

        // Pop off the end indicator
        queue.pop(MESSAGE_END.length);

        return new GalilResponse(data);
    }
}
