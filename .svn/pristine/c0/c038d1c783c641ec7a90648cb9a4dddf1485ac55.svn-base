package com.serotonin.mango;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.serotonin.mango.vo.User;

public class MangoSessionListener implements HttpSessionListener{
	
	private static final String SESSION_USER = "sessionUser";

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		// remove the user and user session from the context
		HttpSession session = sessionEvent.getSession();
        ServletContext context = session.getServletContext();
        User user = (User) session.getAttribute(SESSION_USER);
        if(user!=null) context.removeAttribute(user.getUsername());
	}

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		// TODO Auto-generated method stub
	}

}
