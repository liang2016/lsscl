/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event.type;

import java.util.Map;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.mango.db.dao.DataPointDao;

@JsonRemoteEntity
public class DataPointEventType extends EventType {
    private int dataSourceId = -1;
    private int dataPointId;
    private int pointEventDetectorId;
    private int duplicateHandling = EventType.DuplicateHandling.IGNORE;

    public DataPointEventType() {
        // Required for reflection.
    }

    public DataPointEventType(int dataPointId, int pointEventDetectorId) {
        this.dataPointId = dataPointId;
        this.pointEventDetectorId = pointEventDetectorId;
    }

    @Override
    public int getEventSourceId() {
        return EventType.EventSources.DATA_POINT;
    }

    @Override
    public int getDataSourceId() {
        if (dataSourceId == -1)
            dataSourceId = new DataPointDao().getDataPoint(dataPointId).getDataSourceId();
        return dataSourceId;
    }

    @Override
    public int getDataPointId() {
        return dataPointId;
    }

    public int getPointEventDetectorId() {
        return pointEventDetectorId;
    }

    @Override
    public String toString() {
        return "DataPointEventType(dataPointId=" + dataPointId + ", detectorId=" + pointEventDetectorId + ")";
    }

    @Override
    public int getDuplicateHandling() {
        return duplicateHandling;
    }

    public void setDuplicateHandling(int duplicateHandling) {
        this.duplicateHandling = duplicateHandling;
    }

    @Override
    public int getReferenceId1() {
        return dataPointId;
    }

    @Override
    public int getReferenceId2() {
        return pointEventDetectorId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + pointEventDetectorId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DataPointEventType other = (DataPointEventType) obj;
        if (pointEventDetectorId != other.pointEventDetectorId)
            return false;
        return true;
    }

    //
    // /
    // / Serialization
    // /
    //
    @Override
    public void jsonSerialize(Map<String, Object> map) {
        super.jsonSerialize(map);
        DataPointDao dataPointDao = new DataPointDao();
        map.put("dataPointXID", dataPointDao.getDataPoint(dataPointId).getXid());
        map.put("detectorXID", dataPointDao.getDetectorXid(pointEventDetectorId));
    }

    @Override
    public void jsonDeserialize(JsonReader reader, JsonObject json) throws JsonException {
        super.jsonDeserialize(reader, json);
        dataPointId = getDataPointId(json, "dataPointXID");
        pointEventDetectorId = getPointEventDetectorId(json, dataPointId, "detectorXID");
    }
}
