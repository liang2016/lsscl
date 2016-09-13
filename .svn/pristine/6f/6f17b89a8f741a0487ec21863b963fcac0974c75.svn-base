package com.lsscl.app.dao.impl;

import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.dao.AppDao;
import com.serotonin.mango.Common;

/**
 * 密码修改
 * @author yxx
 *
 */
public class ChangePwdDao extends AppDao{
	/**
	 * 修改密码
	 * 
	 * @param qc
	 * @return
	 */
	private static final String updatePwdSql = "update users set password = ? where phone = ?";

	public RSP getRSP(QC qc) {
		RSP rsp = new RSP(qc.getMsgId());
		String newPasswd = qc.getMsgBody().get("NEWPASSWD");
		String phoneNo = qc.getMsgBody().get("PHONENO");
		String encodePwd = Common.encrypt(newPasswd);
		ejt.update(updatePwdSql, new Object[] { encodePwd, phoneNo });
		return rsp;
	}
}
