/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.graphic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.serotonin.mango.Common;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.view.ImageSet;
import com.serotonin.util.SerializationHelper;

@Deprecated
// Use ViewComponent instead
abstract public class ImageSetRenderer extends BaseGraphicRenderer {
    abstract public String getImage(PointValueTime pointValue);

    protected ImageSet imageSet;
    private boolean displayText;

    public ImageSetRenderer(ImageSet imageSet, boolean displayText) {
        this.imageSet = imageSet;
        this.displayText = displayText;
    }

    public ImageSet getImageSet() {
        return imageSet;
    }

    public String getImage() {
        return getImage(null);
    }

    public int getHeight() {
        return imageSet.getHeight();
    }

    public int getWidth() {
        return imageSet.getWidth();
    }

    public boolean isDisplayText() {
        return displayText;
    }

    public int getTextX() {
        return imageSet.getTextX();
    }

    public int getTextY() {
        return imageSet.getTextY();
    }

    public String getImageSetId() {
        return imageSet.getId();
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
        SerializationHelper.writeSafeUTF(out, imageSet.getId());
        out.writeBoolean(displayText);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            imageSet = Common.ctx.getImageSet(SerializationHelper.readSafeUTF(in));
            displayText = in.readBoolean();
        }
    }
}
