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
import com.serotonin.mango.rt.dataImage.types.BinaryValue;
import com.serotonin.mango.view.ImplDefinition;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
@JsonRemoteEntity
public class BinaryGraphicComponent extends ImageSetComponent {
    public static ImplDefinition DEFINITION = new ImplDefinition("binaryGraphic", "BINARY_GRAPHIC",
            "graphic.binaryGraphic", new int[] { DataTypes.BINARY });

    @JsonRemoteProperty(alias = "zeroImageIndex")
    private int zeroImage;
    @JsonRemoteProperty(alias = "oneImageIndex")
    private int oneImage;

    public int getZeroImage() {
        return zeroImage;
    }

    public void setZeroImage(int zeroImage) {
        this.zeroImage = zeroImage;
    }

    public int getOneImage() {
        return oneImage;
    }

    public void setOneImage(int oneImage) {
        this.oneImage = oneImage;
    }

    @Override
    public ImplDefinition definition() {
        return DEFINITION;
    }

    @Override
    public String getImage(PointValueTime pointValue) {
        boolean bvalue = false;
        if (pointValue != null && pointValue.getValue() instanceof BinaryValue)
            bvalue = pointValue.getBooleanValue();
        return imageSet.getImageFilename(bvalue ? oneImage : zeroImage);
    }

    @Override
    public void validate(DwrResponseI18n response) {
        super.validate(response);

        if (zeroImage < 0)
            response.addMessage("zeroImageIndex", new LocalizableMessage("validate.cannotBeNegative"));
        if (oneImage < 0)
            response.addMessage("oneImageIndex", new LocalizableMessage("validate.cannotBeNegative"));

        if (imageSet != null) {
            if (zeroImage >= imageSet.getImageCount())
                response.addMessage("zeroImageIndex", new LocalizableMessage("emport.error.component.imageIndex",
                        zeroImage, imageSet.getId(), imageSet.getImageCount() - 1));
            if (oneImage >= imageSet.getImageCount())
                response.addMessage("oneImageIndex", new LocalizableMessage("emport.error.component.imageIndex",
                        oneImage, imageSet.getId(), imageSet.getImageCount() - 1));
        }
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
        out.writeInt(zeroImage);
        out.writeInt(oneImage);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            zeroImage = in.readInt();
            oneImage = in.readInt();
        }
    }
}
