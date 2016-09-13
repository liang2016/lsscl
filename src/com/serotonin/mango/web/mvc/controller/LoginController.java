/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.mvc.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.web.integration.CrowdUtils;
import com.serotonin.mango.web.mvc.form.LoginForm;
import com.serotonin.util.StringUtils;
import com.serotonin.util.ValidationUtils;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

public class LoginController extends SimpleFormController {
	private static final Log logger = LogFactory.getLog(LoginController.class);

	private boolean mobile;
	private String hqUrl;
	private String zoneUrl;
	private String subzoneUrl;
	private String factoryUrl;
	private String successUrl;
	private String newUserUrl;

	public void setMobile(boolean mobile) {
		this.mobile = mobile;
	}

	public void setNewUserUrl(String newUserUrl) {
		this.newUserUrl = newUserUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public void setHqUrl(String hqUrl) {
		this.hqUrl = hqUrl;
	}

	public void setZoneUrl(String zoneUrl) {
		this.zoneUrl = zoneUrl;
	}

	public void setSubzoneUrl(String subzoneUrl) {
		this.subzoneUrl = subzoneUrl;
	}

	public void setFactoryUrl(String factoryUrl) {
		this.factoryUrl = factoryUrl;
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors,
			@SuppressWarnings("rawtypes")
			Map controlModel) throws Exception {
		// Check this user is logged
		User loggedUser = Common.getUser(request);
		if (loggedUser != null) {// the user is logged
			return redirectHome(loggedUser);
		} else {
			// Check if Crowd is enabled
			if (CrowdUtils.isCrowdEnabled()) {
				String username = CrowdUtils.getCrowdUsername(request);

				if (username != null) {
					((LoginForm) errors.getTarget()).setUsername(username);

					// The user is logged into Crowd. Make sure the username is
					// valid in this instance.
					User user = new UserDao().getUser(username);
					if (user == null)
						ValidationUtils.rejectValue(errors, "username",
								"login.validation.invalidLogin");
					else {
						// Validate some stuff about the user.
						if (user.isDisabled())
							ValidationUtils.reject(errors,
									"login.validation.invalidLogin");
						else {
							if (CrowdUtils.isAuthenticated(request, response)) {
								ModelAndView mav = performLogin(request,
										username);
								CrowdUtils.setCrowdAuthenticated(Common
										.getUser(request));
								return mav;
							}
						}
					}
				}
			}
			return super.showForm(request, response, errors, controlModel);
		}
	}

	@Override
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) {
		LoginForm login = (LoginForm) command;


//		 String yazhengma = login.getYazhengma();  
//	      HttpSession session = request.getSession();  
//	        String buf = (String)session.getAttribute("rand") ;  
//	        if (buf==null) {  
//	        	ValidationUtils.rejectValue(errors, "username",
//				"login.validation.invalidLogin");
//	        	
//	        }else  {  
//	            if (buf.toLowerCase().equals(yazhengma.toLowerCase())) {  
//	            	//no
//	            }else {  
//	                //跳转到登录页面中去  
//	            	ValidationUtils.rejectValue(errors, "username",
//					"login.validation.invalidLogin");
//	            	
//	            }  
//	        }
		
		// Make sure there is a username
		if (StringUtils.isEmpty(login.getUsername()))
			ValidationUtils.rejectValue(errors, "username",
					"login.validation.invalidLogin");

		// Make sure there is a password
		if (StringUtils.isEmpty(login.getPassword()))
			ValidationUtils.rejectValue(errors, "password",
					"login.validation.invalidLogin");
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		LoginForm login = (LoginForm) command;
		
		boolean crowdAuthenticated = false;

		// Check if the user exists
		User user = new UserDao().getUser(login.getUsername());
		if (user == null)
			ValidationUtils.rejectValue(errors, "username",
					"login.validation.invalidLogin");
		else if (user.isDisabled())
			ValidationUtils.reject(errors, "login.validation.invalidLogin");
		else {
			if (CrowdUtils.isCrowdEnabled())
				// First attempt authentication with Crowd.
				crowdAuthenticated = CrowdUtils.authenticate(request, response,
						login.getUsername(), login.getPassword());

			if (!crowdAuthenticated) {
				String passwordHash = Common.encrypt(login.getPassword());

				// Validating the password against the database.
				if (!passwordHash.equals(user.getPassword()))
					ValidationUtils.reject(errors,
							"login.validation.invalidLogin");
			}
		}

		if (errors.hasErrors())
			return showForm(request, response, errors);

		//FIXME 耗时20s
		ModelAndView mav = performLogin(request, login.getUsername());
		if (crowdAuthenticated){
			CrowdUtils.setCrowdAuthenticated(Common.getUser(request));
		}
		return mav;
	}

