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
import com.serotonin.mango.db.dao.CompoundEventDetectorDao;

/**
 *  
 */
@JsonRemoteEntity
public class CompoundDetectorEventType extends EventType {
    private int compoundDetectorId;
    private int duplicateHandling = EventType.DuplicateHandling.IGNORE;

    public CompoundDetectorEventType() {
        // Required for reflection.
    }

    public CompoundDetectorEventType(int compoundDetectorId) {
        this.compoundDetectorId = compoundDetectorId;
    }

    @Override
    public int getEventSourceId() {
        return EventType.EventSources.COMPOUND;
    }

    public int getCompoundDetectorId() {
        return compoundDetectorId;
    }

    @Override
    public String toString() {
        return "CompoundDetectorEventType(compoundDetectorId=" + compoundDetectorId + ")";
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
        return compoundDetectorId;
    }

    @Override
    public int getReferenceId2() {
        return 0;
    }

    @Override
    public int getCompoundEventDetectorId() {
        return compoundDetectorId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + compoundDetectorId;
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
        CompoundDetectorEventType other = (CompoundDetectorEventType) obj;
        if (compoundDetectorId != other.compoundDetectorId)
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
        map.put("XID", new CompoundEventDetectorDao().getCompoundEventDetector(compoundDetectorId).getXid());
    }

    @Override
    public void jsonDeserialize(JsonReader reader, JsonObject json) throws JsonException {
        super.jsonDeserialize(reader, json);
        compoundDetectorId = getCompoundEventDetectorId(json, "XID");
    }
}
