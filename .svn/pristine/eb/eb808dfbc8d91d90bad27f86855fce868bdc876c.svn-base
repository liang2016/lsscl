/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr.beans;

import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import com.serotonin.io.serial.SerialParameters;
import com.serotonin.spinwave.SpinwaveReceiver;
import com.serotonin.spinwave.SwListener;
import com.serotonin.spinwave.SwMessage;
import com.serotonin.web.i18n.I18NUtils;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
public class SpinwaveSensorListener implements SwListener, TestingUtility {
    private final ResourceBundle bundle;
    private final SpinwaveReceiver spinwaveReceiver;
    private final Set<Long> sensorsFound = new HashSet<Long>();
    private String message;

    // Auto shut-off stuff
    private final AutoShutOff autoShutOff;

    public SpinwaveSensorListener(ResourceBundle bundle, String commPortId, int messageVersion) {
        this.bundle = bundle;
        message = I18NUtils.getMessage(bundle, "dsEdit.spinwave.tester.listening");

        SerialParameters params = new SerialParameters();
        params.setCommPortId(commPortId);
        params.setPortOwnerName("Mango Spinwave Sensor Listener");

        spinwaveReceiver = new SpinwaveReceiver(params, messageVersion);
        spinwaveReceiver.setListener(this);

        try {
            spinwaveReceiver.initialize();
        }
        catch (Exception e) {
            message = getMessage("dsEdit.spinwave.tester.startError", e.getMessage());
        }

        autoShutOff = new AutoShutOff() {
            @Override
            void shutOff() {
                SpinwaveSensorListener.this.cancel();
            }
        };
    }

    public Set<Long> getSensorsFound() {
        autoShutOff.update();
        return sensorsFound;
    }

    public String getMessage() {
        autoShutOff.update();
        return message;
    }

    public void cancel() {
        if (spinwaveReceiver != null) {
            autoShutOff.cancel();
            spinwaveReceiver.terminate();
        }
    }

    public void receivedException(Exception e) {
        message = getMessage("dsEdit.spinwave.tester.exception", e.getMessage());
    }

    public void receivedMessageMismatchException(Exception e) {
        message = getMessage("dsEdit.spinwave.tester.mismatch", e.getMessage());
    }

    public void receivedResponseException(Exception e) {
        message = getMessage("dsEdit.spinwave.tester.response", e.getMessage());
    }

    public void receivedHeartbeat(long arg0, boolean arg1) {
        // Ignore
    }

    public void receivedMessage(SwMessage message) {
        sensorsFound.add(message.getSensorAddress());
    }

    private String getMessage(String key, String param) {
        return new LocalizableMessage(key, param).getLocalizedMessage(bundle);
    }
}
