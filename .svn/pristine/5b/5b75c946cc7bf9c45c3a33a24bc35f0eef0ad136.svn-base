package com.lsscl.app.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lsscl.app.bean.CompressorInfo;
import com.lsscl.app.bean.CompressorListMsgBody;
import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.dao.AppDao;
import com.lsscl.app.util.AcpConfig;

public class CompressorListDao extends AppDao {

	
	/**
	 * 空压机列表
	 * 
	 * @param qc
	 * @return
	 */
	@Override
	public RSP getRSP(QC qc) {
		String scopeId = qc.getMsgBody().get("SCOPEID");
		RSP rsp = null;
		rsp = new RSP(qc.getMsgId());
		CompressorListMsgBody msgBody = new CompressorListMsgBody();
		List<Map<String, String>> list = ejt.query(acp_list,
				new Object[] { scopeId }, new MapResultData());
		List<CompressorInfo> infos = new ArrayList<CompressorInfo>();
		List<Integer> acpids = ejt.query(alarm_acps, new Object[] { scopeId },
				Integer.class);
		for (Map<String, String> m : list) {
			CompressorInfo info = new CompressorInfo();
			info.setCompressorId(m.get("id"));
			String acpname = getAcpName(m);
			info.setCompressorName(acpname);
			// 空压机id
			String id = m.get("id");
			// 排气压力
			String data1 = getPointValueByAcpIdAndName(id,
					AcpConfig.EXHAUSPRESSURE);
			info.setExhausPressure(data1 == null ? "   " : data1);
			// 排气温度
			data1 = getPointValueByAcpIdAndName(id, AcpConfig.EXHAUSTEMPERATURE);
			info.setExhausTemperature(data1 == null ? "   " : data1);

			info.setRunState((isRun(Integer.valueOf(id)) ? "1" : "0"));
			/**
			 * 遍历所有报警
			 */
			for (Integer aid : acpids) {
				if (("" + aid).equals(id)) {
					info.setAlarmFlag("1");
				}
			}
			infos.add(info);
		}
		msgBody.setCompressorInfos(infos);
		rsp.setMsgBody(msgBody);
		return rsp;
	}

}
