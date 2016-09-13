import com.lsscl.app.dao.impl.ScopeAlarmListDao;

public class HelloWorld {
	public static void main(String[] args) {

		String pattern = "(?i)\\s*((bar)|V|(吨)|(kw\\.h)|(kw)|(hz))\\s*";
		String s = "kW.h";
		System.out.println(ScopeAlarmListDao.select_scopeAlarmList2);
	}
}
