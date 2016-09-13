/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.galil;

import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataImage.types.NumericValue;
import com.serotonin.mango.vo.dataSource.galil.TellPositionPointTypeVO;
import com.serotonin.web.i18n.LocalizableException;

/**
 *  
 */
public class TellPositionPointTypeRT extends PointTypeRT {
    private final TellPositionPointTypeVO vo;

    public TellPositionPointTypeRT(TellPositionPointTypeVO vo) {
        super(vo);
        this.vo = vo;
    }

    @Override
    public String getPollRequestImpl() {
        return "TP" + vo.getAxis();
    }

    @Override
    public MangoValue parsePollResponse(String data, String pointName) throws LocalizableException {
        double value = parseValue(data, vo.getDataTypeId(), pointName).getDoubleValue();

        value = rawToEngineeringUnits(value, vo.getScaleRawLow(), vo.getScaleRawHigh(), vo.getScaleEngLow(), vo
                .getScaleEngHigh());

        if (vo.isRoundToInteger())
            value = Math.round(value);

        return new NumericValue(value);
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
