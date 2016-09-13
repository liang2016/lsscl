/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.view.ImplDefinition;

@JsonRemoteEntity
public class MultistateRenderer extends BaseTextRenderer {
    private static ImplDefinition definition = new ImplDefinition("textRendererMultistate", "MULTISTATE",
            "textRenderer.multistate", new int[] { DataTypes.MULTISTATE });

    public static ImplDefinition getDefinition() {
        return definition;
    }

    public String getTypeName() {
        return definition.getName();
    }

    public ImplDefinition getDef() {
        return definition;
    }

    @JsonRemoteProperty(innerType = MultistateValue.class)
    private List<MultistateValue> multistateValues = new ArrayList<MultistateValue>();

    public void addMultistateValue(int key, String text, String colour) {
        multistateValues.add(new MultistateValue(key, text, colour));
    }

    public List<MultistateValue> getMultistateValues() {
        return multistateValues;
    }

    public void setMultistateValues(List<MultistateValue> multistateValues) {
        this.multistateValues = multistateValues;
    }

    @Override
    protected String getTextImpl(MangoValue value, int hint) {
        if (!(value instanceof com.serotonin.mango.rt.dataImage.types.MultistateValue))
            return null;
        return getText(value.getIntegerValue(), hint);
    }

    @Override
    public String getText(int value, int hint) {
        if (hint == HINT_RAW)
            return Integer.toString(value);

        MultistateValue mv = getMultistateValue(value);
        if (mv == null)
            return Integer.toString(value);
        return mv.getText();
    }

    @Override
    protected String getColourImpl(MangoValue value) {
        if (!(value instanceof com.serotonin.mango.rt.dataImage.types.MultistateValue))
            return null;
        return getColour(value.getIntegerValue());
    }

    @Override
    public String getColour(int value) {
        MultistateValue mv = getMultistateValue(value);
        if (mv == null)
            return null;
        return mv.getColour();
    }

    private MultistateValue getMultistateValue(int value) {
        for (MultistateValue mv : multistateValues) {
            if (mv.getKey() == value)
                return mv;
        }
        return null;
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
        out.writeObject(multistateValues);
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            multistateValues = (List<MultistateValue>) in.readObject();
        }
    }
}
