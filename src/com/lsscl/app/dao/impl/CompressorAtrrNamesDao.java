package com.lsscl.app.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lsscl.app.bean.AttrNamesMsgBody;
import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.dao.AppDao;

public class CompressorAtrrNamesDao extends AppDao {
	/**
	 * 单个用户下空压机属性名称列表
	 */
	private static final String acp_attr_names = "select p.name,p.pointId from appAcps a "
			+ "left join appPoints p on a.id = p.acpId "
			+ "where a.scopeId = ?";
	/**
	 * 空压机属性名称
	 * 
	 * @param qc
	 * @return
	 */
	public RSP getRSP(QC qc) {
		String scopeId = qc.getMsgBody().get("SCOPEID");
		List<Map<String, String>> attrs = ejt.query(acp_attr_names,
				new Object[] { scopeId }, new MapResultData());
		RSP rsp = null;
		rsp = new RSP(qc.getMsgId());
		AttrNamesMsgBody msg = new AttrNamesMsgBody();
		Map<String, String> map = new HashMap<String, String>();
		for (Map<String, String> m : attrs) {
			map.put("T" + m.get("pointId"), m.get("name"));
		}
		msg.setAttrs(map);
		rsp.setMsgBody(msg);
		return rsp;
	}

}
