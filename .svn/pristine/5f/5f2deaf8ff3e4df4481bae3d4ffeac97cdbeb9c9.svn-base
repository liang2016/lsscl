/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.galil;

import com.serotonin.mango.rt.dataImage.types.AlphanumericValue;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.vo.dataSource.galil.CommandPointTypeVO;

/**
 *  
 */
public class CommandPointTypeRT extends PointTypeRT {
    public CommandPointTypeRT(CommandPointTypeVO vo) {
        super(vo);
    }

    @Override
    public String getPollRequestImpl() {
        return null;
    }

    @Override
    public MangoValue parsePollResponse(String data, String pointName) {
        return null;
    }

    @Override
    protected String getSetRequestImpl(MangoValue value) {
        return value.getStringValue();
    }

    @Override
    public MangoValue parseSetResponse(String data) {
        return new AlphanumericValue(data);
    }
}
