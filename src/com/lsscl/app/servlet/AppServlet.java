package com.lsscl.app.servlet;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import com.lsscl.app.bean.PointsStatisticsMsgBody;
import com.lsscl.app.bean.PointsWithin24MsgBody;
import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.service.AppOldService;
import com.lsscl.app.service.AppService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class AppServlet extends HttpServlet {
	private static final long serialVersionUID = 7537058786303267609L;
	private static final String TYPE_XML = "text/xml;";
	private static final String TYPE_JSON = "text/json;";

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String type = req.getContentType();
		String data = getPostData(req);
		QC qc = null;
		if (type.startsWith(TYPE_XML)) {// xml数据传输
			qc = getQCWithXML(data);
			sendXmlData(req, res, qc);
		} else if (type.startsWith(TYPE_JSON)) {// json传输
			qc = getQCWithJSON(data);
			sendJsonData(req, res, qc);
		} else {
			qc = getQCWithXML(data);
			sendXmlData(req, res, qc);
		}
	}

	/**
	 * 发送json数据
	 * 
	 * @param req
	 * @param res
	 * @param qc
	 */
	private void sendJsonData(HttpServletRequest req, HttpServletResponse res,
			QC qc) {
		RSP rsp = new AppService().getRSP(qc, req);
		try {
			OutputStream os = res.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(os);
			if (AppService.PointsWithin24H.equals(rsp.getMsgId())) {
				PointsWithin24MsgBody msgBody = (PointsWithin24MsgBody) rsp
						.getMsgBody();
				msgBody.toImage(bos);
			}else if(AppService.PointsStatistics.equals(rsp.getMsgId())){
				PointsStatisticsMsgBody msgBody = (PointsStatisticsMsgBody) rsp
						.getMsgBody();
				msgBody.toImage(bos);
			} else {
				os.write(rsp.toJSON().getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送xml数据
	 * 
	 * @param req
	 * @param res
	 * @param qc
	 * @throws IOException
	 */
	private void sendXmlData(HttpServletRequest req, HttpServletResponse res,
			QC qc) throws IOException {
		// 响应信息
		RSP rsp = new AppOldService().getRSP(qc, req);
		Configuration cfg = new Configuration();
		cfg.setServletContextForTemplateLoading(getServletContext(),
				"/WEB-INF/templates");
		Template temp = cfg.getTemplate("RSP.xml");
		Map root = new HashMap();
		root.put("rsp", rsp);
		/* 合并数据模型和模版 */
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter(baos);
		try {
			temp.process(root, out);
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		out.flush();
		OutputStream os = res.getOutputStream();
		os.write(baos.toByteArray());
	}

	/**
	 * 发送xml数据
	 * 
	 * @param req
	 * @param res
	 * @param qc
	 * @throws IOException
	 */
	private void sendError(HttpServletRequest req, HttpServletResponse res,
			QC qc) throws IOException {
		// 响应信息
		RSP rsp = new RSP(qc.getMsgId());
		rsp.setResult(1);
		rsp.setError("您的版本太旧了，请重官网下载最新版本哦！");
		Configuration cfg = new Configuration();
		cfg.setServletContextForTemplateLoading(getServletContext(),
				"/WEB-INF/templates");
		Template temp = cfg.getTemplate("RSP.xml");
		Map root = new HashMap();
		root.put("rsp", rsp);
		/* 合并数据模型和模版 */
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter(baos);
		try {
			temp.process(root, out);
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		out.flush();
		OutputStream os = res.getOutputStream();
		os.write(baos.toByteArray());
	}

	private QC getQCWithJSON(String json) {
		QC qc = null;
		try {
			JSONObject jObj = new JSONObject(json);
			String qcString = jObj.getString("QC");
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

	private QC getQCWithXML(String xml) {
		QC qc = new QC();
		try {
			byte[] b = xml.getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b);
			SAXBuilder builder = new SAXBuilder();

			Document doc = builder.build(is);
			Element rootEl = doc.getRootElement();
			// 请求信息
			List<Element> list = rootEl.getChildren();
			String msgId = null, simno = null, imei = null, msg = null;

			for (Element el : list) {
				String tagName = el.getName();
				String content = el.getText();
				if ("MSGID".equals(tagName)) {//
					qc.setMsgId(content);
				} else if ("SIMNO".equals(tagName)) {
					qc.setSimNo(content);
				} else if ("IMEI".equals(tagName)) {
					qc.setImei(content);
				} else if ("IMSI".equals(tagName)) {
					qc.setImsi(content);
				} else if ("MSGBODY".equals(tagName)) {
					List<Element> cList = el.getChildren();
					for (Element e : cList) {
						String cTagName = e.getName();
						String cContent = e.getText();
						if ("PHONENO".equals(cTagName)) {
							qc.getMsgBody().put("PHONENO", cContent);
						} else if ("PASSWORD".equals(cTagName)) {
							qc.getMsgBody().put("PASSWORD", cContent);
						} else if ("COMPRESSORID".equals(cTagName)) {
							qc.getMsgBody().put("COMPRESSORID", cContent);
						} else if ("CONTACTTEXT".equals(cTagName)) {
							qc.getMsgBody().put("CONTACTTEXT", cContent);
							System.out.println(cContent);
						} else if ("STIME".equals(cTagName)) {
							qc.getMsgBody().put("STIME", cContent);
						} else if ("ETIME".equals(cTagName)) {
							qc.getMsgBody().put("ETIME", cContent);
						} else if ("LASTQCTIME".equals(cTagName)) {
							qc.getMsgBody().put("LASTQCTIME", cContent);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qc;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		final AppService service = new AppService();
		final ServletContext context = config.getServletContext();

		System.out.println("---------------AppServlet-----------------");
		service.initData();
		new Timer().schedule(new TimerTask() {
			// 报警定时器
			@Override
			public void run() {
				service.checkAlarms(context);
			}
		}, 1, 1000 * 30);
		new Timer().schedule(new TimerTask() {
			// 报警定时器
			@Override
			public void run() {
				service.initData();
			}
		}, 1, 1000 * 60 * 5);
	}

	private String getPostData(HttpServletRequest req) {

		String xml = null;
		try {

			BufferedReader bufferedReader = req.getReader();
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			xml = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xml;
	}

	/**
	 * 将json对象转换成Map
	 * 
	 * @param jsonObject
	 *            json对象
	 * @return Map对象
	 */
	public static Map<String, String> toMap(JSONObject jsonObject) {

		Map<String, String> result = new HashMap<String, String>();
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
