/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.publish;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonSerializable;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.util.LocalizableJsonException;
import com.serotonin.mango.vo.DataPointVO;

/**
 *  
 */
abstract public class PublishedPointVO implements Serializable, JsonSerializable {
    private int dataPointId;

    public int getDataPointId() {
        return dataPointId;
    }

    public void setDataPointId(int dataPointId) {
        this.dataPointId = dataPointId;
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
        out.writeInt(dataPointId);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            dataPointId = in.readInt();
        }
    }

    public void jsonSerialize(Map<String, Object> map) {
        DataPointDao dataPointDao = new DataPointDao();
        DataPointVO dp = dataPointDao.getDataPoint(dataPointId);
        String xid;
        if (dp == null)
            xid = null;
        else
            xid = dp.getXid();

        map.put("dataPointId", xid);
    }

    public void jsonDeserialize(JsonReader reader, JsonObject json) throws LocalizableJsonException {
        DataPointDao dataPointDao = new DataPointDao();
        String xid = json.getString("dataPointId");
        if (xid == null)
            throw new LocalizableJsonException("emport.error.publishedPoint.missing", "dataPointId");

        DataPointVO vo = dataPointDao.getDataPoint(xid);
        if (vo == null)
            throw new LocalizableJsonException("emport.error.missingPoint", xid);
        dataPointId = vo.getId();
    }
}
