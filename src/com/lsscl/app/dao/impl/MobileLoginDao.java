package com.lsscl.app.dao.impl;

import java.util.List;
import java.util.Map;

import com.lsscl.app.bean.LoginMsgBody;
import com.lsscl.app.bean.MsgBody;
import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.dao.AppDao;
import com.serotonin.mango.Common;

public class MobileLoginDao extends AppDao {
	private static final String MobileLogin = "select count(*) from users where phone = ? and password = ?";
	
	/**
	 * 登录验证
	 * 
	 * @param qc
	 * @return
	 */
	public RSP getRSP(QC qc) {
		String phone = qc.getMsgBody().get("PHONENO");
		String password = qc.getMsgBody().get("PASSWORD");
		String enPwd = Common.encrypt(password);
		RSP rsp = new RSP(qc.getMsgId());
		int count = queryForObject(MobileLogin, new Object[] { phone, enPwd },
				Integer.class, 0);
		if (count == 1) {//
			String sql = "select s.scopeType,u.username,s.id,s.scopename from scope s "
					+ "left join user_scope us on s.id = us.scopeId "
					+ "left join users u on us.uid = u.id "
					+ "where u.phone = ? and u.password = ?";
			List<Map<String, String>> maps = ejt.query(sql, new Object[] {
					phone, enPwd }, new MapResultData());
			if (maps.size() > 0) {
				String scopeType = maps.get(0).get("scopeType");
				String scopeId = maps.get(0).get("id");
				String username = maps.get(0).get("username");
				String scopename = maps.get(0).get("scopename");
				rsp.setResult(0);
				LoginMsgBody msgBody = new LoginMsgBody();
				msgBody.setScopename(scopename);
				if ("3".equals(scopeType)) {
					msgBody.setUserflag(1);// 工厂用户
					msgBody.setDefaultScopeId(0);
				} else {// 非工厂用户
					msgBody.setUserflag(2);// 渠道用户
					msgBody.setDefaultScopeId(getDefaultScopeId(scopeId));
				}
				if (scopeId != null)
					msgBody.setScopeId(Integer.parseInt(scopeId));
				msgBody.setUsername(username);
				rsp.setMsgBody(msgBody);
			} else {
				rsp.setResult(1);
				rsp.setError("帐号异常，请联系管理员");
				rsp.setMsgBody(new MsgBody());
			}

		} else if (count == 0) {
			rsp = new RSP(qc.getMsgId());
			rsp.setResult(1);
			rsp.setError("用户名或密码不正确");
			rsp.setMsgBody(new MsgBody());
		} else if (count > 1) {
			rsp = new RSP(qc.getMsgId());
			rsp.setResult(1);
			rsp.setError("帐号异常，请联系管理员");
			rsp.setMsgBody(new MsgBody());
		}
		return rsp;
	}
}
