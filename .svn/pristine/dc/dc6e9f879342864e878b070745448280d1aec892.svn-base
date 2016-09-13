/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.modbus;

import com.serotonin.mango.rt.dataSource.PointLocatorRT;
import com.serotonin.mango.vo.dataSource.modbus.ModbusPointLocatorVO;
import com.serotonin.modbus4j.code.DataType;

public class ModbusPointLocatorRT extends PointLocatorRT {
    private final ModbusPointLocatorVO vo;

    public ModbusPointLocatorRT(ModbusPointLocatorVO vo) {
        this.vo = vo;
    }

    public int getRegisterCount() {
        return DataType.getRegisterCount(vo.getModbusDataType());
    }

    @Override
    public boolean isSettable() {
        return vo.isSettable();
    }

    public ModbusPointLocatorVO getVO() {
        return vo;
    }
}
