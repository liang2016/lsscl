/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr.beans;

import java.io.IOException;
import java.util.ResourceBundle;

import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.serotonin.mango.rt.dataSource.snmp.Version;
import com.serotonin.web.i18n.I18NUtils;

/**
 *  
 * 
 */
public class SnmpOidGet extends Thread implements TestingUtility {
    private final ResourceBundle bundle;
    private final String host;
    private final int port;
    private final Version version;
    private final String oid;
    private final int retries;
    private final int timeout;
    private String result;

    public SnmpOidGet(ResourceBundle bundle, String host, int port, Version version, String oid, int retries,
            int timeout) {
        this.bundle = bundle;
        this.host = host;
        this.port = port;
        this.version = version;
        this.oid = oid;
        this.retries = retries;
        this.timeout = timeout;
        start();
    }

    @Override
    public void run() {
        Snmp snmp = null;
        try {
            snmp = new Snmp(new DefaultUdpTransportMapping());
            version.addUser(snmp);
            snmp.listen();

            PDU pdu = version.createPDU();
            pdu.setType(PDU.GET);
            pdu.add(new VariableBinding(new OID(oid)));

            PDU response = snmp.send(pdu, version.getTarget(host, port, retries, timeout)).getResponse();
            if (response == null)
                result = I18NUtils.getMessage(bundle, "dsEdit.snmp.tester.noResponse");
            else
                result = response.get(0).getVariable().toString();
        }
        catch (IOException e) {
            result = e.getMessage();
        }
        finally {
            try {
                if (snmp != null)
                    snmp.close();
            }
            catch (IOException e) {
                // no op
            }
        }
    }

    public String getResult() {
        return result;
    }

    public void cancel() {
        // no op
    }
}
