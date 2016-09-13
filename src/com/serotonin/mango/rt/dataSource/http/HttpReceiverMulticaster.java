/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.http;

import java.util.concurrent.CopyOnWriteArraySet;

import com.serotonin.util.IpAddressUtils;
import com.serotonin.util.IpWhiteListException;
import com.serotonin.util.StringUtils;

/**
 *  
 */
public class HttpReceiverMulticaster {
    private final CopyOnWriteArraySet<HttpMulticastListener> listeners = new CopyOnWriteArraySet<HttpMulticastListener>();

    public void addListener(HttpMulticastListener l) {
        listeners.add(l);
    }

    public void removeListener(HttpMulticastListener l) {
        listeners.remove(l);
    }

    public void multicast(HttpReceiverData data) {
        for (HttpMulticastListener l : listeners) {
            // Check if the listener cares about stuff from this ip address.
            try {
                if (!IpAddressUtils.ipWhiteListCheck(l.getIpWhiteList(), data.getRemoteIp()))
                    continue;
            }
            catch (IpWhiteListException e) {
                l.ipWhiteListError(e.getMessage());
                continue;
            }

            // Check if the listener cares about stuff from this device id.
            if (!StringUtils.isEmpty(data.getDeviceId())) {
                if (!StringUtils.globWhiteListMatchIgnoreCase(l.getDeviceIdWhiteList(), data.getDeviceId()))
                    continue;
            }

            // Everything checks out, so tell the listener about this data.
            l.data(data);
        }
    }
}
