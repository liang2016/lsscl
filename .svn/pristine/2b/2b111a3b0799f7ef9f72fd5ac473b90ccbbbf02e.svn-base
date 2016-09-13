package com.serotonin.mango.web.servlet;

import java.io.IOException;
import java.util.ResourceBundle;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.serotonin.mango.rt.dataImage.IdPointValueTime;
import com.serotonin.db.MappedRowCallback;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.PointValueDao;
import com.serotonin.mango.rt.dataImage.AnnotatedPointValueTime;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.mango.vo.report.ReportCsvStreamer;
import com.serotonin.mango.vo.report.ReportDataValue;
import com.serotonin.mango.vo.report.ReportPointInfo;
import com.serotonin.mango.web.dwr.beans.DataExportDefinition;

public class ChartExportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		User user = Common.getUser(request);
		if (user == null)
			return;
		DataExportDefinition def = user.getDataExportDefinition();
		if (def == null)
			return;

		DataPointDao dataPointDao = new DataPointDao();
		PointValueDao pointValueDao = new PointValueDao();

		long from = def.getFrom() == null ? -1 : def.getFrom().getMillis();
		long to = def.getTo() == null ? System.currentTimeMillis() : def
				.getTo().getMillis();
		// get Points Names
		List<DataPointVO> pointNames=new ArrayList<DataPointVO>();
		for (int i = 0; i < def.getPointIds().length; i++) {
			DataPointVO dp = dataPointDao.getDataPoint(def.getPointIds()[i]);
			pointNames.add(dp);
		}

		// Stream the content.
		response.setContentType("text/csv;charset=GBK");

		final ResourceBundle bundle = Common.getBundle();
		final ReportCsvStreamer exportCreator = new ReportCsvStreamer(response
				.getWriter(), bundle, pointNames);

		final ReportDataValue rdv = new ReportDataValue();
		final  ReportDataValue oldRdv= new ReportDataValue();;
		MappedRowCallback<IdPointValueTime> callback = new MappedRowCallback<IdPointValueTime>() {
			@Override
			public void row(IdPointValueTime pvt, int rowIndex) {
				rdv.setReportPointId(pvt.getDataPointId());
				rdv.setTime(pvt.getTime());
				rdv.setValue(pvt.getValue());
				PointValueTime temp=pvt;
				if (temp instanceof AnnotatedPointValueTime)
					rdv.setAnnotation(((AnnotatedPointValueTime) temp)
							.getAnnotation(bundle));
				else
					rdv.setAnnotation(null);
				exportCreator.pointData(rdv,oldRdv);
				oldRdv.setReportPointId(rdv.getReportPointId());
				oldRdv.setTime(rdv.getTime());
				oldRdv.setValue(rdv.getValue());
			}
		};

//		for (int pointId : def.getPointIds()) {
//			DataPointVO dp = dataPointDao.getDataPoint(pointId);
//			if (Permissions.hasDataPointReadPermission(user, dp)) {
//				ReportPointInfo pointInfo = new ReportPointInfo();
//				pointInfo.setPointName(dp.getName());
//				pointInfo.setDeviceName(dp.getDeviceName());
//				pointInfo.setTextRenderer(dp.getTextRenderer());
//				exportCreator.startPoint(pointInfo);
				List<Integer> points=new ArrayList<Integer>();
				for (int pointId : def.getPointIds()) {
					points.add(pointId);
				}
				pointValueDao
						.getPointValuesBetween(points, from, to, callback);
//			}
//		}

		exportCreator.done();
	}
}
