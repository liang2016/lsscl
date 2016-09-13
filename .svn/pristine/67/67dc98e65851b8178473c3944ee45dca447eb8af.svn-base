package com.serotonin.mango.web.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.Common;

public class FactorySettingController extends AbstractController{
	private String hasLogin;
	private String noLogin;
	public String getHasLogin() {
		return hasLogin;
	}
	public void setHasLogin(String hasLogin) {
		this.hasLogin = hasLogin;
	}
	public String getNoLogin() {
		return noLogin;
	}
	public void setNoLogin(String noLogin) {
		this.noLogin = noLogin;
	}
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) {
		// Check if the user is logged in.
		User user = Common.getUser(request);
		if (user != null) {
			return new ModelAndView(hasLogin);
			// return new ModelAndView(new RedirectView(redirectUrl));
		}

		// Regardless of what happened above, forward to the configured view.
		return new ModelAndView(noLogin);
	}
}
