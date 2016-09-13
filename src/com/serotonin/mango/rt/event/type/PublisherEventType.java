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
import com.serotonin.mango.db.dao.PublisherDao;
import com.serotonin.mango.vo.publish.PublisherVO;

/**
 *  
 */
@JsonRemoteEntity
public class PublisherEventType extends EventType {
    private int publisherId;
    private int publisherEventTypeId;

    public PublisherEventType() {
        // Required for reflection.
    }

    public PublisherEventType(int publisherId, int publisherEventTypeId) {
        this.publisherId = publisherId;
        this.publisherEventTypeId = publisherEventTypeId;
    }

    @Override
    public int getEventSourceId() {
        return EventType.EventSources.PUBLISHER;
    }

    public int getPublisherEventTypeId() {
        return publisherEventTypeId;
    }

    @Override
    public int getPublisherId() {
        return publisherId;
    }

    @Override
    public String toString() {
        return "PublisherEventType(publisherId=" + publisherId + ", eventTypeId=" + publisherEventTypeId + ")";
    }

    @Override
    public int getDuplicateHandling() {
        return EventType.DuplicateHandling.IGNORE;
    }

    @Override
    public int getReferenceId1() {
        return publisherId;
    }

    @Override
    public int getReferenceId2() {
        return publisherEventTypeId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + publisherEventTypeId;
        result = prime * result + publisherId;
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
        PublisherEventType other = (PublisherEventType) obj;
        if (publisherEventTypeId != other.publisherEventTypeId)
            return false;
        if (publisherId != other.publisherId)
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
        PublisherVO<?> pub = new PublisherDao().getPublisher(publisherId);
        map.put("XID", pub.getXid());
        map.put("publisherEventTypeId", pub.getEventCodes().getCode(publisherEventTypeId));
    }

    @Override
    public void jsonDeserialize(JsonReader reader, JsonObject json) throws JsonException {
        super.jsonDeserialize(reader, json);
        PublisherVO<?> pb = getPublisher(json, "XID");
        publisherId = pb.getId();
        publisherEventTypeId = getInt(json, "publisherEventTypeId", pb.getEventCodes());
    }
}
