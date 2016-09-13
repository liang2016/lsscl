package com.lsscl.app.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.lsscl.app.bean.QC;
public class HttpUtil {
	
	/**
	 * 发送xml数据
	 * @param path 请求地址
	 * @param xml xml数据
	 * @param encoding 编码
	 * @return
	 * @throws Exception
	 */
	public static byte[] postXml(String path, String xml, String encoding) throws Exception{
		byte[] data = xml.getBytes(encoding);
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "text/json; charset="+ encoding);//类型
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.setConnectTimeout(5 * 1000);
		OutputStream outStream = conn.getOutputStream();
		outStream.write(data);
		outStream.flush();
		outStream.close();
		if(conn.getResponseCode()==200){
			return readStream(conn.getInputStream());
		}
		return null;
	}
	/**
	 * 读取流
	 * @param inStream
	 * @return 字节数组
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while( (len=inStream.read(buffer)) != -1){
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}
	public static String SendDataToServer(String url, String qc,String sessionId) {
		String svrURL = url;
        String ret = null;
		try {
			// 设置连接超时参数
			int timeOutConnection = 60 * 1000;
			int timeOutSocket = 60 * 1000;
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams
					.setConnectionTimeout(params, timeOutConnection);
			HttpConnectionParams.setSoTimeout(params, timeOutSocket);
			HttpConnectionParams.setSocketBufferSize(params, 64 * 1024);

			StringEntity reqEntity = new StringEntity(qc,
					"utf-8");
			HttpPost httpPost = new HttpPost(svrURL);
			httpPost.setEntity(reqEntity);
			httpPost.setHeader("Cookie", "SESSIONID=" + sessionId);
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient(params);
			HttpResponse response = defaultHttpClient.execute(httpPost);

			// Log.d("FOLLOWME", "SendDataToServer2");
			if (response.getStatusLine().getStatusCode() == 200) {
				ret = EntityUtils.toString(response.getEntity(),
						"GBK");
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (Exception e) {
		}
		return ret;
	}
	
}
