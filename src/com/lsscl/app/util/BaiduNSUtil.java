package com.lsscl.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.lsscl.app.bean.QC;

public class BaiduNSUtil {
	private static final String apiKey = "x8jSKUepDGUe9kFDlt0FPoBQ";
	private static final String secretKey = "ODPiO37j90ZlTK4kYKu3NHHn55ksbEdl";
	private static final Log LOG = LogFactory.getLog(BaiduNSUtil.class);
	/**
	 * 云推送
	 * @param userid Android登录用户设备id
	 * @param channelid 
	 * @param title 标题 
	 * @param content 通知内容
	 * @param msgType “声音"（1）, "振动"（2）, "22:00-8:00 静音”（3）
	 */
	public static void notification(String userid,String channelid,String title,String content,String msgType){
		int basicType = getBasicType(msgType);
		if(basicType==-1)return;
		  /*
         * @brief 向Android端设备推送单播消息
         * message_type = 1
         * device_type = 3
         */

        // 1. 设置developer平台的ApiKey/SecretKey
        
        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

        // 2. 创建BaiduChannelClient对象实例
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);

        // 3. 若要了解交互细节，请注册YunLogHandler类
        channelClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {
        	LOG.warn("----BaiduNS----\n userid:"+userid);
            // 4. 创建请求类对象
            PushUnicastMessageRequest request = new PushUnicastMessageRequest();
            request.setDeviceType(3);
            request.setChannelId(Long.parseLong(channelid));
            request.setUserId(userid);
            request.setMessageType(1);
            String message = "{\"title\":\""+title+"\",\"description\":\""+content+"\",\"notification_basic_style\":"+basicType+"}";
            request.setMessage(message);
            // 5. 调用pushMessage接口
            PushUnicastMessageResponse response = channelClient
                    .pushUnicastMessage(request);

            
            LOG.warn(message+"\n amount:"+response.getSuccessAmount());
            LOG.warn("----push end----");
//            Assert.assertEquals(1, response.getSuccessAmount());

        } catch (ChannelClientException e) {
            // 处理客户端错误异常
            e.printStackTrace();
        } catch (ChannelServerException e) {
            // 处理服务端错误异常
            System.out.println(String.format(
                    "request_id: %d, error_code: %d, error_message: %s",
                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
        }
	}

	/**
	 * 根据loginQC的msgType获取百度云基本推送类型（响铃 震动 通知可以被清除）
	 * @param flag “声音"（1）, "振动"（2）, "22:00-8:00 静音”（3）
	 * @return 响铃 震动 通知可以被清除（二进制转10进制（1选中、 0未选中））
	 */
	public static int getBasicType(String flag){
		SimpleDateFormat df = new SimpleDateFormat("HH");
		int hour = Integer.parseInt(df.format(new Date()));
		boolean isDuringNight = hour<8 || hour>22;
		if("0".equals(flag)){ //对应123（三项全有）指定时间内静音
			return isDuringNight?3:7;
		}else if("1".equals(flag)){// 对应12
			return 7;
		}else if("2".equals(flag)){// 对应13
			return isDuringNight?1:5;
		}else if("3".equals(flag)){// 对应23 指定时间振动
			return isDuringNight?1:3;
		}else if("4".equals(flag)){// 对应1 只有声音
			return 5;
		}else if("5".equals(flag)){// 对应2 只有振动
			return 3;
		}else if("6".equals(flag)){// 对应3（相当于是全静音模式）
			return 1;
		}
		return -1;
	}
	/**
	 * 发送通知
	 * @param loginQC
	 */
	public static void notification(QC loginQC){
    	String userId = loginQC.getImsi();
    	String channel = loginQC.getImei();
    	
    }
}
