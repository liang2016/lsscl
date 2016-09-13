/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event.type;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonValue;
import com.serotonin.json.TypeFactory;
import com.serotonin.mango.rt.event.type.EventType.EventSources;
import com.serotonin.mango.util.LocalizableJsonException;

public class EventTypeFactory implements TypeFactory {
    @Override
    public Class<?> getType(JsonValue jsonValue) throws JsonException {
        if (jsonValue.isNull())
            throw new LocalizableJsonException("emport.error.eventType.null");

        JsonObject json = jsonValue.toJsonObject();

        String text = json.getString("sourceType");
        if (text == null)
            throw new LocalizableJsonException("emport.error.eventType.missing", "sourceType",
                    EventType.SOURCE_CODES.getCodeList());

        int source = EventType.SOURCE_CODES.getId(text);
        if (!EventType.SOURCE_CODES.isValidId(source))
            throw new LocalizableJsonException("emport.error.eventType.invalid", "sourceType", text,
                    EventType.SOURCE_CODES.getCodeList());

        if (source == EventSources.DATA_POINT)
            return DataPointEventType.class;
        if (source == EventSources.DATA_SOURCE)
            return DataSourceEventType.class;
        if (source == EventSources.SYSTEM)
            return SystemEventType.class;
        if (source == EventSources.COMPOUND)
            return CompoundDetectorEventType.class;
        if (source == EventSources.SCHEDULED)
            return ScheduledEventType.class;
        if (source == EventSources.PUBLISHER)
            return PublisherEventType.class;
        if (source == EventSources.AUDIT)
            return AuditEventType.class;
        if (source == EventSources.MAINTENANCE)
            return MaintenanceEventType.class;

        return null;
    }
}
