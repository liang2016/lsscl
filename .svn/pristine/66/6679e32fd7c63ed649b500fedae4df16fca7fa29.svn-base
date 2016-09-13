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
import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.json.JsonValue;
import com.serotonin.mango.Common;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.types.NumericValue;
import com.serotonin.mango.util.LocalizableJsonException;
import com.serotonin.mango.view.DynamicImage;
import com.serotonin.mango.view.ImplDefinition;
import com.serotonin.util.SerializationHelper;

/**
 *  
 */
@JsonRemoteEntity
public class DynamicGraphicComponent extends PointComponent {
    public static ImplDefinition DEFINITION = new ImplDefinition("dynamicGraphic", "DYNAMIC_GRAPHIC",
            "graphic.dynamicGraphic", new int[] { DataTypes.NUMERIC });

    private DynamicImage dynamicImage;
    @JsonRemoteProperty
    private boolean displayText;
    @JsonRemoteProperty
    private double min;
    @JsonRemoteProperty
    private double max;

    public DynamicImage tgetDynamicImage() {
        return dynamicImage;
    }

    public void tsetDynamicImage(DynamicImage dynamicImage) {
        this.dynamicImage = dynamicImage;
    }

    public boolean isDisplayText() {
        return displayText;
    }

    public void setDisplayText(boolean displayText) {
        this.displayText = displayText;
    }

    public double getMax() {
        return max;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMin() {
        return min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    @Override
    public ImplDefinition definition() {
        return DEFINITION;
    }

    @Override
    public String snippetName() {
        return "dynamicImageContent";
    }

    public String getImage() {
        if (dynamicImage == null)
            return null;
        return dynamicImage.getImageFilename();
    }

    public double getProportion(PointValueTime pointValue) {
        if (pointValue == null || !(pointValue.getValue() instanceof NumericValue))
            return 0;

        double dvalue = pointValue.getDoubleValue();
        double proportion = (dvalue - min) / (max - min);
        if (proportion > 1)
            return 1;
        if (proportion < 0)
            return 0;
        return proportion;
    }

    public int getHeight() {
        if (dynamicImage == null)
            return 0;
        return dynamicImage.getHeight();
    }

    public int getWidth() {
        if (dynamicImage == null)
            return 0;
        return dynamicImage.getWidth();
    }

    public int getTextX() {
        if (dynamicImage == null)
            return 0;
        return dynamicImage.getTextX();
    }

    public int getTextY() {
        if (dynamicImage == null)
            return 0;
        return dynamicImage.getTextY();
    }

    public String getDynamicImageId() {
        if (dynamicImage == null)
            return null;
        return dynamicImage.getId();
    }

    @Override
    public void addDataToModel(Map<String, Object> model, PointValueTime pointValue) {
        model.put("proportion", getProportion(pointValue));
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
        if (dynamicImage == null)
            SerializationHelper.writeSafeUTF(out, null);
        else
            SerializationHelper.writeSafeUTF(out, dynamicImage.getId());
        out.writeDouble(min);
        out.writeDouble(max);
        out.writeBoolean(displayText);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            dynamicImage = Common.ctx.getDynamicImage(SerializationHelper.readSafeUTF(in));
            min = in.readDouble();
            max = in.readDouble();
            displayText = in.readBoolean();
        }
    }

    @Override
    public void jsonDeserialize(JsonReader reader, JsonObject json) throws JsonException {
        super.jsonDeserialize(reader, json);

        JsonValue jsonImageId = json.getValue("dynamicImage");
        if (jsonImageId != null) {
            if (jsonImageId.isNull())
                dynamicImage = null;
            else {
                String id = jsonImageId.toJsonString().getValue();
                dynamicImage = Common.ctx.getDynamicImage(id);
                if (dynamicImage == null)
                    throw new LocalizableJsonException("emport.error.component.unknownDynamicImage", id, Common.ctx
                            .getDynamicImageIds());
            }
        }
    }

    @Override
    public void jsonSerialize(Map<String, Object> map) {
        super.jsonSerialize(map);

        if (dynamicImage == null)
            map.put("dynamicImage", null);
        else
            map.put("dynamicImage", dynamicImage.getId());
    }
}
