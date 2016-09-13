/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.snmp;

import org.snmp4j.smi.OctetString;

/**
 *  
 * 
 */
public class SnmpUtils {
    public static OctetString createOctetString(String s) {
        OctetString octetString;

        if (s.startsWith("0x"))
            octetString = OctetString.fromHexString(s.substring(2), ':');
        else
            octetString = new OctetString(s);

        return octetString;
    }
}
