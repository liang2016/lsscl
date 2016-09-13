package modbus;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.lang3.time.DateUtils;

import com.serotonin.io.serial.SerialParameters;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;

public class ModbusRTU {
	private ModbusFactory factory = new ModbusFactory();
	private ModbusMaster master = null;
	private DecimalFormat format = new DecimalFormat("0.0");
	private DecimalFormat format3 = new DecimalFormat("0.000");
	private Random random = new Random();
	private String[] cid = { "101272124", "388477462", "388477474",
			"388477478", "388477510", "388477518", "388477538", "388477542",
			"3322208", "388477458", "388477490", "388477494", "388477506",
			"388477522", "395520057", "199222040" };

	private String getCid() {
		int index = Math.abs(random.nextInt()) % (cid.length);
		return cid[index];
	}

	private int rowid = 101000;

	public void init() {
		SerialParameters params = new SerialParameters();
		try {
			// 设定 MODBUS 通讯的串行口
			params.setCommPortId("COM6");
			// 设定成无奇偶校验
			params.setParity(2);
			// 设定成数据位是 8 位
			params.setDataBits(8);
			// 设定为 1 个停止位
			params.setStopBits(1);
			// 串行口上的波特率
			params.setBaudRate(19200);
//			master = factory.createRtuMaster(params);
			master.setRetries(0);
			master.init();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void destroy() {
		this.master.destroy();
	}

	/**
	 * 读保持寄存器上的内容
	 * 
	 * @param master
	 *            主站
	 * @param slaveId
	 *            从站地址
	 * @param start
	 *            起始地址的偏移量
	 * @param len
	 *            待读寄存器的个数
	 */
	private void readHoldingRegisters(int slaveId, int start, int len) {
		try {
			ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(
					slaveId, start, len);
			ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master
					.send(request);
			if (response.isException())
				System.out.println("Exception response: message="
						+ response.getExceptionMessage());
			else
				System.out.println(Arrays.toString(response.getShortData()));
		} catch (ModbusTransportException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 读取电表
	 * 
	 * @param slaveId
	 *            地址位
	 * @param start
	 *            开始
	 * @param len
	 *            长度
	 */
	public void readElectValue(int slaveId, int start, int len) {
		ReadHoldingRegistersRequest request = null;
		ReadHoldingRegistersResponse response = null;
		try {
			request = new ReadHoldingRegistersRequest(slaveId, start, len);
			response = (ReadHoldingRegistersResponse) master.send(request);
			if (response.isException()) {
				System.out.println("ERROR========================START");
				System.out.println(response.getExceptionCode());
				System.out.println(response.getExceptionMessage());
				System.out.println("ERROR========================END");
			}
			short[] value = response.getShortData();
			/*
			 * 0:电压 6:电流 9:有功功率 3:功率 4:度数
			 */
			StringBuffer dbStr = new StringBuffer("" + rowid).append("\t")
					.append(getCid()).append("\t");
			rowid++;
			double v = (value[0] * 0.214844);
			double a = (value[6] * 0.004883);
			short[] nums = readElectValues(slaveId, start, len);
			// System.out.println("电压："+format.format(v));
			// System.out.println("电流："+format3.format(a));
			// System.out.println("功率："+format3.format(a * v));
			// System.out.println("度数："+nums[5] * 0.01);
			dbStr.append(format3.format(a)).append("\t");
			dbStr.append(format.format(v)).append("\t");
			dbStr.append(format3.format(a * v)).append("\t");
			dbStr.append(format3.format(a * v)).append("\t");
			dbStr.append(nums[5] * 0.01).append("\t");
//			dbStr.append(DateUtils.getStrNow()).append("\t");
			System.out.println(dbStr);
		} catch (ModbusTransportException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public short[] readElectValues(int slaveId, int start, int len) {
		ReadInputRegistersRequest request = null;
		ReadInputRegistersResponse response = null;
		try {
			request = new ReadInputRegistersRequest(slaveId, start, len);
			response = (ReadInputRegistersResponse) master.send(request);
			if (response.isException()) {
				System.out.println("ERROR========================START");
				System.out.println(response.getExceptionCode());
				System.out.println(response.getExceptionMessage());
				System.out.println("ERROR========================END");
			}
			short[] value = response.getShortData();
			return value;
		} catch (ModbusTransportException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		ModbusRTU rtu = new ModbusRTU();
		rtu.init();
		for (int i = 0; i < 1000; i++) {
			// System.out.println(DateUtils.getStrNow() +
			// " read valuesuccess ....");
			rtu.readElectValue(1, 0, 8);
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		rtu.destroy();
		System.out.println("B");
	}
}