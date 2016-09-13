/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataImage.types.NumericValue;
import com.serotonin.mango.view.ImplDefinition;
import com.serotonin.util.SerializationHelper;

@JsonRemoteEntity
public class TimeRenderer extends BaseTextRenderer {
    private static ImplDefinition definition = new ImplDefinition("textRendererTime", "TIME", "textRenderer.time",
            new int[] { DataTypes.NUMERIC });

    public static ImplDefinition getDefinition() {
        return definition;
    }

    public String getTypeName() {
        return definition.getName();
    }

    public ImplDefinition getDef() {
        return definition;
    }

    @JsonRemoteProperty
    private String format;
    @JsonRemoteProperty
    private int conversionExponent;

    private SimpleDateFormat formatInstance;

    public TimeRenderer() {
        // no op
    }

    public TimeRenderer(String format, int conversionExponent) {
        setFormat(format);
        this.conversionExponent = conversionExponent;
    }

    private void createFormatInstance() {
        formatInstance = new SimpleDateFormat(format);
    }

    @Override
    protected String getTextImpl(MangoValue value, int hint) {
        if (!(value instanceof NumericValue))
            return null;
        return getText((long) value.getDoubleValue(), hint);
    }

    @Override
    public String getText(double value, int hint) {
        long l = (long) value;

        if (hint == HINT_RAW || hint == HINT_SPECIFIC)
            return new Long(l).toString();

        l *= (long) Math.pow(10, conversionExponent);
        return formatInstance.format(new Date(l));
    }

    @Override
    protected String getColourImpl(MangoValue value) {
        return null;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
        createFormatInstance();
    }

    public int getConversionExponent() {
        return conversionExponent;
    }

    public void setConversionExponent(int conversionExponent) {
        this.conversionExponent = conversionExponent;
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
        SerializationHelper.writeSafeUTF(out, format);
        out.writeInt(conversionExponent);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            format = SerializationHelper.readSafeUTF(in);
            createFormatInstance();
            conversionExponent = in.readInt();
        }
    }
}
