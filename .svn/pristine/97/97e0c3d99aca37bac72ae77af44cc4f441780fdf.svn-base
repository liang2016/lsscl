/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.publish.persistent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.mango.vo.publish.PublishedPointVO;

@JsonRemoteEntity
public class PersistentPointVO extends PublishedPointVO {
    // No properties

    // The index and data point VO are only used at runtime.
    private int index;
    private String xid;
    private byte[] serializedDataPoint;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public byte[] getSerializedDataPoint() {
        return serializedDataPoint;
    }

    public void setSerializedDataPoint(byte[] serializedDataPoint) {
        this.serializedDataPoint = serializedDataPoint;
    }

    //
    //
    // Serialization
    //
    private static final long serialVersionUID = -1;
    private static final int version = 1;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(version);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            // no properties
        }
    }
}
