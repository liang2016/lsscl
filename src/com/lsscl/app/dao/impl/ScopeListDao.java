package com.lsscl.app.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.bean.Scope;
import com.lsscl.app.bean.ScopeListMsgBody;
import com.lsscl.app.dao.AppDao;
import com.serotonin.mango.Common;

public class ScopeListDao extends AppDao {


	/**
	 * 获取区域列表
	 * 
	 * @param qc
	 * @return
	 */
	public RSP getRSP(QC qc) {
		RSP rsp = new RSP(qc.getMsgId());
		String scopeId = qc.getMsgBody().get("SCOPEID");
		String isRoot = qc.getMsgBody().get("ISROOT");
		if (scopeId == null) {
			rsp.setError("查询错误");
			rsp.setResult(1);
			return rsp;
		}
		List<Scope> scopes = new ArrayList<Scope>();
		if (isRoot != null && Boolean.parseBoolean(isRoot)) {
			String phone = qc.getMsgBody().get("PHONENO");
			String password = qc.getMsgBody().get("PASSWORD");
			String enPwd = Common.encrypt(password);
			scopes = ejt.query(ScopeRowMapper.getRootScope,
					new Object[] { phone,enPwd }, new ScopeRowMapper());
		} else {
			scopes = ejt.query(ScopeRowMapper.selectByParentId2,
					new Object[] { scopeId }, new ScopeRowMapper());
		}
		ScopeListMsgBody msg = new ScopeListMsgBody();
		msg.setScopes(scopes);
		rsp.setMsgBody(msg);
		return rsp;
	}

}
