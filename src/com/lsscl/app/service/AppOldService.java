package com.lsscl.app.service;


import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.dao.AppOldDao;
import com.lsscl.app.util.AcpConfig;
import com.lsscl.app.util.ApnsUtil;
import com.lsscl.app.util.StringUtil;

public class AppOldService {
	/**
	 * 登录
	 */
	public static final String LOGIN = "MobileLogin";
	/**
	 * 登录首页
	 */
	public static final String INDEX = "MobileIndex";
	
	/**
	 * 空压机列表
	 */
	public static final String ACP_LIST = "CompressorList";
	
	/**
	 * 空压机详细
	 */
	public static final String ACP_DETAIL = "CompressorDetails";
	
	/**
	 * 空压机属性点名称
	 */
	public static final String ACP_ATTR = "CompressorAttrNames";
	
	/**
	 * 报警推送历史
	 */
	public static final String ALARM_HISTORY = "AlarmHistory";
	
	/**
	 * 我的反馈
	 */
	public static final String CONTACT_US = "ContactUs";
	
	/**
	 * 退出登录
	 */
	public static final String LOGOUT = "MobileLogout";
	
	/**
	 * 区域列表
	 */
	public static final String SCOPELIST = "ScopeList";
	
	/**
	 * 报警历史下载
	 */
	public static final String ALARM_HISTORY_DOWNLOAD = "AlarmHistoryDownload";
	
	/**
	 * session过期
	 */
	public static final String SESSION_TIMEOUT = AcpConfig.cfg.getProperty("session.timeout");

	/**
	 * 帐号被其他用户登录
	 */
	public static final String USER_ISLOGINED = AcpConfig.cfg.getProperty("user.isLogined");
	/**
	 * 用户未登录
	 */
	public static final String UNLOGIN = AcpConfig.cfg.getProperty("unLogin");
	private AppOldDao appDao = new AppOldDao();
	
	public RSP getRSP(QC qc,HttpServletRequest req) {
		if(qc==null)return null;
		String msgId = qc.getMsgId();
		RSP rsp = null;
		if(LOGIN.equals(msgId)){//登录
			rsp = appDao.mobleLogin(qc);
		}else if(INDEX.equals(msgId)){//登录首页
		    rsp = appDao.mobileIndex(qc);	
		}else if(ACP_LIST.equals(msgId)){//空压机列表
			rsp = appDao.compressorList(qc);
		}else if(ACP_DETAIL.equals(msgId)){//空压机详细
			rsp = appDao.compressorDetails(qc);
		}else if(ALARM_HISTORY.equals(msgId)){//报警推送历史
			rsp = appDao.alarmHistory(qc);
		}else if(CONTACT_US.equals(msgId)){//我的反馈
			rsp = appDao.contactUs(qc);
		}else if(LOGOUT.equals(msgId)){//退出登录
			rsp = appDao.mobleLogout(qc);
		}else if(ALARM_HISTORY_DOWNLOAD.equals(msgId)){//下载报警历史
			rsp = new RSP(msgId);
			checkLogin(qc, req, rsp);
			if(rsp.getResult()!=2){
				rsp = appDao.alarmHistoryDownload(qc,req);
			}
			return rsp;
		}else if(ACP_ATTR.equals(msgId)){
			rsp = appDao.compressorAtrrNames(qc);
		}
		//验证登录
		checkLogin(qc, req, rsp);
		return rsp;
	}
	
	private void checkLogin(QC qc,HttpServletRequest req,RSP rsp){
		
		String msgId = qc.getMsgId();
		String phone = qc.getMsgBody().get("PHONENO");
		String key = StringUtil.formatPhoneNO(phone);
		if(LOGIN.equals(msgId)){//登录
			HttpSession session = req.getSession();
			session.getServletContext().setAttribute(key, qc);
			
		}else if(LOGOUT.equals(msgId)){//登出
			HttpSession session = req.getSession();
			session.getServletContext().removeAttribute(key);
		}else{
			String imsi = qc.getImsi();
			HttpSession session = req.getSession();
			QC loginQC = (QC) session.getServletContext().getAttribute(key);
			if(loginQC!=null){
				String loginImsi = loginQC.getImsi();
				if(!loginImsi.equals(imsi)){
					rsp.setError(USER_ISLOGINED);
					rsp.setResult(2);
					rsp.setMsgBody(null);
				}
			}else{
				rsp.setError("用户未登录");
				rsp.setResult(2);
				rsp.setMsgBody(null);
			}
		}
	}

	/**
	 * 查看报警，若新报警，远程推送消息
	 * @param context
	 */
	public void checkAlarms(ServletContext context) {
		long now = new Date().getTime();
		//1:查看所有报警点
		List<Map<String,Object>>results = appDao.getMobileEvents();
		//2：遍历查询点的新值
		//存储号码
		Set<String> tokens = new HashSet<String>();
		for(Map<String,Object>m:results){
			Long time = (Long) m.get("cTime");
			Integer pid = (Integer) m.get("id");
			//报警时间在10分钟内，产生推送消息
			if(time!=null&&((now-time)<=10*60*1000)){
				//1:获取改点对应的手机号
				List<String>phones = appDao.getPhonesByPid(pid);
				//获取报警点对应手机的令牌
				for(String phoneNO:phones){
					String key = StringUtil.formatPhoneNO(phoneNO);
					QC qc = (QC) context.getAttribute(key);
					if(qc==null)continue;
					String imsi = qc.getImsi();
					if(imsi!=null&&imsi.length()==71){
						tokens.add(key);
					}
				}
			}
			
			//更新报警
			appDao.updateMobileEvent(pid,time);
		}
		System.out.println("pushtokenCount:"+tokens.size());
		//远程推送阿婆你是
		for(String s:tokens){
			QC qc = (QC) context.getAttribute(s);
			if(qc==null)continue;
			String imsi = qc.getImsi();
			String imei = qc.getImei();
			if(imsi!=null&&imsi.length()==71){
				ApnsUtil.pushNotification(imsi,imei,"您有一条报警");
			}
		}
	}

}
