/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.dataSource;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.ResourceBundle;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.util.LocalizableJsonException;
import com.serotonin.web.i18n.LocalizableMessage;

abstract public class AbstractPointLocatorVO implements PointLocatorVO {
    public LocalizableMessage getDataTypeMessage() {
        return DataTypes.getDataTypeMessage(getDataTypeId());
    }

    protected String getMessage(ResourceBundle bundle, String key, Object... args) {
        return new LocalizableMessage(key, args).getLocalizedMessage(bundle);
    }

    //
    // /
    // / Serialization
    // /
    //
    private static final long serialVersionUID = -1;
    private static final int version = 1;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(version);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        in.readInt(); // Read the version. Value is currently not used.
    }

    protected void serializeDataType(Map<String, Object> map) {
        map.put("dataType", DataTypes.CODES.getCode(getDataTypeId()));
    }

    protected Integer deserializeDataType(JsonObject json, int... excludeIds) throws JsonException {
        String text = json.getString("dataType");
        if (text == null)
            return null;

        int dataType = DataTypes.CODES.getId(text);
        if (!DataTypes.CODES.isValidId(dataType, excludeIds))
            throw new LocalizableJsonException("emport.error.invalid", "dataType", text,
                    DataTypes.CODES.getCodeList(excludeIds));

        return dataType;
    }

    /**
     * Defaults to returning null. Override to return something else.
     */
    @Override
    public DataPointSaveHandler getDataPointSaveHandler() {
        return null;
    }

    /**
     * Defaults to returning false. Override to return something else.
     */
    @Override
    public boolean isRelinquishable() {
        return false;
    }
}
