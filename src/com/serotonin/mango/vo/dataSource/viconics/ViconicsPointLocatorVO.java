/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.dataSource.viconics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.mango.rt.dataSource.PointLocatorRT;
import com.serotonin.mango.rt.dataSource.viconics.ViconicsPointLocatorRT;
import com.serotonin.mango.vo.dataSource.AbstractPointLocatorVO;
import com.serotonin.viconics.config.StatPoint;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
@JsonRemoteEntity
public class ViconicsPointLocatorVO extends AbstractPointLocatorVO {
    public PointLocatorRT createRuntime() {
        return new ViconicsPointLocatorRT(this);
    }

    public LocalizableMessage getConfigurationDescription() {
        return new LocalizableMessage("common.default", StatPoint.addressToString(pointAddress) + "@"
                + deviceCommAddress);
    }

    private byte[] deviceIeee;
    private int deviceCommAddress;
    private int pointAddress;
    private int dataTypeId;
    private boolean settable;

    public byte[] getDeviceIeee() {
        return deviceIeee;
    }

    public void setDeviceIeee(byte[] deviceIeee) {
        this.deviceIeee = deviceIeee;
    }

    public int getDeviceCommAddress() {
        return deviceCommAddress;
    }

    public void setDeviceCommAddress(int deviceCommAddress) {
        this.deviceCommAddress = deviceCommAddress;
    }

    public int getPointAddress() {
        return pointAddress;
    }

    public void setPointAddress(int pointAddress) {
        this.pointAddress = pointAddress;
    }

    public int getDataTypeId() {
        return dataTypeId;
    }

    public void setDataTypeId(int dataTypeId) {
        this.dataTypeId = dataTypeId;
    }

    public boolean isSettable() {
        return settable;
    }

    public void setSettable(boolean settable) {
        this.settable = settable;
    }

    public void validate(DwrResponseI18n response) {
        // Nothing to change; nothing to validate
    }

    @Override
    public void addProperties(List<LocalizableMessage> list) {
        // no op
    }

    @Override
    public void addPropertyChanges(List<LocalizableMessage> list, Object o) {
        // no op
    }

    //
    // /
    // / Serialization
    // /
    //
    private static final long serialVersionUID = -1;
    private static final int version = 1;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(version);
        out.writeObject(deviceIeee);
        out.writeInt(deviceCommAddress);
        out.writeInt(pointAddress);
        out.writeInt(dataTypeId);
        out.writeBoolean(settable);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            deviceIeee = (byte[]) in.readObject();
            deviceCommAddress = in.readInt();
            pointAddress = in.readInt();
            dataTypeId = in.readInt();
            settable = in.readBoolean();
        }
    }
}
