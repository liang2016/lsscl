/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.view.custom.CustomView;
import com.serotonin.mango.vo.User;

/**
 *  
 */
public class CustomViewInitTag extends TagSupport {
    private static final long serialVersionUID = -1;

    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int doStartTag() throws JspException {
        // Check the user id.
        User user = new UserDao().getUser(username);
        if (user == null)
            throw new JspException("Username '" + username + "' not found");
        if (user.isDisabled())
            throw new JspException("Username '" + username + "' is disabled");

        Common.setCustomView((HttpServletRequest) pageContext.getRequest(), new CustomView(user));

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public void release() {
        super.release();
        username = null;
    }
}
