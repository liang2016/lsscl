/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.dataSource.spinwave;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataImage.types.BinaryValue;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataImage.types.NumericValue;
import com.serotonin.mango.view.conversion.Conversions;
import com.serotonin.spinwave.SwMessage;
import com.serotonin.spinwave.v1.SwMessageV1;

/**
 *  
 */
@JsonRemoteEntity
public class SpinwaveV1PointLocatorVO extends BaseSpinwavePointLocatorVO {
    public interface AttributeTypes {
        int TEMPURATURE = 1;
        int SET_POINT = 2;
        int BATTERY = 3;
        int OVERRIDE = 4;
    }

    public static String getAttributeDescription(int attributeId) {
        if (attributeId == AttributeTypes.TEMPURATURE)
            return "dsEdit.spinwave.v1Attr.temp";
        if (attributeId == AttributeTypes.SET_POINT)
            return "dsEdit.spinwave.v1Attr.setPoint";
        if (attributeId == AttributeTypes.BATTERY)
            return "dsEdit.spinwave.v1Attr.battery";
        if (attributeId == AttributeTypes.OVERRIDE)
            return "dsEdit.spinwave.v1Attr.override";
        return "Unknown";
    }

    public static int getAttributeDataType(int attributeId) {
        if (attributeId == AttributeTypes.OVERRIDE)
            return DataTypes.BINARY;
        return DataTypes.NUMERIC;
    }

    @Override
    public String getAttributeDescription() {
        return getAttributeDescription(getAttributeId());
    }

    public int getDataTypeId() {
        return getAttributeDataType(getAttributeId());
    }

    @Override
    public MangoValue getValue(SwMessage msg) {
        SwMessageV1 message = (SwMessageV1) msg;

        if (getAttributeId() == AttributeTypes.TEMPURATURE) {
            if (isConvertToCelsius())
                return new NumericValue(Conversions.fahrenheitToCelsius(message.getTemperature()));
            return new NumericValue(message.getTemperature());
        }

        if (getAttributeId() == AttributeTypes.SET_POINT) {
            if (isConvertToCelsius())
                return new NumericValue(Conversions.fahrenheitToCelsius(message.getSetPoint()));
            return new NumericValue(message.getSetPoint());
        }

        if (getAttributeId() == AttributeTypes.BATTERY)
            return new NumericValue(message.getBatteryVoltage());

        if (getAttributeId() == AttributeTypes.OVERRIDE)
            return new BinaryValue(message.isOverride());

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
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            // no op
        }
    }
}
