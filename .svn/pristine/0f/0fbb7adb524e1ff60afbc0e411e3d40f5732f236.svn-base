/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.snmp;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OctetString;

/**
 *  
 * 
 */
public class Version1 extends Version {
    private final OctetString community;

    public Version1(String community) {
        this.community = SnmpUtils.createOctetString(community);
    }

    @Override
    public int getVersionId() {
        return SnmpConstants.version1;
    }

    @Override
    public void addUser(Snmp snmp) {
        // no op
    }

    @Override
    public PDU createPDU() {
        return new PDU();
    }

    @Override
    public Target getTarget() {
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(community);
        return target;
    }
}
