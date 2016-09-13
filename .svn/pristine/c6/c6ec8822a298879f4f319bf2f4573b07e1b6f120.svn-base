/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.servlet;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.ReportDao;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.mango.vo.report.EventCsvStreamer;
import com.serotonin.mango.vo.report.ReportCsvStreamer;
import com.serotonin.mango.vo.report.ReportInstance;
import com.serotonin.mango.vo.report.UserCommentCsvStreamer;

/**
 *  
 */
abstract public class ReportExportBase extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected static final int CONTENT_REPORT = 1;
    protected static final int CONTENT_EVENTS = 2;
    protected static final int CONTENT_COMMENTS = 3;

    protected void execute(HttpServletRequest request, HttpServletResponse response, int content) throws IOException {
        // Get the report instance id
        int instanceId = Integer.parseInt(request.getParameter("instanceId"));

        // Get the report instance
        ReportDao reportDao = new ReportDao();
        ReportInstance instance = reportDao.getReportInstance(instanceId);

        // Ensure the user is allowed access.
        Permissions.ensureReportInstancePermission(Common.getUser(request), instance);

        // Stream the content.
        response.setContentType("text/csv;charset=GBK");

        ResourceBundle bundle = Common.getBundle();
        if (content == CONTENT_REPORT) {
        	////
        	List<DataPointVO> pointNames=reportDao.getPointsByInstanceId(instanceId);;
            ReportCsvStreamer creator = new ReportCsvStreamer(response.getWriter(), bundle,pointNames);
            reportDao.reportInstanceData(instanceId, creator);
        }
        else if (content == CONTENT_EVENTS)
            new EventCsvStreamer(response.getWriter(), reportDao.getReportInstanceEvents(instanceId), bundle);
        else if (content == CONTENT_COMMENTS)
            new UserCommentCsvStreamer(response.getWriter(), reportDao.getReportInstanceUserComments(instanceId),
                    bundle);
    }
}
