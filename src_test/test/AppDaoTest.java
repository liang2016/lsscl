package test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.junit.Before;
import org.junit.Test;

import com.lsscl.app.bean.PointsStatisticsMsgBody;
import com.lsscl.app.bean.PointsWithin24MsgBody;
import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.dao.AppDao;
import com.lsscl.app.dao.AppDaoImpl;
import com.lsscl.app.dao.impl.CompressorDetailsDao;
import com.lsscl.app.dao.impl.GetAllScopesDao;
import com.lsscl.app.dao.impl.MobileIndexDao;
import com.lsscl.app.dao.impl.PointsStatisticsDao;
import com.lsscl.app.dao.impl.PointsWithin24HDao;
import com.lsscl.app.dao.impl.ScopeAlarmListDao;
import com.lsscl.app.dao.impl.ScopeTreeDao;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.vo.DataPointVO;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class AppDaoTest {
	private AppDao dao = new AppDaoImpl();

	@Before
	public void init() {
		SQLServerDataSource dataSource = new SQLServerDataSource();
		dataSource
				.setURL("jdbc:sqlserver://192.168.1.116:1433; DatabaseName=lssclDB");
		dataSource.setUser("sa");
		dataSource.setPassword("123456");
	}

	@Test
	public void sss() {

		String str = "var target = \"PDSEARCH_START,1,p7768,p7768,PDSEARCH_END\";";
		String regex = "(?:<\")(,)(?:\";>)";
		int aa = str.indexOf("PDSEARCH_START,");
		int bb = str.indexOf(",PDSEARCH_END");
		String aaa = str.substring(aa + 15, bb);
		// String str = "one123";
		// String regex = "(?<=one)(?=123)";
		String[] strs = aaa.split(",");
		System.out.print(strs[0]);
	}

	@Test
	public void mobleIndex() {
		dao = new MobileIndexDao();
		QC qc = new QC();
		qc.getMsgBody().put("SCOPEID", "168");
		RSP rsp = dao.getRSP(qc);
		System.out.println(rsp.toJSON());
	}

	@Test
	public void mobileLogin() {
		QC qc = new QC();
		qc.getMsgBody().put("PHONENO", "15322222222");
		qc.getMsgBody().put("PASSWORD", "123");
		// RSP rsp = dao.mobleLogin(qc);
		// System.out.println(rsp.toJSON());
	}

	@Test
	public void compressorDetails() {
		QC qc = new QC();
		qc.setMsgId("CompressorDetails");
		qc.getMsgBody().put("SCOPEID", "161");
		qc.getMsgBody().put("COMPRESSORID", "38");

		dao = new CompressorDetailsDao();
		RSP rsp = dao.getRSP(qc);
		System.out.println(rsp.toJSON());
	}

	@Test
	public void compressorAttrNames() {
		QC qc = new QC();
		qc.setMsgId("CompressorAttrNames");
		qc.getMsgBody().put("SCOPEID", "168");

		// RSP rsp = dao.compressorAtrrNames(qc);
		// System.out.println(rsp.toJSON());
	}

	@Test
	public void compressorList() {
		QC qc = new QC();
		qc.getMsgBody().put("SCOPEID", "168");
		// RSP rsp = dao.compressorList(qc);
		// System.out.println(rsp.toJSON());
	}

	@Test
	public void contactInfo() {
		QC qc = new QC();
		qc.setMsgId("");
		qc.getMsgBody().put("PHONENO", "13777777777");
		qc.getMsgBody().put("PASSWORD", "123");
		qc.getMsgBody().put("CONTACTTEXT", "我的反馈");
		qc.getMsgBody().put("COMPRESSORID", "284");
		// RSP rsp=dao.contactUs(qc);
		// System.out.println(rsp);
	}

	@Test
	public void alarmHistory() {
		QC qc = new QC();
		qc.setMsgId("AlarmHistory");
		qc.getMsgBody().put("SCOPEID", "168");
		// RSP rsp = dao.alarmHistory(qc);
		// System.out.println(rsp.toJSON());
	}

	@Test
	public void ScopeList() {
		QC qc = new QC();
		qc.setMsgId("ScopeList");
		qc.getMsgBody().put("SCOPEID", "1");
		qc.getMsgBody().put("PHONENO", "15067118176");
		qc.getMsgBody().put("PASSWORD", "123");
		qc.getMsgBody().put("ISROOT", "true");
		// RSP rsp = dao.ScopeList(qc);
		// System.out.println(rsp.toJSON());
	}

	/**
	 * 区域报警列表测试
	 */
	@Test
	public void ScopeAlarmList() {
		QC qc = new QC();
		qc.setMsgId("ScopeAlarmList");
		qc.getMsgBody().put("SCOPEID", "1");
		qc.getMsgBody().put("LEVEL", "1");
		qc.getMsgBody().put("STARTINDEX", "1");
		qc.getMsgBody().put("PAGESIZE", "30");
		dao = new ScopeAlarmListDao();
		RSP rsp = dao.getRSP(qc);
		System.out.println(rsp.toJSON());
	}

	@Test
	public void ScopeIndex() {
		QC qc = new QC();
		qc.setMsgId("ScopeIndex");
		qc.getMsgBody().put("SCOPEID", "1");
		// RSP rsp = dao.scopeIndex(qc);
		// System.out.println(rsp.toJSON());
	}

	@Test
	public void sendMail() {
		// dao.sendMail("15067118171","123");
	}

	@Test
	public void saveAllEventStatistics() {
		dao.saveAllEventStatistics();
	}

	/**
	 * 报警线程
	 */
	@Test
	public void events() {
		List<Integer> ids = new DataPointDao().getDataPointIds();
		int i = 0;
		for (Integer id : ids) {
			DataPointVO dp = new DataPointDao().getDataPoint(id);
			if ("common.dataTypes.binary".equals(dp.getDataTypeMessage()
					.getKey())) {// 二进制数据
				i++;
				dao.saveMobileEvent(id);

			}
		}
	}

	private void toXml(RSP rsp) {
		try {

			Configuration cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(new File("src"));
			Template temp = cfg.getTemplate("RSP.xml");
			Map root = new HashMap();
			root.put("rsp", rsp);
			/* 合并数据模型和模版 */
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Writer out = new OutputStreamWriter(baos);
			temp.process(root, out);
			out.flush();
			System.out.println(baos.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void makeEvent() {
		int scopeId = 11;
		dao.makeEvent(scopeId, 1);
	}

	@Test
	public void testPhonesByPid() {
		Set set = new HashSet();
		set.add("6974ac11 870e09fa 00e2238e 8cfafc7d 2052e342 182f5b57 fabca445 42b72e1b");
		set.add("6974ac11 870e09fa 00e2238e 8cfafc7d 2052e342 182f5b57 fabca445 42b72e1b");
		System.out.println(set.size());
	}

	@Test
	public void testScopeTree() {
		QC qc = new QC();
		qc.setMsgId("ScopeList");
		qc.getMsgBody().put("SCOPEID", "1");
		qc.getMsgBody().put("PHONENO", "15067118176");
		qc.getMsgBody().put("PASSWORD", "111");
		dao = new ScopeTreeDao();
		RSP rsp = dao.getRSP(qc);
		System.out.println(rsp.toJSON());
	}

	@Test
	public void testUserDao() {
		System.out.println(UserDao.getUsersAndParentUsers);
	}

	@Test
	public void getAllScopes() {
		QC qc = new QC();
		qc.setMsgId("GetAllScopes");
		qc.getMsgBody().put("PHONENO", "15067118176");
		qc.getMsgBody().put("VERSION", "0");
		dao = new GetAllScopesDao();
		RSP rsp = dao.getRSP(qc);
		System.out.println(rsp.toJSON());
	}

	@Test
	public void pointsWith24H() throws FileNotFoundException, ParseException {
		QC qc = new QC();
		qc.setMsgId("PointsWith24H");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String stime = dateFormat.parse("2014-09-25").getTime() + "";
		qc.getMsgBody().put("PID", "13906");
		qc.getMsgBody().put("STIME", stime);
		qc.getMsgBody().put("VERSION", "0");
		qc.getMsgBody().put("IMGTYPE", "line");// 统计图类型
		dao = new PointsWithin24HDao();
		RSP rsp = dao.getRSP(qc);
		PointsWithin24MsgBody msgBody = (PointsWithin24MsgBody) rsp
				.getMsgBody();
		FileOutputStream fos = new FileOutputStream(new File("D:/test.png"));
		msgBody.toImage(fos);
	}

	// function __scriptExecutor__() {
	// return p7768.value;
	// }
	//
	// __scriptExecutor__();
	//
	// function max() {
	// var max = -java.lang.Double.MAX_VALUE;
	// for (var i = 0; i < arguments.length; i++) {
	// if (max < arguments[i])
	// max = arguments[i];
	// }
	// return max;
	// }
	//
	// function min() {
	// var min = java.lang.Double.MAX_VALUE;
	// for (var i = 0; i < arguments.length; i++) {
	// if (min > arguments[i])
	// min = arguments[i];
	// }
	// return min;
	// }
	//
	// function avg() {
	// if (arguments.length == 0)
	// return 0;
	// var sum = 0;
	// for (var i = 0; i < arguments.length; i++)
	// sum += arguments[i];
	// return sum / arguments.length;
	// }
	//
	// function sum() {
	// var sum = 0;
	// for (var i = 0; i < arguments.length; i++)
	// sum += arguments[i];
	// return sum;
	// }

	@Test
	public void pointsStatistics() throws FileNotFoundException, ParseException {
		QC qc = new QC();
		qc.setMsgId("pointsStatistics");
		qc.getMsgBody().put("PID", "13906");
		qc.getMsgBody().put("COUNT", "100");
		qc.getMsgBody().put("IMGTYPE", "line");// 统计图类型
		dao = new PointsStatisticsDao();
		RSP rsp = dao.getRSP(qc);
		PointsStatisticsMsgBody msgBody = (PointsStatisticsMsgBody) rsp
				.getMsgBody();
		FileOutputStream fos = new FileOutputStream(new File("D:/test.png"));
		msgBody.toImage(fos);
	}

	@Test
	public void testExcelRead() {
		try {
			double bar = 5.3;
			int kw = 110;
			File f = new File("D:\\work\\副本节能量计算表.xls");
			FileInputStream fis;
			fis = new FileInputStream(f);
			jxl.Workbook rwb = Workbook.getWorkbook(fis);
			// 一旦创建了Workbook，我们就可以通过它来访问
			// Excel Sheet的数组集合(术语：工作表)，
			// 也可以调用getsheet方法获取指定的工资表
			Sheet[] sheet = rwb.getSheets();
			int cell = 1;
			int row = 1;
			for (int i = 0; i < sheet.length; i++) {
				Sheet rs = rwb.getSheet(i);
				if (rs.getRows() > 1) {
					Cell[] row1 = rs.getRow(0);
					Cell[] col1 = rs.getColumn(0);

					for (int j = 1; j < row1.length; j++) {
						String cells = row1[j].getContents().trim();
						if (cells.equals("" + bar)) {
							cell = j;
							break;
						}
					}

					for (int j = 1; j < col1.length; j++) {
						String rows = col1[j].getContents().trim();
						if (rows.equals("" + kw)) {
							row = j;
							break;
						}
					}
					String result = rs.getCell(cell, row).getContents().trim();

					// for (int j = 1; j < rs.getRows(); j++) {
					// Cell[] cells = rs.getRow(j);
					// String bars = cells[j].getContents().trim();
					// if ((bar + "").equals(bars)) {
					//
					// }
					// for (int k = 1; k < rs.getColumns(); k++) {
					// sb.append("[" + row1[k].getContents().trim() + ","
					// + col1[j].getContents().trim() + ","
					// + cells[k].getContents().trim() + "],");
					// }
					// }
					System.out.println(Double.valueOf(result));
					// sb = new StringBuilder();
				}
			}
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return sb.toString();
	}
}
