package com.lsscl.app.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lsscl.app.bean.PointsStatisticsMsgBody;
import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.dao.AppDao;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.view.text.TextRenderer;
import com.serotonin.mango.vo.DataPointVO;

public class PointsStatisticsDao extends AppDao {

	@Override
	public RSP getRSP(QC qc) {
		RSP rsp = new RSP(qc.getMsgId());
		String pid = qc.getMsgBody().get("PID");
		String count = qc.getMsgBody().get("COUNT");
		int pointId = Integer.parseInt(pid);
		DataPointVO pv = new DataPointDao().getDataPoint(pointId);
		TextRenderer tr = pv.getTextRenderer();
		String sql = "select * from(select top " + count
				+ " id,dataType,pointValue,ts from pointValues_" + pid
				+ " order by id desc)tt order by id asc";
		List<Map<String, Object>> rows = ejt.query(sql, new Object[] {},
				new com.serotonin.mango.vo.ResultData());
		String unit = "单位：" + tr.getMetaText();// 单位
		if (rows.size() == 0) {
			unit = "没有数据";
		}
		List<Double>points = new ArrayList<Double>();
		Integer type = 0;
		if(rows.size()!=0)type = (Integer) rows.get(0).get("dataType");
		if (type == DataTypes.BINARY) {// 二进制数据
			unit = "0:" + tr.getText(false, 2) + ", 1:" + tr.getText(true, 2)
					+ ", -1:无数据";
		}
		
		PointsStatisticsMsgBody msgBody = new PointsStatisticsMsgBody();
		msgBody.setTitle("");
		msgBody.setSubTitle(unit);
		msgBody.setPoints(rows);
		msgBody.setDataType(type);
		msgBody.setImageType(qc.getMsgBody().get("IMGTYPE"));
		rsp.setMsgBody(msgBody);

		return rsp;
	}

}
