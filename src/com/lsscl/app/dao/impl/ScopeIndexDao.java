package com.lsscl.app.dao.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;

import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.bean.ScopeIndexMsgBody;
import com.lsscl.app.dao.AppDao;
import com.lsscl.app.util.AcpConfig;
import com.lsscl.app.util.StringUtil;

public class ScopeIndexDao extends AppDao {
	/**
	 * 区域首页统计信息
	 * 
	 * @param qc
	 * @return
	 */
	private static final String totalAcpsByScopeId = "with ScopeTree AS "
			+ "( " + "SELECT id,scopename,scopetype,parentid FROM scope "
			+ "WHERE id = ? " + "UNION ALL "
			+ "SELECT s.id,s.scopename,s.scopetype,s.parentid FROM "
			+ "ScopeTree t,Scope s " + "WHERE t.id = s.parentid " + ") "
			+ "select a.id from appAcps a "
			+ "left join ScopeTree t on t.id = a.scopeId "
			+ "where t.scopetype = 3";

	public static final String acpStatistics = "{call acpStatistics(?,?,?,?)}";
	private static final String getscopename = "select scopename from scope where id = ?";

	public RSP getRSP(QC qc) {
		RSP rsp = new RSP(qc.getMsgId());
		final String scopeId = qc.getMsgBody().get("SCOPEID");
		if (scopeId == null)
			return rsp;
		int sid = Integer.parseInt(scopeId);
		// 总机器台数
		List<Integer> aids = ejt.query(totalAcpsByScopeId,
				new Object[] { scopeId }, Integer.class);
		int s0 = aids.size();
		// 加载数量 1
		int s1 = getAcpStatistics(sid, AcpConfig.Load_UnLoad, 1);
		// 卸载数量 0
		int s2 = getAcpStatistics(sid, AcpConfig.Load_UnLoad, 0);
		// // 停机数量 0
		int s3 = getAcpStatistics(sid, AcpConfig.RUN_STOP, 0);
		// 报警台数 1
		String scopename = queryForObject(getscopename, new Object[]{scopeId},String.class,"");
		// 运行总功率S6
		double power = 0;
		for (int aid : aids) {
			power += getPowerByAid(aid + "");
		}
		ScopeIndexMsgBody msg = new ScopeIndexMsgBody();
		msg.put("SNAME", scopename);
		msg.put("S0", s0 + "");
		msg.put("S1", s1 + "");
		msg.put("S2", s2 + "");
		msg.put("S3", s3 + "");
		msg.put("S4", "0");
		msg.put("S5", "0");
		msg.put("S6", StringUtil.formatNumber(power / 1000, "0.0"));
		rsp.setMsgBody(msg);
		return rsp;
	}

	/**
	 * 获取状态点（运行/停止、加载/卸载等）空压机数量
	 * 
	 * @param scopeId
	 * @param attrName
	 * @param value
	 * @return
	 */
	private int getAcpStatistics(final int scopeId, final String attrName,
			final int value) {
		int count = (Integer) ejt.execute(acpStatistics,
					new CallableStatementCallback() {

						@Override
						public Object doInCallableStatement(CallableStatement cs)
								throws SQLException, DataAccessException {
							cs.setInt(1, scopeId);
							cs.setString(2, attrName);
							cs.setInt(3, value);
							cs.registerOutParameter(4, Types.INTEGER);
							cs.execute();
							return cs.getObject(4);
						}
					});
		return count;
	}
}
