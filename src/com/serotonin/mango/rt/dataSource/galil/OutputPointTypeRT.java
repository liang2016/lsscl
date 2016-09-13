/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.galil;

import com.serotonin.mango.rt.dataImage.types.BinaryValue;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.vo.dataSource.galil.OutputPointTypeVO;
import com.serotonin.web.i18n.LocalizableException;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
public class OutputPointTypeRT extends PointTypeRT {
    private final OutputPointTypeVO vo;

    public OutputPointTypeRT(OutputPointTypeVO vo) {
        super(vo);
        this.vo = vo;
    }

    @Override
    public String getPollRequestImpl() {
        return "MG @OUT[" + vo.getOutputId() + "]";
    }

    @Override
    public MangoValue parsePollResponse(String data, String pointName) throws LocalizableException {
        return super.parseValue(data, vo.getDataTypeId(), pointName);
    }

    @Override
    protected String getSetRequestImpl(MangoValue value) {
        boolean b = ((BinaryValue) value).getBooleanValue();
        if (b)
            return "SB " + vo.getOutputId();
        return "CB " + vo.getOutputId();
    }

    @Override
    public MangoValue parseSetResponse(String data) throws LocalizableException {
        if (!"".equals(data))
            throw new LocalizableException(new LocalizableMessage("event.galil.unexpected", data));
        return null;
    }
}
