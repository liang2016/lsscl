/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr.beans;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ResourceBundle;

import com.serotonin.mango.rt.dataSource.galil.GalilDataSourceRT;
import com.serotonin.mango.rt.dataSource.galil.GalilMessageParser;
import com.serotonin.mango.rt.dataSource.galil.GalilResponse;
import com.serotonin.util.queue.ByteQueue;
import com.serotonin.web.i18n.I18NUtils;

/**
 *  
 */
public class GalilCommandTester extends Thread {
    private final ResourceBundle bundle;
    private final long timeout;
    private final Socket socket;
    private String result;

    public GalilCommandTester(ResourceBundle bundle, String host, int port, int timeout, String command)
            throws IOException {
        this.bundle = bundle;
        this.timeout = timeout;
        socket = new Socket(host, port);
        socket.getOutputStream().write((command + "\r\n").getBytes(GalilDataSourceRT.CHARSET));
        start();
    }

    @Override
    public void run() {
        try {
            GalilMessageParser parser = new GalilMessageParser();
            ByteQueue queue = new ByteQueue();
            InputStream in = socket.getInputStream();
            long deadline = System.currentTimeMillis() + timeout;
            byte[] buf = new byte[32];
            GalilResponse response = null;

            while (System.currentTimeMillis() < deadline) {
                if (in.available() > 0) {
                    int len = in.read(buf);
                    if (len == -1)
                        break;
                    queue.push(buf, 0, len);

                    response = (GalilResponse) parser.parseMessage(queue);
                    if (response != null)
                        break;
                }
                else {
                    Thread.sleep(20);
                }
            }

            if (response == null)
                result = I18NUtils.getMessage(bundle, "dsEdit.galil.tester.timeout");
            else if (response.isErrorResponse())
                result = I18NUtils.getMessage(bundle, "dsEdit.galil.tester.noResponse");
            else
                result = response.getResponseData();
        }
        catch (Exception e) {
            result = e.getMessage();
        }
        finally {
            try {
                if (socket != null)
                    socket.close();
            }
            catch (IOException e) {
                // no op
            }
        }
    }

    public String getResult() {
        return result;
    }
}
