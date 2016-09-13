package com.lsscl.app.util;

import java.util.Date;

import javapns.back.PushNotificationManager;
import javapns.back.SSLConnectionHelper;
import javapns.data.Device;
import javapns.data.PayLoad;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lsscl.app.bean.PushEvents;

public class ApnsUtil {
	private static final Log LOG = LogFactory.getLog(ApnsUtil.class);
	public static void pushDefaultNotification(String deviceToken, String message){
		pushNoficationWithSound(deviceToken, "default",message);
	}
	public static void pushNoficationWithSound(String deviceToken,String sound, String message){
		try
        {
            //从客户端获取的deviceToken，在此为了测试简单，写固定的一个测试设备标识。
          // String deviceToken = "2a467f33 0ab21821 0a86bd35 87c40046 3a1c6eda 97c64984 5e9fe36a 7c69aed8";
			
            LOG.warn("----Push Start----\n deviceToken:" + deviceToken);
            //定义消息模式
            PayLoad payLoad = new PayLoad();
            payLoad.addAlert(message);
            payLoad.addBadge(1);//消息推送标记数，小红圈中显示的数字。
          //  payLoad.addSound(sound);
            if(sound!=null)payLoad.addSound(sound);
            //注册deviceToken
            PushNotificationManager pushManager = PushNotificationManager.getInstance();
            pushManager.addDevice("iPhone", deviceToken);
            //连接APNS
//            String host = "gateway.sandbox.push.apple.com";
            String host = "gateway.push.apple.com";
            int port = 2195;
            String certificatePath = ApnsUtil.class.getClassLoader().getResource("").getPath()+"key.p12";//前面生成的用于JAVA后台连接APNS服务的*.p12文件位置
            System.out.println(certificatePath);
            String certificatePassword = "admin_2014";//p12文件密码。
            pushManager.initializeConnection(host, port, certificatePath, certificatePassword, SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
            //发送推送
            Device client = pushManager.getDevice("iPhone");
            LOG.warn("推送消息: " + client.getToken()+"\n"+payLoad.toString() +" ");
            pushManager.sendNotification(client, payLoad);
            //停止连接APNS
            pushManager.stopConnection();
            //删除deviceToken
            pushManager.removeDevice("iPhone");
            LOG.warn("----Push End----");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
	}
	public static void pushNotification(String deviceToken, String imei,String message) {
		if(imei==null){
			pushDefaultNotification(deviceToken,message); 
			return;
		}
		int flag= Integer.valueOf(imei);
		if(flag==10){//有声音
			pushDefaultNotification(deviceToken,message);
			return;
		}else if(flag==11){//22:00~8:00静音
			int h = new Date().getHours();
			if(h>8&&h<22){
				pushDefaultNotification(deviceToken,message);
			}else{
				pushNoficationWithSound(deviceToken, null,message);
			}
			return;
		}else if(flag==1||flag==0){//没有声音
			pushNoficationWithSound(deviceToken, null,message);
		}
	}
	public static void pushNotification(PushEvents pe) {
		String message = pe.message();
		String deviceToken = pe.getDeviceToken();
		String flag = pe.getFlag();
		pushNotification(deviceToken,flag,message);
	}
}
