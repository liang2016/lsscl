/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.galil;

import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataImage.types.NumericValue;
import com.serotonin.mango.vo.dataSource.galil.InputPointTypeVO;
import com.serotonin.web.i18n.LocalizableException;

/**
 *  
 */
public class InputPointTypeRT extends PointTypeRT {
    private final InputPointTypeVO vo;

    public InputPointTypeRT(InputPointTypeVO vo) {
        super(vo);
        this.vo = vo;
    }

    @Override
    public String getPollRequestImpl() {
        if (vo.getDataTypeId() == DataTypes.BINARY)
            return "MG @IN[" + vo.getInputId() + "]";
        return "MG @AN[" + vo.getInputId() + "]";
    }

    @Override
    public MangoValue parsePollResponse(String data, String pointName) throws LocalizableException {
        int dataTypeId = vo.getDataTypeId();
        MangoValue value = parseValue(data, dataTypeId, pointName);

        if (dataTypeId == DataTypes.NUMERIC)
            value = new NumericValue(rawToEngineeringUnits(value.getDoubleValue(), vo.getScaleRawLow(), vo
                    .getScaleRawHigh(), vo.getScaleEngLow(), vo.getScaleEngHigh()));

        return value;
    }

    @Override
    protected String getSetRequestImpl(MangoValue value) {
        return null;
    }

    @Override
    public MangoValue parseSetResponse(String data) {
        return null;
    }
}
