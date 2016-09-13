/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.types.NumericValue;
import com.serotonin.mango.view.ImplDefinition;

/**
 *  
 */
@JsonRemoteEntity
public class AnalogGraphicComponent extends ImageSetComponent {
    public static ImplDefinition DEFINITION = new ImplDefinition("analogGraphic", "ANALOG_GRAPHIC",
            "graphic.analogGraphic", new int[] { DataTypes.NUMERIC });

    @JsonRemoteProperty
    private double min;
    @JsonRemoteProperty
    private double max;

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    @Override
    public ImplDefinition definition() {
        return DEFINITION;
    }

    @Override
    public String getImage(PointValueTime pointValue) {
        if (imageSet == null)
            // Image set not loaded?
            return "imageSetNotLoaded";

        if (pointValue == null || !(pointValue.getValue() instanceof NumericValue) || imageSet.getImageCount() == 1)
            return imageSet.getImageFilename(0);

        double dvalue = pointValue.getDoubleValue();

        int index = (int) ((dvalue - min) / (max - min) * imageSet.getImageCount());
        if (index < 0)
            index = 0;
        if (index >= imageSet.getImageCount())
            index = imageSet.getImageCount() - 1;

        return imageSet.getImageFilename(index);
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
        out.writeDouble(min);
        out.writeDouble(max);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            min = in.readDouble();
            max = in.readDouble();
        }
    }
}
