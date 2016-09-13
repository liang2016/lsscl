/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.ResourceBundle;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.Common;
import com.serotonin.mango.util.LocalizableJsonException;
import com.serotonin.mango.view.ImplDefinition;

/**
 *  
 */
@JsonRemoteEntity
public class ImageChartComponent extends CompoundComponent {
    public static ImplDefinition DEFINITION = new ImplDefinition("imageChart", "IMAGE_CHART", "graphic.imageChart",
            null);

    public static final String POINT_1 = "point1";
    public static final String POINT_2 = "point2";
    public static final String POINT_3 = "point3";
    public static final String POINT_4 = "point4";
    public static final String POINT_5 = "point5";
    public static final String POINT_6 = "point6";
    public static final String POINT_7 = "point7";
    public static final String POINT_8 = "point8";
    public static final String POINT_9 = "point9";
    public static final String POINT_10 = "point10";

    @JsonRemoteProperty
    private int width = 500;
    @JsonRemoteProperty
    private int height = 300;
    private int durationType = Common.TimePeriods.DAYS;
    @JsonRemoteProperty
    private int durationPeriods = 1;

    public ImageChartComponent() {
        initialize();
    }

    @Override
    protected void initialize() {
        addChild(POINT_1, "graphic.imageChart." + POINT_1, new SimplePointComponent(), null);
        addChild(POINT_2, "graphic.imageChart." + POINT_2, new SimplePointComponent(), null);
        addChild(POINT_3, "graphic.imageChart." + POINT_3, new SimplePointComponent(), null);
        addChild(POINT_4, "graphic.imageChart." + POINT_4, new SimplePointComponent(), null);
        addChild(POINT_5, "graphic.imageChart." + POINT_5, new SimplePointComponent(), null);
        addChild(POINT_6, "graphic.imageChart." + POINT_6, new SimplePointComponent(), null);
        addChild(POINT_7, "graphic.imageChart." + POINT_7, new SimplePointComponent(), null);
        addChild(POINT_8, "graphic.imageChart." + POINT_8, new SimplePointComponent(), null);
        addChild(POINT_9, "graphic.imageChart." + POINT_9, new SimplePointComponent(), null);
        addChild(POINT_10, "graphic.imageChart." + POINT_10, new SimplePointComponent(), null);
    }

    @Override
    public boolean hasInfo() {
        return true;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDurationType() {
        return durationType;
    }

    public void setDurationType(int durationType) {
        this.durationType = durationType;
    }

    public int getDurationPeriods() {
        return durationPeriods;
    }

    public void setDurationPeriods(int durationPeriods) {
        this.durationPeriods = durationPeriods;
    }

    @Override
    public ImplDefinition definition() {
        return DEFINITION;
    }

    @Override
    public String getStaticContent() {
        return null;
    }

    @Override
    public boolean isDisplayImageChart() {
        return false;
    }

    @Override
    public String getImageChartData(ResourceBundle bundle) {
        return generateImageChartData(bundle, Common.getMillis(durationType, durationPeriods), width, height, POINT_1,
                POINT_2, POINT_3, POINT_4, POINT_5, POINT_6, POINT_7, POINT_8, POINT_9, POINT_10);
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
        out.writeInt(width);
        out.writeInt(height);
        out.writeInt(durationType);
        out.writeInt(durationPeriods);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            width = in.readInt();
            height = in.readInt();
            durationType = in.readInt();
            durationPeriods = in.readInt();
        }
    }

    @Override
    public void jsonDeserialize(JsonReader reader, JsonObject json) throws JsonException {
        super.jsonDeserialize(reader, json);

        String text = json.getString("durationType");
        if (text == null)
            throw new LocalizableJsonException("emport.error.chart.missing", "durationType", Common.TIME_PERIOD_CODES
                    .getCodeList());

        durationType = Common.TIME_PERIOD_CODES.getId(text);
        if (durationType == -1)
            throw new LocalizableJsonException("emport.error.chart.invalid", "durationType", text,
                    Common.TIME_PERIOD_CODES.getCodeList());
    }

    @Override
    public void jsonSerialize(Map<String, Object> map) {
        super.jsonSerialize(map);

        map.put("durationType", Common.TIME_PERIOD_CODES.getCode(durationType));
    }
}
