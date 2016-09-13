package test;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.serotonin.mango.db.dao.EventDao;
import com.serotonin.mango.vo.event.EventHandlerVO;
import com.serotonin.web.i18n.LocalizableMessage;
import com.serotonin.web.i18n.LocalizableMessageParseException;

public class EventDaoTest {
	private EventDao dao;

	@Before
	public void init() {
		SQLServerDataSource dataSource = new SQLServerDataSource();
		dataSource
				.setURL("jdbc:sqlserver://192.168.1.116:1433; DatabaseName=LssclDB");
		dataSource.setUser("sa");
		dataSource.setPassword("123456");
		dao = new EventDao(dataSource);
	}

	@Test
	public void getEventHandler() {
		EventHandlerVO vo = dao.getEventHandler(14);
		System.out.println(vo);
	}

	@Test
	public void messageTest() {
		Locale defaultLocale = Locale.getDefault();
		String message = "event.detector.highLimitPeriod|1# LS-20-T1|105 ℃|[common.tp.description|200|[common.tp.seconds|]]";
		String message2 = "event.detector.changeCount|1# LS-20-报警: 空滤堵塞|正常|异常|";
		String message3 = "event.email.failure|中强-压力,温度 - 定时器报表|hsj@lsscl.com, zzq@lsscl.com, gsz@lsscl.com, ghp@lsscl.com|Mail server connection failed; nested exception is javax.mail.MessagingException: Could not connect to SMTP host: 127.0.0.1, port: 25;nested exception is:java.net.ConnectException: Connection refused: connect|";
		String m4 = "event.schedule.active|[event.schedule.dailyUntil|00:00:00|00:00:00|]";
		String m5 = "event.ds|加载比例|[event.meta.pointUnavailable|加载比例|]";
		String m6 = "event.login|admin|";
		try {
			LocalizableMessage lm = LocalizableMessage.deserialize(m6);
			ResourceBundle bundle = ResourceBundle.getBundle("mobile",defaultLocale);  
			System.out.println(lm.getLocalizedMessage(bundle));
		} catch (LocalizableMessageParseException e) {
			e.printStackTrace();
		}
	}
}
