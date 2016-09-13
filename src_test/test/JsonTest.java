package test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.lsscl.app.bean.QC;

public class JsonTest {

	@Test
	public void test() {
		String json = "{\n" + "QC:{\n" + "MSGID:'MobileLogout',\n"
				+ "SIMNO:'通信卡号',\n" + "IMEI:'设备标识',\n" + "IMSI:'卡标识',\n"
				+ "MSGBODY:{\n" + "PHONENO:'手机号',\n" + "PASSWORD:'用户密码',\n"
				+ "CONTACTTEXT:'反馈内容'\n" + "}\n" + "}\n" + "}";
		System.out.println(json);
		QC qc = getQCWithJSON(json);
		System.out.println(qc.getMsgBody());
	}

	@Test
	public void test1(){
		String s = "{age:1}";
		try {
			JSONObject json = new JSONObject(s);
			System.out.println(json.getString("age"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	private QC getQCWithJSON(String json) {
		QC qc = null;
		try {
			JSONObject jObj = new JSONObject(json);
			String qcString = jObj.getString("QC");
			System.out.println(qcString);
			if (qcString == null)
				return qc;
			qc = new QC();
			jObj = new JSONObject(qcString);
			String msgId = jObj.getString("MSGID");
			String simNo = jObj.getString("SIMNO");
			String imei = jObj.getString("IMEI");
			String imsi = jObj.getString("IMSI");
			qc.setMsgId(msgId);
			qc.setSimNo(simNo);
			qc.setImei(imei);
			qc.setImsi(imsi);
			String msgBody = jObj.getString("MSGBODY");
			if (msgBody == null)
				return qc;
			jObj = new JSONObject(msgBody);
			qc.setMsgBody(toMap(jObj));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return qc;
	}

	/**
	 * 将json对象转换成Map
	 * 
	 * @param jsonObject
	 *            json对象
	 * @return Map对象
	 */
	public static Map<String,String> toMap(JSONObject jsonObject) {

		Map<String,String> result = new HashMap<String,String>();
		try {
		Iterator<String> iterator = jsonObject.keys();
		String key = null;
		String value = null;
		while (iterator.hasNext()) {

			key = iterator.next();
				value = jsonObject.getString(key);
			result.put(key, value);

		}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;

	}
}
