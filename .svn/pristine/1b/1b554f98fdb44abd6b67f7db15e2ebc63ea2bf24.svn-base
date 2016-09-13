/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.graphic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.serotonin.mango.Common;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.view.DynamicImage;
import com.serotonin.mango.view.ImplDefinition;
import com.serotonin.util.SerializationHelper;

/**
 *  
 */
@Deprecated
// Use ViewComponent instead
public class DynamicImageRenderer extends BaseGraphicRenderer {
    private static ImplDefinition definition = new ImplDefinition("graphicRendererDynamicImage", "DYNAMIC_IMAGE",
            "graphic.dynamicImage", new int[] { DataTypes.NUMERIC });

    public static ImplDefinition getDefinition() {
        return definition;
    }

    public String getTypeName() {
        return definition.getName();
    }

    public ImplDefinition getDef() {
        return definition;
    }

    private DynamicImage dynamicImage;
    private boolean displayText;
    private double min;
    private double max;

    public DynamicImageRenderer(DynamicImage dynamicImage, double min, double max, boolean displayText) {
        this.dynamicImage = dynamicImage;
        this.min = min;
        this.max = max;
        this.displayText = displayText;
    }

    public DynamicImage getDynamicImage() {
        return dynamicImage;
    }

    public String getImage() {
        return dynamicImage.getImageFilename();
    }

    public double getProportion(PointValueTime pointValue) {
        if (pointValue == null || pointValue.getValue() == null)
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
        return dynamicImage.getHeight();
    }

    public int getWidth() {
        return dynamicImage.getWidth();
    }

    public boolean isDisplayText() {
        return displayText;
    }

    public int getTextX() {
        return dynamicImage.getTextX();
    }

    public int getTextY() {
        return dynamicImage.getTextY();
    }

    public String getDynamicImageId() {
        return dynamicImage.getId();
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
}
