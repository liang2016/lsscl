package com.lsscl.app.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lsscl.app.bean.IndexMsgBody;
import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.dao.AppDao;
import com.lsscl.app.util.StringUtil;
/**
 * 工厂请求首页
 * @author yxx
 *
 */
public class MobileIndexDao extends AppDao {
	/**
	 * 空压机总数
	 */
	private static final String acp_total = "select a.id from appAcps a where a.scopeId = ?";
	
	/**
	 * 上次报警时间
	 */
	private static final String lastAlarmTime = "select top 1 m.cTime from mobileEvents m "
			+ "left join appPoints p on pointId = m.id "
			+ "left join appAcps a on a.id = p.acpId "
			+ "where a.scopeId = ? and m.cTime is not null order by m.cTime desc";
	/**
	 * 区域信息
	 */
	private static final String Sql_ScopeInfoById = "select scopename from scope where id = ?";
	/**
	 * 登录首页响应
	 * 
	 * @param qc
	 * @return
	 */
	@Override
	public RSP getRSP(QC qc) {
		String scopeId = qc.getMsgBody().get("SCOPEID");
		RSP rsp = null;
		rsp = new RSP(qc.getMsgId());

		// 查询数据库
		IndexMsgBody msgBody = new IndexMsgBody();
		List<Integer> ids = queryForList(acp_total, new Object[] { scopeId },
				Integer.class);
		int r = 0, alarmCount = 0;
		double p = 0;
		List<Integer> alarmIds = ejt.query(alarm_acps,
				new Object[] { scopeId }, Integer.class);
		for (int id : ids) {
			// 运行停止
			if (isRun(id)) {// 运行
				r++;
				p += getPowerByAid(id + "");
			}
			if (alarmIds.contains(id))
				alarmCount++;
		}

		Map<String, String> factoryInfo = queryForObject(Sql_ScopeInfoById,
				new Object[] { scopeId }, new MapResultData());
		if (factoryInfo.size() != 0) {
			String factoryName = factoryInfo.get("scopename");
			msgBody.setUsername("");
			msgBody.setFactoryName(factoryName);
			msgBody.setTime(StringUtil.formatDate(new Date(),
					"yyyy-MM-dd HH:mm:ss"));
			msgBody.setPower(StringUtil.formatNumber(p / 1000, "0.0"));
			msgBody.setTotal(ids.size());
			msgBody.setOpen(r);
			msgBody.setClose(ids.size() - r);
			msgBody.setAlarm(alarmCount);
			Long time = queryForObject(lastAlarmTime, new Object[] { scopeId },
					Long.class, 0L);
			if (time != 0) {
				String datetime = StringUtil.formatDate(new Date(time),
						"yyyy-MM-dd HH:mm:ss");
				msgBody.setLastAlarmTime(datetime);
			} else {
				msgBody.setLastAlarmTime("   ");
			}

			rsp.setMsgBody(msgBody);
		} else {
			rsp.setError("帐号异常");
			rsp.setResult(2);
		}
		return rsp;
	}
}
