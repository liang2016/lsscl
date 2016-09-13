package com.lsscl.app.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lsscl.app.bean.AcpAlarmInfo;
import com.lsscl.app.bean.AlarmInfo;
import com.lsscl.app.bean.MsgBody;
import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.dao.AppDao;
import com.lsscl.app.util.StringUtil;

public class AlarmHistoryDao extends AppDao {

	/**
	 * 查询单个空压机的报警事件
	 */
	private static final String select_alarmByAcpId = "select m.id,m.cTime,p.name from mobileEvents m "
			+ "left join appPoints p on p.pointId = m.id "
			+ "left join appAcps a on a.id = p.acpId " + "where a.id = ? ";
	/**
	 * 报警推送历史
	 * 
	 * @param qc
	 * @return
	 */
	@Override
	public RSP getRSP(QC qc) {
		RSP rsp = null;
		rsp = new RSP(qc.getMsgId());
		rsp.setRspTime(StringUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		String scopeId = qc.getMsgBody().get("SCOPEID");
		List<Integer> acpIds = queryForList(alarm_acps,
				new Object[] { scopeId }, Integer.class);
		Map<String, AcpAlarmInfo> map = new HashMap<String, AcpAlarmInfo>();
		for (Integer aid : acpIds) {
			AcpAlarmInfo info = new AcpAlarmInfo();
			info.setAid(aid);
			List<AlarmInfo> infos = new ArrayList<AlarmInfo>();
			info.setInfos(infos);
			List<Map<String, Object>> list = ejt.query(select_alarmByAcpId,
					new Object[] { aid },
					new com.serotonin.mango.vo.ResultData());
			for (Map<String, Object> m : list) {
				Integer id = (Integer) m.get("id");
				Long cTime = (Long) m.get("cTime");
				if (cTime != null) {// 有报警
					// 查询当前报警值
					String sql = "select top 1 * from pointValues_" + id
							+ " order by id desc";
					Map<String, Object> point = queryForObject(sql, null,
							new com.serotonin.mango.vo.ResultData(),
							new HashMap<String, Object>());
					double data = (Double) point.get("pointValue");
					long ts = (Long) point.get("ts");
					if (data == 1) {// 有报警
						AlarmInfo ai = new AlarmInfo();
						ai.setPid(id + "");
						ai.setcTime(cTime + "");
						ai.seteTime(ts + "");
						infos.add(ai);
					} else if (data == 0) {// 无报警
						ejt.update(update_mobileEvents,
								new Object[] { null, id });
					}
				}
			}
			map.put(aid + "", info);
		}
		MsgBody msg = new MsgBody();
		msg.setAlarms(map);
		rsp.setMsgBody(msg);
		return rsp;
	}

}
