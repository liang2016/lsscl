/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.nmea;

import com.serotonin.messaging.IncomingRequestMessage;

/**
 *  
 */
public class NmeaMessage implements IncomingRequestMessage {
    private final String message;
    private String name;
    private String[] fields;

    public NmeaMessage(String message) {
        this.message = message;

        if (message != null) {
            String[] parts = message.split(",");

            if (parts.length > 0) {
                name = parts[0];

                fields = new String[parts.length - 1];
                System.arraycopy(parts, 1, fields, 0, fields.length);
            }
            else {
                fields = new String[0];
            }
        }
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public int getFieldCount() {
        return fields.length;
    }

    public String getField(int index1base) {
        return fields[index1base - 1];
    }
}
