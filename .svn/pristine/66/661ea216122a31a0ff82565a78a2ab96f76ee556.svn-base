/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.modbus;

import com.serotonin.mango.vo.dataSource.modbus.ModbusIpDataSourceVO;
import com.serotonin.mango.vo.dataSource.modbus.ModbusIpDataSourceVO.TransportType;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.ip.IpParameters;

public class ModbusIpDataSource extends ModbusDataSource {
    private final ModbusIpDataSourceVO configuration;

    public ModbusIpDataSource(ModbusIpDataSourceVO configuration) {
        super(configuration);
        this.configuration = configuration;
    }

    //
    //
    // Lifecycle
    //
    //
    @Override
    public void initialize() {
        IpParameters params = new IpParameters();
        params.setHost(configuration.getHost());
        params.setPort(configuration.getPort());
        params.setEncapsulated(configuration.isEncapsulated());//封装

        ModbusMaster modbusMaster;
        if (configuration.getTransportType() == TransportType.UDP)
            modbusMaster = new ModbusFactory().createUdpMaster(params);
        else
            modbusMaster = new ModbusFactory().createTcpMaster(params,
                    configuration.getTransportType() == TransportType.TCP_KEEP_ALIVE);

        super.initialize(modbusMaster);
    }
}