	private ModelAndView performLogin(HttpServletRequest request,
			String username) throws Exception {
		// Check if the user is already logged in.
		User user = Common.getUser(request);
		if (user != null && user.getUsername().equals(username)) {
			// The user is already logged in. Nothing to do.
			if (logger.isDebugEnabled())
				logger.debug("User is already logged in, not relogging in");
		} else {
			UserDao userDao = new UserDao();
			// Get the user data from the app server.
			user = new UserDao().getUser(username);

			// Update the last login time.
			userDao.recordLogin(user.getId());

			
			
			user.setLoginUrl(request.getRequestURI());
			// Add the user object to the session. This indicates to the rest
			// of the application whether the user is logged in or not.
			
			//FIXME 耗时语句
			Common.setUser(request, user);

			// add the user and user session to the context
			// and remove another user from the context
			ServletContext context = request.getSession().getServletContext();
			HttpSession otherSession = (HttpSession) context
					.getAttribute(username);
			if (otherSession != null){
				if(otherSession.getId()!=request.getSession().getId()){
					try{
						context.removeAttribute(user.getUsername());
						otherSession.invalidate();
					}catch(Exception e){
						logger.error("", e);
						//return new ModelAndView(new RedirectView("logout.htm"));
					}
				}
			}
			HttpSession myession = request.getSession();
			myession.setMaxInactiveInterval(1800);
			context.setAttribute(username, myession);
			if (logger.isDebugEnabled())
				logger.debug("User object added to session");
		}

		// 登陆成功后，将权限信息赋予此用户
		UserDao.addUserRole(user);

		if (!mobile) {
			// if (user.isFirstLogin())
			// return new ModelAndView(new RedirectView(newUserUrl));
			if (!StringUtils.isEmpty(user.getHomeUrl())){
				return new ModelAndView(new RedirectView(user.getHomeUrl()));
			}
		}

		// //返回到 成功后的试图 wathlist.jsp
		// return new ModelAndView(new RedirectView(successUrl));
		return redirectHome(user);
	}

	/**
	 * 跳转到该用户的首页
	 * 
	 * @param user
	 *            用户信息
	 */
	private ModelAndView redirectHome(User user) throws Exception {
		/**
		 * 在此 将根据角色不同，登陆之后现实的默认首页也不同 总部用户--区域列表; 区域用户--子区域列表; 子区域用户--工厂列表;
		 * 工厂用户--观察列表
		 */
		if (user.getHomeScope().getScopetype() == ScopeVO.ScopeTypes.HQ) {// 总部用户
			return new ModelAndView(new RedirectView(hqUrl));
		} else if (user.getHomeScope().getScopetype() == ScopeVO.ScopeTypes.ZONE) {// 区域用户
			return new ModelAndView(new RedirectView(zoneUrl + "?zoneId="
					+ user.getHomeScope().getId()));
		} else if (user.getHomeScope().getScopetype() == ScopeVO.ScopeTypes.SUBZONE) {// 子区域用户
			return new ModelAndView(new RedirectView(subzoneUrl + "?subzoneId="
					+ user.getHomeScope().getId()));
		} else if (user.getHomeScope().getScopetype() == ScopeVO.ScopeTypes.FACTORY) {// 工厂用户
			return new ModelAndView(new RedirectView(factoryUrl + "?factoryId="
					+ user.getHomeScope().getId()));
		} else {
			throw new Exception("login.validation.noRole");
		}
	}
}
