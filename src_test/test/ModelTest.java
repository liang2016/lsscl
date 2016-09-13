package test;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.lsscl.app.bean.IndexMsgBody;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.dao.AppDao;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class ModelTest {
	private AppDao dao;
	@Before
	public void init(){
		SQLServerDataSource dataSource = new SQLServerDataSource();
		dataSource.setURL("jdbc:sqlserver://192.168.1.116:1433; DatabaseName=LssclDB");
		dataSource.setUser("sa");
		dataSource.setPassword("123456");
//		dao = new AppDao(dataSource);
	}

	@Test
	public void getRspByJSON() throws IOException{
//		String json = IOUtils.toString(Thread.currentThread()
//                .getContextClassLoader().getResourceAsStream("rsp.json"));
		RSP rsp = new RSP();
		rsp.setMsgId("MobileIndex");
		rsp.setResult(0);
		rsp.setError("无");
		IndexMsgBody msg = new IndexMsgBody();
		msg.setUsername("李四");
		msg.setFactoryName("巨石集团");
		msg.setLastAlarmTime("2014-03-11 10:10:11");
		msg.setPower("75.66");
		msg.setTotal(5);
		msg.setOpen(3);
		msg.setClose(2);
		msg.setAlarm(1);
		rsp.setMsgBody(msg);
		System.out.println(rsp.toJSON());
//	    rsp = RSP.getRSPByJsonString(rsp.toJSON());
		System.out.println(rsp);
	}


	
}
