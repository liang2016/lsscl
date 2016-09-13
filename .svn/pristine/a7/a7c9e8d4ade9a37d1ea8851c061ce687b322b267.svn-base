/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr.beans;

import java.util.ResourceBundle;

import com.serotonin.mango.Common;
import com.serotonin.mango.rt.dataSource.http.HttpMulticastListener;
import com.serotonin.mango.rt.dataSource.http.HttpReceiverData;
import com.serotonin.web.i18n.I18NUtils;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
public class HttpReceiverDataListener implements HttpMulticastListener, TestingUtility {
    final ResourceBundle bundle;
    private final String[] ipWhiteList;
    private final String[] deviceIdWhiteList;
    String message;
    private HttpReceiverData data;

    // Auto shut-off stuff
    private final AutoShutOff autoShutOff;

    public HttpReceiverDataListener(ResourceBundle bundle, String[] ipWhiteList, String[] deviceIdWhiteList) {
        this.bundle = bundle;
        message = I18NUtils.getMessage(bundle, "dsEdit.httpReceiver.tester.listening");

        this.ipWhiteList = ipWhiteList;
        this.deviceIdWhiteList = deviceIdWhiteList;
        Common.ctx.getHttpReceiverMulticaster().addListener(this);

        autoShutOff = new AutoShutOff() {
            @Override
            void shutOff() {
                message = I18NUtils.getMessage(HttpReceiverDataListener.this.bundle, "dsEdit.httpReceiver.tester.auto");
                HttpReceiverDataListener.this.cancel();
            }
        };
    }

    public HttpReceiverData getData() {
        autoShutOff.update();
        return data;
    }

    public String getMessage() {
        autoShutOff.update();
        return message;
    }

    public void cancel() {
        autoShutOff.cancel();
        Common.ctx.getHttpReceiverMulticaster().removeListener(this);
    }

    //
    // /
    // / HttpMulticastListener
    // /
    //
    public String[] getDeviceIdWhiteList() {
        return deviceIdWhiteList;
    }

    public String[] getIpWhiteList() {
        return ipWhiteList;
    }

    public void ipWhiteListError(String message) {
        message = new LocalizableMessage("dsEdit.httpReceiver.tester.whiteList", message).getLocalizedMessage(bundle);
    }

    public void data(HttpReceiverData data) {
        message = I18NUtils.getMessage(bundle, "dsEdit.httpReceiver.tester.data");
        this.data = data;
    }
}
