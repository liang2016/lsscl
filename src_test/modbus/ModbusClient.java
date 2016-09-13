package modbus;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.rt.dataImage.DataPointRT;
import com.serotonin.mango.rt.dataSource.modbus.ModbusPointLocatorRT;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.dataSource.modbus.ModbusPointLocatorVO;
import com.serotonin.messaging.MessagingExceptionHandler;
import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.base.ReadFunctionGroup;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

public class ModbusClient implements MessagingExceptionHandler {
	@Test
	public void testClient(){
		SQLServerDataSource dataSource = new SQLServerDataSource();
		dataSource.setURL("jdbc:sqlserver://192.168.1.116:1433; DatabaseName=LssclDB");
		dataSource.setUser("sa");
		dataSource.setPassword("123456");
		
		BatchRead<ModbusPointLocatorRT> batchRead = new BatchRead<ModbusPointLocatorRT>();
        batchRead.setContiguousRequests(true);//设置连续请求
        batchRead.setErrorsInResults(true);
        batchRead.setExceptionsInResults(true);
        batchRead.setForceRead(false);
        List<DataPointRT> dataPoints = new ArrayList<DataPointRT>();
        DataPointVO vo = new DataPointDao(dataSource).getDataPoint(8206);
        DataPointRT dataPoint1 = new DataPointRT(vo, vo
				.getPointLocator().createRuntime());
        dataPoints.add(dataPoint1);
        long time_88 = 0;
        for (DataPointRT dataPoint : dataPoints) {
        	ModbusPointLocatorRT locator = dataPoint.getPointLocator();
            if (!locator.getVO().isSlaveMonitor()) {
            	if(locator.getVO().getRange()==RegisterRange.HOLDING_REGISTER_88){
            		if(true){
                		time_88=new Date().getTime()/1000;
                	}
            		locator.getVO().setTimestamp(time_88);
            	}
            	 if(batchRead.isForceRead()){
                 	if(locator.getVO().getRange()!=RegisterRange.HOLDING_REGISTER_88)
                 		continue;
                 }
				BaseLocator<?> modbusLocator = createModbusLocator(locator.getVO());
                batchRead.addLocator(locator, modbusLocator);
            }
        }
        IpParameters params = new IpParameters();
        params.setHost("192.168.1.116");
        params.setPort(8888);
        params.setEncapsulated(true);//封装
        ModbusMaster modbusMaster = new ModbusFactory().createUdpMaster(params);
        modbusMaster.setTimeout(5000);
        modbusMaster.setRetries(4);
        modbusMaster.setMaxReadBitCount(2000);
        modbusMaster.setMaxReadRegisterCount(125);
        modbusMaster.setMaxWriteRegisterCount(120);

        modbusMaster.setExceptionHandler(this);//设置异常处理程序
        try {
        	modbusMaster.init();
        	if(batchRead.getReadFunctionGroups()!=null)
        		updateBatchRead(batchRead,new Date().getTime()/1000);
        	BatchResults<ModbusPointLocatorRT> results = modbusMaster.send(batchRead);
        	ModbusPointLocatorRT locator = dataPoint1.getPointLocator();
        	Object result = results.getValue(locator);
        	long[] times = (long[]) results.getTimes(locator);
        	System.out.println("dataType:"+locator.getVO().getDataTypeId());
        	System.out.println("dataValue:"+result);
        	System.out.println("retTime:"+Arrays.toString(times));
        	System.out.println("times[0]-time_88="+(times[0]-time_88));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 public static BaseLocator<?> createModbusLocator(ModbusPointLocatorVO vo) {
	        return BaseLocator.createLocator(vo.getSlaveId(), vo.getRange(), vo.getOffset(),vo.getTimestamp(), vo.getModbusDataType(),
	                vo.getBit(), vo.getRegisterCount(), Charset.forName(vo.getCharset()));
	    }
	@Override
	public void receivedException(Exception paramException) {
		
	}
	
	private void updateBatchRead(BatchRead<ModbusPointLocatorRT> batchRead,long timestamp) {
    	for (ReadFunctionGroup<ModbusPointLocatorRT> RF: batchRead.getReadFunctionGroups()) {
    		RF.getSlaveAndTime().setTime(timestamp);
		}
	}

}
