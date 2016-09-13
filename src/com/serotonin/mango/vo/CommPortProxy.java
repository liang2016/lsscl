/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo;

import gnu.io.CommPortIdentifier;
//report
public class CommPortProxy {
    private final String name;
    private String portType;
    private final boolean currentlyOwned;
    private final String currentOwner;

    public CommPortProxy(CommPortIdentifier cpid) {
        name = cpid.getName();
        switch (cpid.getPortType()) {
        case CommPortIdentifier.PORT_SERIAL:
            portType = "Serial";
            break;
        case CommPortIdentifier.PORT_PARALLEL:
            portType = "Parallel";
            break;
        default:
            portType = "Unknown (" + cpid.getPortType() + ")";
        }
        currentlyOwned = cpid.isCurrentlyOwned();
        currentOwner = cpid.getCurrentOwner();
    }

    public boolean isCurrentlyOwned() {
        return currentlyOwned;
    }

    public String getCurrentOwner() {
        return currentOwner;
    }

    public String getName() {
        return name;
    }

    public String getPortType() {
        return portType;
    }
}
