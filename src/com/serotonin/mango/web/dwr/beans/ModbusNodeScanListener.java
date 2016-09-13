/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr.beans;

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.NodeScanListener;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.util.ProgressiveTask;
import com.serotonin.web.i18n.I18NUtils;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 * 
 */
public class ModbusNodeScanListener implements NodeScanListener, TestingUtility {
    private final ResourceBundle bundle;
    private final ModbusMaster modbusMaster;
    private ProgressiveTask task;
    private final List<Integer> nodesFound = new LinkedList<Integer>();
    private String message = "";

    public ModbusNodeScanListener(ResourceBundle bundle, ModbusMaster modbusMaster, boolean serial) {
        this.bundle = bundle;
        this.modbusMaster = modbusMaster;

        // Don't start the scan if we can't initialize the master.
        try {
            modbusMaster.init();
        }
        catch (ModbusInitException e) {
            if (serial)
                message = new LocalizableMessage("dsEdit.modbus.scannerSerial.startError", e.getMessage())
                        .getLocalizedMessage(bundle);
            else
                message = new LocalizableMessage("dsEdit.modbus.scannerIp.startError", e.getMessage())
                        .getLocalizedMessage(bundle);
            return;
        }

        task = modbusMaster.scanForSlaveNodes(this);
    }

    public List<Integer> getNodesFound() {
        return nodesFound;
    }

    public String getMessage() {
        return message;
    }

    public boolean isFinished() {
        return task == null;
    }

    public synchronized void cancel() {
        if (task != null) {
            task.cancel();
            try {
                // Wait for the task to finish. The cleanup call below will notify when it gets called.
                wait();
            }
            catch (InterruptedException e) {
                // no op
            }
        }
    }

    private void cleanup() {
        modbusMaster.destroy();
        task = null;
        notifyAll();
    }

    //
    // / NodeScanListener implementation
    //
    public void progressUpdate(float progress) {
        message = new LocalizableMessage("dsEdit.modbus.scanner.progress", Integer.toString((int) (progress * 100)))
                .getLocalizedMessage(bundle);
    }

    public synchronized void taskCancelled() {
        cleanup();
        message = I18NUtils.getMessage(bundle, "dsEdit.modbus.scanner.cancelled");
    }

    public synchronized void taskCompleted() {
        cleanup();
        message = I18NUtils.getMessage(bundle, "dsEdit.modbus.scanner.complete");
    }

    public void nodeFound(int nodeNumber) {
        nodesFound.add(nodeNumber);
    }
}
