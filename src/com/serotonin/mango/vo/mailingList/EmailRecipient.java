/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.mailingList;

import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonSerializable;
import com.serotonin.mango.util.ExportCodes;

@JsonRemoteEntity(typeFactory = EmailRecipientFactory.class)
abstract public class EmailRecipient implements JsonSerializable {
    public static final int TYPE_MAILING_LIST = 1;
    public static final int TYPE_USER = 2;
    public static final int TYPE_ADDRESS = 3;

    public static final ExportCodes TYPE_CODES = new ExportCodes();
    static {
        TYPE_CODES.addElement(TYPE_MAILING_LIST, "MAILING_LIST", "mailingLists.mailingList");
        TYPE_CODES.addElement(TYPE_USER, "USER", "mailingLists.emailAddress");
        TYPE_CODES.addElement(TYPE_ADDRESS, "ADDRESS", "common.user");
    }

    abstract public int getRecipientType();

    abstract public void appendAddresses(Set<String> addresses, DateTime sendTime);

    abstract public void appendAllAddresses(Set<String> addresses);

    abstract public int getReferenceId();

    abstract public String getReferenceAddress();

    /**
     * @throws JsonException
     */
    @Override
    public void jsonDeserialize(JsonReader reader, JsonObject json) throws JsonException {
        // no op. The type value is used by the factory.
    }

    @Override
    public void jsonSerialize(Map<String, Object> map) {
        map.put("recipientType", TYPE_CODES.getCode(getRecipientType()));
    }
}
