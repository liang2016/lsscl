package test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.lsscl.app.bean.QC;
import com.lsscl.app.util.HttpUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class HttpTest {
	@Test
	public void test(){
		Configuration cfg = new Configuration();
		try {
			cfg.setDirectoryForTemplateLoading(new File("src"));
			Template temp = cfg.getTemplate("QC.xml");
			 QC qc = new QC();
			 qc.setMsgId("MobileLogin");
//		     qc.setMsgId("ContactUs");
//		     qc.setMsgId("CompressorList");
//		     qc.setMsgId("MobileIndex");
//		     qc.setMsgId("CompressorDetails");
//		     qc.setMsgId("AlarmHistory");
//			 qc.setMsgId("AlarmHistoryDownload");
		     qc.setMsgId("CompressorAttrNames");
		     qc.setMsgId("ScopeAlarmList");
		     qc.setSimNo("1233210");
		     qc.setImei("test");
		     qc.setImsi("e1231");
		     
		     qc.getMsgBody().put("PHONENO", "15188888888");
				qc.getMsgBody().put("PASSWORD", "123");
				qc.getMsgBody().put("CONTACTTEXT", "我的反馈123");
				qc.getMsgBody().put("COMPRESSORID", "291");
				qc.getMsgBody().put("STIME", "2013-12-21 10:10:10");
				qc.getMsgBody().put("ETIME", "2013-12-22 10:10:10");
				qc.getMsgBody().put("LASTQCTIME", "2013-12-22 10:10:10");
			Map root = new HashMap();
			root.put("qc", qc);
			/* 合并数据模型和模版*/  
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        Writer out = new OutputStreamWriter(baos);  
	        temp.process(root, out);  
	        out.flush();  
	        //发送请求
//	        String path = "http://www.lsscl.com:8080/servlet/AppServlet";
	        String path = "http://www.81do.net:8080/servlet/AppServlet";
	        System.out.println(baos.toString());
	        byte [] data = HttpUtil.postXml(path, baos.toString(), "utf-8");
	        String s = new String(data,"GBK");
//	        String s = HttpUtil.SendDataToServer(path, baos.toString(), "2D39C7101D60F39BF3710E004F585402");
			System.out.println(s);
	        
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/*	@Test
	public void testApns(){
		String deviceToken = "2a467f33 0ab21821 0a86bd35 87c40046 3a1c6eda 97c64984 5e9fe36a 7c69aed8";
		ApnsUtil.pushNoficationWithSound(deviceToken, "tts");
		System.out.println(new Date().getHours());
	}*/

	@Test
	public void testJson(){
		String path = "http://192.168.1.100:8080/servlet/AppServlet";
		String msgId = "MobileLogin";
//		msgId = "CompressorList";
//		msgId = "CompressorDetails";
//		msgId = "MobileIndex";
//		msgId = "AlarmHistory";
//		msgId = "CompressorAttrNames";
//		msgId = "ContactUs";
//		msgId = "ScopeList";
		msgId = "ScopeAlarmList";
//		msgId = "ScopeIndex";
		String phoneNo = "13100000001";
		phoneNo = "15067118176";
		String pwd = "123";
		String aid = "11";
		String imsi  = "948d3a7b ba699b9c de5b35ea cc34b238 0f653ba7 cb85bc0a 299fc123 0bcbe631";
		String imei = "10";//有声音
		String json = "{QC:{" + "MSGID:'"+msgId+"',"
				+ "SIMNO:'通信卡号'," + "IMEI:'"+imei+"'," + "IMSI:'"+imsi+"',"
				+ "MSGBODY:{" + "PHONENO:'"+phoneNo+"'," + "PASSWORD:'"+pwd+"',"
				+ "CONTACTTEXT:'邮件测试'," + "COMPRESSORID:"+aid+",SCOPEID:1,LEVEL:0,STARTINDEX:1,PAGESIZE:500}}}";
		try {
			byte []data = HttpUtil.postXml(path, json, "utf-8");
			String s = new String(data,"GBK");
			System.out.println(s);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
