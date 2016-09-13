/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.graphic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.view.ImageSet;
import com.serotonin.mango.view.ImplDefinition;

@Deprecated
// Use ViewComponent instead
public class AnalogImageSetRenderer extends ImageSetRenderer {
    private static ImplDefinition definition = new ImplDefinition("graphicRendererAnalogImage", "ANALOG_IMAGE",
            "graphic.multistateImage", new int[] { DataTypes.NUMERIC });

    public static ImplDefinition getDefinition() {
        return definition;
    }

    public String getTypeName() {
        return definition.getName();
    }

    public ImplDefinition getDef() {
        return definition;
    }

    private double min;
    private double max;

    public AnalogImageSetRenderer(ImageSet imageSet, double min, double max, boolean displayText) {
        super(imageSet, displayText);
        this.min = min;
        this.max = max;
    }

    @Override
    public String getImage(PointValueTime pointValue) {
        if (imageSet == null)
            // Image set not loaded?
            return "imageSetNotLoaded";

        if (pointValue == null || pointValue.getValue() == null || imageSet.getImageCount() == 1)
            return imageSet.getImageFilename(0);

        double dvalue = pointValue.getDoubleValue();

        int index = (int) ((dvalue - min) / (max - min) * imageSet.getImageCount());
        if (index < 0)
            index = 0;
        if (index >= imageSet.getImageCount())
            index = imageSet.getImageCount() - 1;

        return imageSet.getImageFilename(index);
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
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
