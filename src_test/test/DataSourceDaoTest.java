package test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.db.dao.acp.ACPDao;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.acp.ACPTypeVO;
import com.serotonin.mango.vo.acp.ACPVO;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.mango.vo.dataSource.modbus.ModbusIpDataSourceVO;
import com.serotonin.mango.vo.scope.ScopeVO;

public class DataSourceDaoTest {
	private DataSourceDao dao;
	private ACPDao acpDao;

	@Before
	public void init() {
		SQLServerDataSource dataSource = new SQLServerDataSource();
		dataSource
				.setURL("jdbc:sqlserver://192.168.1.117:1433; DatabaseName=LssclDB");
		dataSource.setUser("sa");
		dataSource.setPassword("asus_admin1");
		dao = new DataSourceDao(dataSource);
		// acpDao = new ACPDao(dataSource);
	}

	@Test
	public void getDataSource() {
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("page", 10);
		queryParam.put("pageSize", 10);
		// queryParam.put("name", "test");
		// queryParam.put("port", 11006+"");
		List<Map<String, Object>> maps = dao.getDataSourceByMap(queryParam);
		System.out.println(dao.getCountByMap(queryParam));
		for (Map<String, Object> m : maps) {
			// System.out.println(m);
			ModbusIpDataSourceVO mv = (ModbusIpDataSourceVO) m.get("data");
			System.out.println(m.get("id") + "," + mv.getFactoryId() + ","
					+ mv.getPort());
		}
	}

	@Test
	public void getDataSourceById() {
		ModbusIpDataSourceVO m1 = (ModbusIpDataSourceVO) dao.getDataSource(2);
		ModbusIpDataSourceVO m2 = (ModbusIpDataSourceVO) dao.getDataSource(4);
		System.out.println(m1.getPort() + "," + m1.getName());
		System.out.println(m2.getPort() + "," + m2.getName());
	}

	public void getAcpById() {
		ACPVO acp = new ACPDao().findById(274);
		System.out.println(acp.getVolume());
	}

	/**
	 * 平量添加数据源
	 */
	@Test
	public void addDataSources() {
		int dataSourceCount = 300;
		int iport = 12800;
		int updatePeriods = 20; // 更新期间
		int updatePeriodType = 1; // 秒
		boolean quantize = false; // 量化
		int timeout = 5000; // 超时 (毫秒)
		int retries = 3; // 重试
		boolean contiguousBatches = false; // 仅临近的节点
		boolean createSlaveMonitorPoints = false; // 创建从站通信检测点
		int maxReadBitCount = 2000; // 最大读取数(位)
		int maxReadRegisterCount = 125; // 最大读取数(寄存器)
		int maxWriteRegisterCount = 120; // 最大写入数(寄存器)
		String transportType = "UDP"; // 传输类型
		String host = "192.168.1.117"; // 主机
		boolean encapsulated = true; // 封装277
		System.out.println("-----------------");
		ScopeDao sdao = new ScopeDao();
		int n = 0;
		while (true) {
			for (int sid = 181; sid < 277; sid++) {
				ScopeVO sv = sdao.findById(sid);
				int type = sv.getScopetype();
				if (type == 3) {// 工厂
					saveModbusIpDataSource(sid, iport + "", updatePeriods,
							updatePeriodType, quantize, timeout, retries,
							contiguousBatches, createSlaveMonitorPoints,
							maxReadBitCount, maxReadRegisterCount,
							maxWriteRegisterCount, transportType, host, iport,
							encapsulated);
					iport++;
					n++;
					if(n>dataSourceCount)break;
				}
			}
			if (n > dataSourceCount)
				break;
		}
	}

	private void saveModbusIpDataSource(int fid, String name,
			int updatePeriods, int updatePeriodType, boolean quantize,
			int timeout, int retries, boolean contiguousBatches,
			boolean createSlaveMonitorPoints, int maxReadBitCount,
			int maxReadRegisterCount, int maxWriteRegisterCount,
			String transportType, String host, int port, boolean encapsulated) {
		// A new data source
		ModbusIpDataSourceVO ds = (ModbusIpDataSourceVO) DataSourceVO
				.createDataSourceVO(3);
		ds.setXid(new DataSourceDao().generateUniqueXid());
		ds.setId(Common.NEW_ID);
		ds.setName(name);
		ds.setFactoryId(fid);
		ds.setUpdatePeriods(updatePeriods);
		ds.setUpdatePeriodType(updatePeriodType);
		ds.setQuantize(quantize);
		ds.setTimeout(timeout);
		ds.setRetries(retries);
		ds.setContiguousBatches(contiguousBatches);
		ds.setCreateSlaveMonitorPoints(createSlaveMonitorPoints);
		ds.setMaxReadBitCount(maxReadBitCount);
		ds.setMaxReadRegisterCount(maxReadRegisterCount);
		ds.setMaxWriteRegisterCount(maxWriteRegisterCount);
		ds.setTransportTypeStr(transportType);
		ds.setHost(host);
		ds.setPort(port);
		ds.setEncapsulated(encapsulated);
		ds.setEnabled(true);
		// DataPointDao dataPointDao=new DataPointDao();
		new DataSourceDao().saveDataSource(ds);

		saveAcp(ds.getId(), port, fid);
	}

	private void saveAcp(int dsid, int port, int sid) {
		ACPDao acpDao = new ACPDao();
		ACPVO acpvo = new ACPVO();
		acpvo.setAcpname(port + "");
		acpvo.setXid(port + "");
		ACPTypeVO acpTypeVO = new ACPTypeVO();
		acpTypeVO.setId(1);
		acpvo.setAcpTypeVO(acpTypeVO);
		acpvo.setOffset(40001);
		acpvo.setType(0);
		acpvo.setVolume(110);
		acpvo.setPressure(8);
		acpvo.setFactoryId(sid);
		acpDao.save(acpvo, dsid);
	}
}
