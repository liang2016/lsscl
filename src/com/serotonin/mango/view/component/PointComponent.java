/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.util.SerializationHelper;
import com.serotonin.util.StringUtils;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
abstract public class PointComponent extends ViewComponent {
    private DataPointVO dataPoint;
    @JsonRemoteProperty
    private String nameOverride;
    @JsonRemoteProperty
    private boolean settableOverride;
    @JsonRemoteProperty
    private String bkgdColorOverride;
    @JsonRemoteProperty
    private boolean displayControls;
    @JsonRemoteProperty
    private int fontSize=12;
    @JsonRemoteProperty
    private String fontWeight;

    // Runtime attributes
    private boolean valid;
    private boolean visible;

    @Override
    public boolean isPointComponent() {
        return true;
    }

    abstract public void addDataToModel(Map<String, Object> model, PointValueTime pointValue);

    abstract public String snippetName();

    @Override
    public void validateDataPoint(User user, boolean makeReadOnly) {
        if (dataPoint == null) {
            valid = false;
            visible = false;
        }
        else {
            visible = Permissions.hasDataPointReadPermission(user, dataPoint);
            valid = definition().supports(dataPoint.getPointLocator().getDataTypeId());
        }

        if (makeReadOnly)
            settableOverride = false;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public boolean containsValidVisibleDataPoint(int dataPointId) {
        if (!valid || !visible)
            return false;

        return dataPoint.getId() == dataPointId;
    }

    public int[] getSupportedDataTypes() {
        return definition().getSupportedDataTypes();
    }

    public String getTypeName() {
        return definition().getName();
    }

    public LocalizableMessage getDisplayName() {
        return new LocalizableMessage(definition().getNameKey());
    }

    public String getName() {
        if (!StringUtils.isEmpty(nameOverride))
            return nameOverride;
        if (dataPoint == null)
            return "(unknown)";
        return dataPoint.getName();
    }

    public boolean isSettable() {
        if (dataPoint == null)
            return false;
        if (!dataPoint.getPointLocator().isSettable())
            return false;
        return settableOverride;
    }

    public boolean isChartRenderer() {
        if (dataPoint == null)
            return false;
        return dataPoint.getChartRenderer() != null;
    }

    public DataPointVO tgetDataPoint() {
        return dataPoint;
    }

    public void tsetDataPoint(DataPointVO dataPoint) {
        this.dataPoint = dataPoint;
    }

    public int getDataPointId() {
        if (dataPoint == null)
            return 0;
        return dataPoint.getId();
    }

    public String getNameOverride() {
        return nameOverride;
    }

    public void setNameOverride(String nameOverride) {
        this.nameOverride = nameOverride;
    }

    public boolean isSettableOverride() {
        return settableOverride;
    }

    public void setSettableOverride(boolean settableOverride) {
        this.settableOverride = settableOverride;
    }

    public String getBkgdColorOverride() {
        return bkgdColorOverride;
    }

    public void setBkgdColorOverride(String bkgdColorOverride) {
        this.bkgdColorOverride = bkgdColorOverride;
    }

    public boolean isDisplayControls() {
        return displayControls;
    }

    public void setDisplayControls(boolean displayControls) {
        this.displayControls = displayControls;
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

        writeDataPoint(out, dataPoint);
        SerializationHelper.writeSafeUTF(out, nameOverride);
        out.writeBoolean(settableOverride);
        SerializationHelper.writeSafeUTF(out, bkgdColorOverride);
        out.writeBoolean(displayControls);
        out.writeInt(fontSize);
        SerializationHelper.writeSafeUTF(out, fontWeight);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            dataPoint = readDataPoint(in);
            nameOverride = SerializationHelper.readSafeUTF(in);
            settableOverride = in.readBoolean();
            bkgdColorOverride = SerializationHelper.readSafeUTF(in);
            displayControls = in.readBoolean();
            fontSize=in.readInt();
            fontWeight=SerializationHelper.readSafeUTF(in);
        }
    }

    @Override
    public void jsonDeserialize(JsonReader reader, JsonObject json) throws JsonException {
        super.jsonDeserialize(reader, json);
        jsonDeserializeDataPoint(json.getValue("dataPointXid"), this);
    }

    @Override
    public void jsonSerialize(Map<String, Object> map) {
        super.jsonSerialize(map);
        jsonSerializeDataPoint(map, "dataPointXid", this);
    }

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontWeight() {
		return fontWeight;
	}

	public void setFontWeight(String fontWeight) {
		this.fontWeight = fontWeight;
	}
}
