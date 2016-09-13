/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.view.custom.CustomView;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.permission.Permissions;

/**
 *  
 */
abstract public class ViewTagSupport extends TagSupport {
    private static final long serialVersionUID = -1;

    protected CustomView getCustomView() throws JspException {
        CustomView view = Common.getCustomView((HttpServletRequest) pageContext.getRequest());
        if (view == null)
            throw new JspException("No custom view in session. Use the init tag before defining points");
        return view;
    }

    protected DataPointVO getDataPointVO(CustomView view, String xid) throws JspException {
        // Find the point.
        DataPointVO dataPointVO = new DataPointDao().getDataPoint(xid);
        if (dataPointVO == null)
            throw new JspException("Point with XID '" + xid + "' not found");

        // Check that the authorizing user has access to the point.
        Permissions.ensureDataPointReadPermission(view.getAuthorityUser(), dataPointVO);

        return dataPointVO;
    }
}
