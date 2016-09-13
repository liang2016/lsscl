/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.onewire;

import com.dalsemi.onewire.container.SwitchContainer;

/**
 *  
 */
public class NetworkPathElement {
    private final SwitchContainer switchContainer;
    private final Long address;
    private final int channel;

    public NetworkPathElement(SwitchContainer switchContainer, Long address, int channelNumber) {
        this.switchContainer = switchContainer;
        this.address = address;
        channel = channelNumber;
    }

    public SwitchContainer getContainer() {
        return switchContainer;
    }

    public int getChannel() {
        return channel;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + channel;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final NetworkPathElement other = (NetworkPathElement) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        }
        else if (!address.equals(other.address))
            return false;
        if (channel != other.channel)
            return false;
        return true;
    }
}
