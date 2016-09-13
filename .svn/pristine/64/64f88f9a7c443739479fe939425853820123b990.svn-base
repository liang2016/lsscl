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
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.util.LocalizableJsonException;
import com.serotonin.mango.vo.User;

@JsonRemoteEntity
public class UserEntry extends EmailRecipient {
    private int userId;
    private User user;

    @Override
    public int getRecipientType() {
        return EmailRecipient.TYPE_USER;
    }

    @Override
    public int getReferenceId() {
        return userId;
    }

    @Override
    public String getReferenceAddress() {
        return null;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void appendAddresses(Set<String> addresses, DateTime sendTime) {
        appendAllAddresses(addresses);
    }

    @Override
    public void appendAllAddresses(Set<String> addresses) {
        if (user == null)
            return;
        if (!user.isDisabled())
            addresses.add(user.getEmail());
    }

    @Override
    public String toString() {
        if (user == null)
            return "userId=" + userId;
        return user.getUsername();
    }

    @Override
    public void jsonSerialize(Map<String, Object> map) {
        super.jsonSerialize(map);
        if (user == null)
            user = new UserDao().getUser(userId);
        map.put("username", user.getUsername());
    }

    @Override
    public void jsonDeserialize(JsonReader reader, JsonObject json) throws JsonException {
        super.jsonDeserialize(reader, json);

        String username = json.getString("username");
        if (username == null)
            throw new LocalizableJsonException("emport.error.recipient.missing.reference", "username");

        user = new UserDao().getUser(username);
        if (user == null)
            throw new LocalizableJsonException("emport.error.recipient.invalid.reference", "username", username);

        userId = user.getId();
    }
}
