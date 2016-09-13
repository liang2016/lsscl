/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.types.ImageValue;
import com.serotonin.mango.view.ImplDefinition;

/**
 *  
 */
@JsonRemoteEntity
public class ThumbnailComponent extends PointComponent {
    public static ImplDefinition DEFINITION = new ImplDefinition("thumbnailImage", "THUMBNAIL",
            "graphic.thumbnailImage", new int[] { DataTypes.IMAGE });

    @JsonRemoteProperty
    private int scalePercent;

    public int getScalePercent() {
        return scalePercent;
    }

    public void setScalePercent(int scalePercent) {
        this.scalePercent = scalePercent;
    }

    @Override
    public String snippetName() {
        return "imageValueContent";
    }

    @Override
    public ImplDefinition definition() {
        return DEFINITION;
    }

    @Override
    public void addDataToModel(Map<String, Object> model, PointValueTime pointValue) {
        if (pointValue == null || pointValue.getValue() == null) {
            model.put("error", "common.noData");
            return;
        }

        if (!(pointValue.getValue() instanceof ImageValue)) {
            model.put("error", "common.thumb.invalidValue");
            return;
        }

        ImageValue imageValue = (ImageValue) pointValue.getValue();
        model.put("imageType", imageValue.getTypeExtension());
        model.put("scalePercent", scalePercent);
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

        out.writeInt(scalePercent);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1)
            scalePercent = in.readInt();
    }
}
