/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.galil;

import java.text.DecimalFormat;

import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataSource.DataSourceUtils;
import com.serotonin.mango.vo.dataSource.galil.PointTypeVO;
import com.serotonin.web.i18n.LocalizableException;

/**
 *  
 */
abstract public class PointTypeRT {
    private static final DecimalFormat numericFormat = new DecimalFormat("#.#");

    private final PointTypeVO vo;

    public PointTypeRT(PointTypeVO vo) {
        this.vo = vo;
    }

    public boolean isSettable() {
        return vo.isSettable();
    }

    final public GalilRequest getPollRequest() {
        String data = getPollRequestImpl();
        if (data == null)
            return null;
        return new GalilRequest(data);
    }

    abstract protected String getPollRequestImpl();

    abstract public MangoValue parsePollResponse(String data, String pointName) throws LocalizableException;

    final public GalilRequest getSetRequest(MangoValue value) {
        String data = getSetRequestImpl(value);
        if (data == null)
            return null;
        return new GalilRequest(data);
    }

    abstract protected String getSetRequestImpl(MangoValue value);

    abstract public MangoValue parseSetResponse(String data) throws LocalizableException;

    protected MangoValue parseValue(String data, int dataTypeId, String pointName) throws LocalizableException {
        return DataSourceUtils.getValue(data, dataTypeId, "0.0000", null, numericFormat, pointName);
    }

    protected double rawToEngineeringUnits(double raw, double rawLow, double rawHigh, double engLow, double engHigh) {
        double numerator = (engHigh - engLow) * raw + rawHigh * engLow - rawLow * engHigh;
        return numerator / (rawHigh - rawLow);
    }

    protected double engineeringUnitsToRaw(double eng, double rawLow, double rawHigh, double engLow, double engHigh) {
        double numerator = (rawHigh - rawLow) * eng - rawHigh * engLow + rawLow * engHigh;
        return numerator / (engHigh - engLow);
    }
}
