/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view;

import java.util.Map;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonSerializable;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.util.ExportCodes;
import com.serotonin.mango.util.LocalizableJsonException;
import com.serotonin.mango.vo.User;
import com.serotonin.util.StringUtils;

/**
 *  
 */
@JsonRemoteEntity
public class ShareUser implements JsonSerializable {
    public static final int ACCESS_NONE = 0;
    public static final int ACCESS_READ = 1;
    public static final int ACCESS_SET = 2;
    public static final int ACCESS_OWNER = 3;

    public static final ExportCodes ACCESS_CODES = new ExportCodes();
    static {
        ACCESS_CODES.addElement(ACCESS_NONE, "NONE", "common.access.none");
        ACCESS_CODES.addElement(ACCESS_READ, "READ", "common.access.read");
        ACCESS_CODES.addElement(ACCESS_SET, "SET", "common.access.set");
    }

    private int userId;
    private int accessType;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAccessType() {
        return accessType;
    }

    public void setAccessType(int accessType) {
        this.accessType = accessType;
    }

    @Override
    public void jsonDeserialize(JsonReader reader, JsonObject json) throws JsonException {
        String text = json.getString("user");
        if (StringUtils.isEmpty(text))
            throw new LocalizableJsonException("emport.error.viewShare.missing", "user");
        User user = new UserDao().getUser(text);
        if (user == null)
            throw new LocalizableJsonException("emport.error.missingUser", text);
        userId = user.getId();

        text = json.getString("accessType");
        if (StringUtils.isEmpty(text))
            throw new LocalizableJsonException("emport.error.missing", "accessType", ACCESS_CODES
                    .getCodeList(ACCESS_OWNER));
        accessType = ACCESS_CODES.getId(text, ACCESS_OWNER);
        if (accessType == -1)
            throw new LocalizableJsonException("emport.error.invalid", "permission", text, ACCESS_CODES
                    .getCodeList(ACCESS_OWNER));
    }

    @Override
    public void jsonSerialize(Map<String, Object> map) {
        map.put("user", new UserDao().getUser(userId).getUsername());
        map.put("accessType", ACCESS_CODES.getCode(accessType));
    }
}
