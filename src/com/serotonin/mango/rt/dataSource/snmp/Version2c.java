/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.snmp;

import org.snmp4j.mp.SnmpConstants;

/**
 *  
 * 
 */
public class Version2c extends Version1 {
    public Version2c(String community) {
        super(community);
    }

    @Override
    public int getVersionId() {
        return SnmpConstants.version2c;
    }
}
