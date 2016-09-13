/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.permission;

import java.util.Map;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonSerializable;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.util.ExportCodes;
import com.serotonin.mango.util.LocalizableJsonException;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.util.StringUtils;

/**
 *  
 * 
 */
@JsonRemoteEntity
public class DataPointAccess implements JsonSerializable {
    public static final int READ = 1;
    public static final int SET = 2;

    private static final ExportCodes ACCESS_CODES = new ExportCodes();
    static {
        ACCESS_CODES.addElement(READ, "READ", "common.access.read");
        ACCESS_CODES.addElement(SET, "SET", "common.access.set");
    }

    private int dataPointId;
    private int permission;

    public int getDataPointId() {
        return dataPointId;
    }

    public void setDataPointId(int dataPointId) {
        this.dataPointId = dataPointId;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    @Override
    public void jsonDeserialize(JsonReader reader, JsonObject json) throws JsonException {
        String text = json.getString("dataPointXid");
        if (StringUtils.isEmpty(text))
            throw new LocalizableJsonException("emport.error.permission.missing", "dataPointXid");

        DataPointVO dp = new DataPointDao().getDataPoint(text);
        if (dp == null)
            throw new LocalizableJsonException("emport.error.missingPoint", text);
        dataPointId = dp.getId();

        text = json.getString("permission");
        if (StringUtils.isEmpty(text))
            throw new LocalizableJsonException("emport.error.missing", "permission", ACCESS_CODES.getCodeList());
        permission = ACCESS_CODES.getId(text);
        if (permission == -1)
            throw new LocalizableJsonException("emport.error.invalid", "permission", text, ACCESS_CODES.getCodeList());
    }

    @Override
    public void jsonSerialize(Map<String, Object> map) {
        map.put("dataPointXid", new DataPointDao().getDataPoint(dataPointId).getXid());
        map.put("permission", ACCESS_CODES.getCode(permission));
    }
}
