/*
 *   LssclM2M - http://www.lsscl.com
 *   Copyright (C) 2010 Arne Pl\u00f6se
 *   @author Arne Pl\u00f6se
 *
 *    
 *    
 *    
 *    
 *
 *    
 *    
 *    
 *    
 *
 *    
 *   
 */
package com.serotonin.mango.web.dwr.beans;

import net.sf.openv4j.Devices;
import net.sf.openv4j.Protocol;

/**
 *
 * @author aploese
 */
public class OpenV4JProtocolBean {

    public static OpenV4JProtocolBean[] fromDevice(Devices device) {
        OpenV4JProtocolBean[] result = new OpenV4JProtocolBean[device.getProtocols().length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new OpenV4JProtocolBean(device.getProtocols()[i]);
        }
        return result;
    }
    final Protocol p;

    public OpenV4JProtocolBean(Protocol p) {
        this.p = p;
    }

    public String getName() {
        return p.getName();
    }

    public String getLabel() {
        return p.getLabel();
    }
}
