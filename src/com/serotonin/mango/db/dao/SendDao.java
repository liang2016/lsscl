package com.serotonin.mango.db.dao;

import com.serotonin.mango.db.dao.BaseDao;
import java.util.List;

public class SendDao extends BaseDao {
	public interface SMSSend {
		int READY = 0;
		int SENT = 1;
	}

	private final String INSERT_START = "insert into SMSList(tel,time,info,state) ";
	private final String INSERT_VALUE = "select ?,?,?,?";
	private final int INSERT_VALUES_COUNT = 4;

	/**
	 * 添加SMS
	 * 
	 * @param tel
	 * @param info
	 * @param state
	 */
	public void insertSMS(List<String> tel,long time, String info, int state) {
		Object[] params = new Object[tel.size() * INSERT_VALUES_COUNT];
		StringBuilder sb = new StringBuilder();
		sb.append(INSERT_START);
		int index =0;
		for (int i = 0; i < tel.size(); i++) {
			if (i > 0)
				sb.append(" union all ");
			sb.append(INSERT_VALUE);
			index= i * INSERT_VALUES_COUNT;
			params[index++] = tel.get(i);
			params[index++] =time;
			params[index++] = info;
			params[index++] = state;
			// System.out.println(sb.toString());
			// for (int j = 0; j < params.length; j++) {
			// System.out.println(params[i]);
			// }
		}
		if(index>0)
			doInsert(sb.toString(), params);
	}

	// public static void main(String[] args) {
	// String[] tel = { "13767426410", "13865421456", "13254687521" };
	// String info = "工厂黄色警报";
	// int state = SMSSend.READY;
	// new SMSDao().insertSMS(tel, info, state);
	// }
}
