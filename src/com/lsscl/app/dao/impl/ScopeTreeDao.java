package com.lsscl.app.dao.impl;

import java.util.List;
import java.util.Map;

import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.bean.ScopeTreeMsgBody;
import com.lsscl.app.dao.AppDao;
import com.serotonin.mango.Common;

/**
 * 区域树形关系请求
 * 
 * @author yxx
 * 
 */
public class ScopeTreeDao extends AppDao {
	private static final String scopeTree = "with scopeTree " + "as( "
			+ "select id,parentid,scopetype from scope " + "where id in ( "
			+ "select us.scopeid from scope s "
			+ "left join user_scope us on s.id = us.scopeid "
			+ "left join users u on u.id = us.uid "
			+ "where u.phone = ? and u.password = ? and s.scopetype !=0) " + "union ALL "
			+ "select s.id,s.parentid,s.scopetype "
			+ "from scope s,ScopeTree t " + "where s.parentid = t.id " + ") "
			+ "select ID,PARENTID,SCOPETYPE from scopeTree";

	@Override
	public RSP getRSP(QC qc) {
		RSP rsp = new RSP(qc.getMsgId());
		String phoneNo = qc.getMsgBody().get("PHONENO");
		String password = qc.getMsgBody().get("PASSWORD");
		String enPwd = Common.encrypt(password);
		List<Map<String, String>> results = ejt.query(scopeTree, new Object[] {
				phoneNo, enPwd }, new MapResultData());
		ScopeTreeMsgBody msg = new ScopeTreeMsgBody();
		msg.setScopes(results);
		rsp.setMsgBody(msg);
		return rsp;
	}

}
