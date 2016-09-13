/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.snmp;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.UdpAddress;

/**
 *  
 * 
 */
abstract public class Version {
    public static Version getVersion(int version, String community, String securityName, String authProtocol,
            String authPassphrase, String privProtocol, String privPassphrase, String engineId, String contextEngineId,
            String contextName) {
        if (version == SnmpConstants.version1)
            return new Version1(community);
        else if (version == SnmpConstants.version2c)
            return new Version2c(community);
        else if (version == SnmpConstants.version3)
            return new Version3(securityName, authProtocol, authPassphrase, privProtocol, privPassphrase, engineId,
                    contextEngineId, contextName);
        else
            throw new IllegalArgumentException("Invalid version value: " + version);
    }

    abstract public int getVersionId();

    abstract public void addUser(Snmp snmp);

    abstract public PDU createPDU();

    abstract protected Target getTarget();

    public Target getTarget(String host, int port, int retries, int timeout) throws UnknownHostException {
        Target target = getTarget();

        Address address = new UdpAddress(InetAddress.getByName(host), port);
        target.setAddress(address);
        target.setRetries(retries);
        target.setTimeout(timeout);

        return target;
    }
}
