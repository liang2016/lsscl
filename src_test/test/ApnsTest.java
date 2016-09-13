package test;

import javapns.back.PushNotificationManager;
import javapns.back.SSLConnectionHelper;
import javapns.data.Device;
import javapns.data.PayLoad;

import org.junit.Test;


public class ApnsTest {
	@Test
	public void test(){
		try
        {
            //从客户端获取的deviceToken，在此为了测试简单，写固定的一个测试设备标识。
          // String deviceToken = "2a467f33 0ab21821 0a86bd35 87c40046 3a1c6eda 97c64984 5e9fe36a 7c69aed8";
			String deviceToken = "948d3a7bba699b9cde5b35eacc34b2380f653ba7cb85bc0a299fc1230bcbe631";
            System.out.println("Push Start deviceToken:" + deviceToken);
            //定义消息模式
            PayLoad payLoad = new PayLoad();
            payLoad.addAlert("this is test(back)!");
            payLoad.addBadge(1);//消息推送标记数，小红圈中显示的数字。
//            payLoad.addSound("布谷鸟.caf");
            //注册deviceToken
            PushNotificationManager pushManager = PushNotificationManager.getInstance();
            pushManager.addDevice("iPhone", deviceToken);
            //连接APNS
            String host = "gateway.push.apple.com";
            
            int port = 2195;
            String certificatePath = "E:\\share\\aps_production.p12";//前面生成的用于JAVA后台连接APNS服务的*.p12文件位置
            String certificatePassword = "admin_2014";//p12文件密码。
          
            //开发版
//            host = "gateway.push.apple.com";
//            certificatePath = "E:\\share\\aps_development.p12";
            pushManager.initializeConnection(host, port, certificatePath, certificatePassword, SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
            //发送推送
            Device client = pushManager.getDevice("iPhone");
            System.out.println("推送消息: " + client.getToken()+"\n"+payLoad.toString() +" ");
            pushManager.sendNotification(client, payLoad);
            //停止连接APNS
            pushManager.stopConnection();
            //删除deviceToken
            pushManager.removeDevice("iPhone");
            System.out.println("Push End");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
	}

}
