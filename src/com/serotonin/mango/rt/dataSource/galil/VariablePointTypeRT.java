/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.galil;

import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataImage.types.AlphanumericValue;
import com.serotonin.mango.rt.dataImage.types.BinaryValue;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataImage.types.MultistateValue;
import com.serotonin.mango.rt.dataImage.types.NumericValue;
import com.serotonin.mango.vo.dataSource.galil.VariablePointTypeVO;
import com.serotonin.web.i18n.LocalizableException;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
public class VariablePointTypeRT extends PointTypeRT {
    private final VariablePointTypeVO vo;

    public VariablePointTypeRT(VariablePointTypeVO vo) {
        super(vo);
        this.vo = vo;
    }

    @Override
    protected String getPollRequestImpl() {
        if (vo.getDataTypeId() == DataTypes.ALPHANUMERIC)
            return vo.getVariableName() + "={S6}";
        return vo.getVariableName() + "=";
    }

    @Override
    public MangoValue parsePollResponse(String data, String pointName) throws LocalizableException {
        if (vo.getDataTypeId() == DataTypes.ALPHANUMERIC)
            return new AlphanumericValue(data);

        try {
            double value = Double.parseDouble(data);

            if (vo.getDataTypeId() == DataTypes.BINARY)
                return new BinaryValue(value != 0);

            if (vo.getDataTypeId() == DataTypes.MULTISTATE)
                return new MultistateValue((int) value);

            // Must be numeric.
            return new NumericValue(value);
        }
        catch (NumberFormatException e) {
            throw new LocalizableException(new LocalizableMessage("event.galil.numericParse", data));
        }
    }

    @Override
    protected String getSetRequestImpl(MangoValue value) {
        StringBuilder data = new StringBuilder();
        data.append(vo.getVariableName()).append('=');

        if (vo.getDataTypeId() == DataTypes.BINARY)
            data.append(value.getBooleanValue() ? '1' : '0');
        else if (vo.getDataTypeId() == DataTypes.MULTISTATE)
            data.append(value.getIntegerValue());
        else if (vo.getDataTypeId() == DataTypes.NUMERIC)
            data.append(value.getDoubleValue());
        else
            data.append('"').append(value.getStringValue()).append('"');

        return data.toString();
    }

    @Override
    public MangoValue parseSetResponse(String data) throws LocalizableException {
        if (!"".equals(data))
            throw new LocalizableException(new LocalizableMessage("event.galil.unexpected", data));
        return null;
    }
}
