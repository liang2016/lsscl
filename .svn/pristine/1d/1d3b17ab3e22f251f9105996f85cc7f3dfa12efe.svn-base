/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.galil;

import com.serotonin.messaging.IncomingResponseMessage;
import com.serotonin.messaging.OutgoingRequestMessage;
import com.serotonin.messaging.WaitingRoomKey;
import com.serotonin.messaging.WaitingRoomKeyFactory;

/**
 *  
 */
public class GalilWaitingRoomKeyFactory implements WaitingRoomKeyFactory {
    @Override
    public WaitingRoomKey createWaitingRoomKey(OutgoingRequestMessage request) {
        return new GalilWaitingRoomKey();
    }

    @Override
    public WaitingRoomKey createWaitingRoomKey(IncomingResponseMessage response) {
        return new GalilWaitingRoomKey();
    }

    static class GalilWaitingRoomKey implements WaitingRoomKey {
        @Override
        public int hashCode() {
            return 31;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            return true;
        }
    }
}
