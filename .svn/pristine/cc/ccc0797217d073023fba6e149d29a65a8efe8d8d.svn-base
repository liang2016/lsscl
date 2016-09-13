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
import com.serotonin.mango.db.dao.ScheduledEventDao;

/**
 *  
 * 
 */
@JsonRemoteEntity
public class ScheduledEventType extends EventType {
    private int scheduleId;
    private int duplicateHandling = EventType.DuplicateHandling.IGNORE;

    public ScheduledEventType() {
        // Required for reflection.
    }

    public ScheduledEventType(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Override
    public int getEventSourceId() {
        return EventType.EventSources.SCHEDULED;
    }

    @Override
    public int getScheduleId() {
        return scheduleId;
    }

    @Override
    public String toString() {
        return "ScheduledEventType(scheduleId=" + scheduleId + ")";
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
        return scheduleId;
    }

    @Override
    public int getReferenceId2() {
        return 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + scheduleId;
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
        ScheduledEventType other = (ScheduledEventType) obj;
        if (scheduleId != other.scheduleId)
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
        map.put("XID", new ScheduledEventDao().getScheduledEvent(scheduleId).getXid());
    }

    @Override
    public void jsonDeserialize(JsonReader reader, JsonObject json) throws JsonException {
        super.jsonDeserialize(reader, json);
        scheduleId = getScheduledEventId(json, "XID");
    }
}
