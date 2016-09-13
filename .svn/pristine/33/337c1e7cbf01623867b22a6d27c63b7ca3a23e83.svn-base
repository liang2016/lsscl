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
import com.serotonin.mango.util.LocalizableJsonException;
import com.serotonin.util.StringUtils;

@JsonRemoteEntity
public class AddressEntry extends EmailRecipient {
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void appendAddresses(Set<String> addresses, DateTime sendTime) {
        appendAllAddresses(addresses);
    }

    @Override
    public void appendAllAddresses(Set<String> addresses) {
        addresses.add(address);
    }

    @Override
    public int getRecipientType() {
        return EmailRecipient.TYPE_ADDRESS;
    }

    @Override
    public int getReferenceId() {
        return 0;
    }

    @Override
    public String getReferenceAddress() {
        return address;
    }

    @Override
    public String toString() {
        return address;
    }

    @Override
    public void jsonSerialize(Map<String, Object> map) {
        super.jsonSerialize(map);
        map.put("address", address);
    }

    @Override
    public void jsonDeserialize(JsonReader reader, JsonObject json) throws JsonException {
        super.jsonDeserialize(reader, json);

        address = json.getString("address");
        if (StringUtils.isEmpty(address))
            throw new LocalizableJsonException("emport.error.recipient.missing.reference", "address");
    }
}
