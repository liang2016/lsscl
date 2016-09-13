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
import com.serotonin.mango.view.ImplDefinition;
import com.serotonin.util.SerializationHelper;

/**
 *  
 */
@JsonRemoteEntity
public class SimplePointComponent extends PointComponent {
    public static ImplDefinition DEFINITION = new ImplDefinition("simple", "SIMPLE", "graphic.simple", new int[] {
            DataTypes.BINARY, DataTypes.MULTISTATE, DataTypes.NUMERIC, DataTypes.ALPHANUMERIC });

    @JsonRemoteProperty
    private boolean displayPointName=true;

    @JsonRemoteProperty
    private String styleAttribute;

    public boolean isDisplayPointName() {
        return displayPointName;
    }

    public void setDisplayPointName(boolean displayPointName) {
        this.displayPointName = displayPointName;
    }

    public String getStyleAttribute() {
        return styleAttribute;
    }

    public void setStyleAttribute(String styleAttribute) {
        this.styleAttribute = styleAttribute;
    }

    @Override
    public String snippetName() {
        return "basicContent";
    }

    @Override
    public void addDataToModel(Map<String, Object> model, PointValueTime pointValue) {
        model.put("displayPointName", displayPointName);
        model.put("styleAttribute", styleAttribute);
    }

    @Override
    public ImplDefinition definition() {
        return DEFINITION;
    }

    //
    // /
    // / Serialization
    // /
    //
    private static final long serialVersionUID = -1;
    private static final int version = 3;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(version);

        out.writeBoolean(displayPointName);
        SerializationHelper.writeSafeUTF(out, styleAttribute);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            displayPointName = false;
            styleAttribute = "";
        }
        else if (ver == 2) {
            displayPointName = in.readBoolean();
            styleAttribute = "";
        }
        else if (ver == 3) {
            displayPointName = in.readBoolean();
            styleAttribute = SerializationHelper.readSafeUTF(in);
        }
    }
}
