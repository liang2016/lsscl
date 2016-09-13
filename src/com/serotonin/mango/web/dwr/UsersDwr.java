/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContextFactory;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import com.serotonin.mango.rt.maint.work.EmailWorkItem;
import com.serotonin.mango.vo.DataPointNameComparator;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.mango.vo.permission.DataPointAccess;
import com.serotonin.mango.vo.permission.PermissionException;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.mango.web.email.MangoEmailContent;
import com.serotonin.util.StringUtils;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.I18NUtils;
import com.serotonin.web.i18n.LocalizableMessage;
import com.serotonin.mango.vo.EmailTempVO;
import com.serotonin.mango.db.dao.EmailTempDao;
import java.util.Date;
import com.serotonin.mango.db.dao.power.UserEventLimtDao;

import com.serotonin.mango.db.dao.SystemSettingsDao;

import microsoft.exchange.webservices.data.EmailAddress;
import com.serotonin.mango.rt.exchange.Content;
import com.serotonin.mango.rt.exchange.ExchangeSend;

public class UsersDwr extends BaseDwr {
	public Map<String, Object> getInitData(int scopeId, int scopeType) {
		Map<String, Object> initData = new HashMap<String, Object>();

		User user = Common.getUser();
		ScopeDao scopeDao = new ScopeDao();
		List<ScopeVO> scopeList = scopeDao.getChildScope(scopeId, scopeType);
		// 这里暂时查询总部
		initData.put("zoneList", scopeList);
		if (Permissions.hasAdmin(user)) {
			// Users
			initData.put("admin", true);
			if (scopeId != 0 && scopeType != 3) {
				initData.put("users", new UserDao().getUsers(scopeId));
			} else {
				initData.put("users", new UserDao().getUsers(scopeId));
				// Data sources
				List<DataSourceVO<?>> dataSourceVOs = new DataSourceDao()
						.getDataSources();
				List<Map<String, Object>> dataSources = new ArrayList<Map<String, Object>>(
						dataSourceVOs.size());
				Map<String, Object> ds, dp;
				List<Map<String, Object>> points;
				DataPointDao dataPointDao = new DataPointDao();
				for (DataSourceVO<?> dsvo : dataSourceVOs) {
					ds = new HashMap<String, Object>();
					ds.put("id", dsvo.getId());
					ds.put("name", dsvo.getName());
					points = new LinkedList<Map<String, Object>>();
					for (DataPointVO dpvo : dataPointDao.getDataPoints(
							dsvo.getId(), DataPointNameComparator.instance)) {
						dp = new HashMap<String, Object>();
						dp.put("id", dpvo.getId());
						dp.put("name", dpvo.getName());
						dp.put("settable", dpvo.getPointLocator().isSettable());
						points.add(dp);
					}
					ds.put("points", points);
					dataSources.add(ds);
				}
				initData.put("dataSources", dataSources);
			}
		} else
			initData.put("user", user);

		return initData;
	}

	public User getUser(int id) {
		Permissions.ensureAdmin();
		if (id == Common.NEW_ID) {
			User user = new User();
			user.setDataSourcePermissions(new ArrayList<Integer>(0));
			user.setDataPointPermissions(new ArrayList<DataPointAccess>(0));
			return user;
		}
		return new UserDao().getUser(id);
	}

	// 查询用户管理的范围
	public List<ScopeVO> getUserZoneList(int userId) {
		ScopeDao scopeDao = new ScopeDao();
		return scopeDao.getUserZoneList(userId);
	}

	public DwrResponseI18n saveUserAdmin(int id, String username,
			String password, String email, String phone, boolean admin,
			boolean disabled, int receiveAlarmEmails,
			boolean receiveOwnAuditEvents, List<Integer> dataSourcePermissions,
			List<DataPointAccess> dataPointPermissions, int scopeType) {
		Permissions.ensureAdmin();
		DwrResponseI18n response = new DwrResponseI18n();
		// Validate the given information. If there is a problem, return an
		// appropriate error message.
		HttpServletRequest request = WebContextFactory.get()
				.getHttpServletRequest();
		User currentUser = Common.getUser(request);
		int homeScopeId = currentUser.getCurrentScope().getId();
		UserDao userDao = new UserDao();
		User user;
		if (id == Common.NEW_ID) {
			if (currentUser.getCurrentScope().getScopetype() == ScopeVO.ScopeTypes.FACTORY) {
				if (userDao.getUserLimit(currentUser.getCurrentScope().getId()) <= 0) {
					response.addMessage(new LocalizableMessage(
							"users.validate.userCount.Limit"));
				}
			}
			user = new User();
		} else
			user = userDao.getUser(id);
		user.setUsername(username);
		if (!StringUtils.isEmpty(password))
			user.setPassword(Common.encrypt(password));
		user.setEmail(email);
		user.setPhone(phone);
		user.setAdmin(admin);
		user.setDisabled(disabled);
		user.setReceiveAlarmEmails(receiveAlarmEmails);
		user.setReceiveOwnAuditEvents(receiveOwnAuditEvents);
		user.setDataSourcePermissions(dataSourcePermissions);
		user.setDataPointPermissions(dataPointPermissions);
		// 添加用户范围
		ScopeVO scope = new ScopeVO();
		scope.setScopetype(scopeType);
		scope.setId(homeScopeId);
		user.setHomeScope(scope);

		user.validate(response);

		// Check if the username is unique.
		User dupUser = userDao.getUser(username);
		if (id == Common.NEW_ID && dupUser != null)
			response.addMessage(new LocalizableMessage(
					"users.validate.usernameUnique"));
		else if (dupUser != null && id != dupUser.getId())
			response.addMessage(new LocalizableMessage(
					"users.validate.usernameInUse"));
		// Check if the email address is unique
		boolean isExist = userDao.emailAddrIsExist(id, email);
		if (isExist) {
			response.addMessage(new LocalizableMessage(
					"users.validate.emailUnique"));
		}
		// 验证手机号码
		if (!response.getHasMessages()) {
			isExist = userDao.phoneIsExist(id, phone);
			if (isExist) {
				response.addMessage(new LocalizableMessage(
						"users.validate.phoneUnique"));
			}
		}

		// Cannot make yourself disabled or not admin
		if (currentUser.getId() == id) {
			if (!admin)
				response.addMessage(new LocalizableMessage(
						"users.validate.adminInvalid"));
			if (disabled)
				response.addMessage(new LocalizableMessage(
						"users.validate.adminDisable"));
		}

		if (!response.getHasMessages()) {
			userDao.saveUser(user);

			if (currentUser.getId() == id) {
				user.setCurrentScope(user.getCurrentScope());
				userDao.addUserRole(user);
				// Update the user object in session too. Why not?
				Common.setUser(request, user);
			}
			response.addData("userId", user.getId());
		}

		return response;
	}

	// scope添加用户
	public DwrResponseI18n saveScopeUserAdmin(int id, String username,
			String password, String email, String phone, boolean admin,
			boolean disabled, int receiveAlarmEmails,
			boolean receiveOwnAuditEvents, List<Integer> scopeId,
			List<Boolean> isSets, int scopetype) {
		Permissions.ensureAdmin();

		// Validate the given information. If there is a problem, return an
		// appropriate error message.
		HttpServletRequest request = WebContextFactory.get()
				.getHttpServletRequest();
		User currentUser = Common.getUser(request);
		UserDao userDao = new UserDao();
		int homeScopeId = currentUser.getCurrentScope().getId();
		User user;
		if (id == Common.NEW_ID)
			user = new User();
		else
			user = userDao.getUser(id);
		user.setUsername(username);
		if (!StringUtils.isEmpty(password))
			user.setPassword(Common.encrypt(password));
		user.setEmail(email);
		user.setPhone(phone);
		user.setAdmin(admin);
		user.setDisabled(disabled);
		user.setReceiveAlarmEmails(receiveAlarmEmails);
		user.setReceiveOwnAuditEvents(receiveOwnAuditEvents);
		// 用户注册范围
		ScopeVO scope = new ScopeVO();
		scope.setScopetype(scopetype);
		scope.setId(homeScopeId);
		user.setHomeScope(scope);

		DwrResponseI18n response = new DwrResponseI18n();
		user.validate(response);

		// Check if the username is unique.
		User dupUser = userDao.getUser(username);
		if (id == Common.NEW_ID && dupUser != null)
			response.addMessage(new LocalizableMessage(
					"users.validate.usernameUnique"));
		else if (dupUser != null && id != dupUser.getId())
			response.addMessage(new LocalizableMessage(
					"users.validate.usernameInUse"));
		// Check if the email address is unique
		if (!response.getHasMessages()) {
			boolean isExist = userDao.emailAddrIsExist(id, email);
			if (isExist) {
				response.addMessage(new LocalizableMessage(
						"users.validate.emailUnique"));
			}
		}
		// 验证手机号码
		if (!response.getHasMessages()) {
			boolean isExist = userDao.phoneIsExist(id, phone);
			if (isExist) {
				response.addMessage(new LocalizableMessage(
						"users.validate.phoneUnique"));
			}
		}
		// Cannot make yourself disabled or not admin
		if (currentUser.getId() == id) {
			if (!admin)
				response.addMessage(new LocalizableMessage(
						"users.validate.adminInvalid"));
			if (disabled)
				response.addMessage(new LocalizableMessage(
						"users.validate.adminDisable"));
		}

		if (!response.getHasMessages()) {
			userDao.saveUser(user);
			userDao.saveUserScope(user.getId(), scopeId, isSets);
			if (currentUser.getId() == id) {
				user.setCurrentScope(user.getCurrentScope());
				userDao.addUserRole(user);

				// Update the user object in session too. Why not?
				Common.setUser(request, user);
			}
			response.addData("userId", user.getId());
		}
		return response;
	}

	public DwrResponseI18n saveUser(int id, String password, String email,
			String phone, int receiveAlarmEmails, boolean receiveOwnAuditEvents) {
		HttpServletRequest request = WebContextFactory.get()
				.getHttpServletRequest();
		User user = Common.getUser(request);
		if (user.getId() != id)
			throw new PermissionException("Cannot update a different user",
					user);

		UserDao userDao = new UserDao();
		User updateUser = userDao.getUser(id);
		if (!StringUtils.isEmpty(password))
			updateUser.setPassword(Common.encrypt(password));
		updateUser.setEmail(email);
		updateUser.setPhone(phone);
		updateUser.setReceiveAlarmEmails(receiveAlarmEmails);
		updateUser.setReceiveOwnAuditEvents(receiveOwnAuditEvents);

		DwrResponseI18n response = new DwrResponseI18n();
		updateUser.validate(response);
		// Check if the email address is unique
		if (!response.getHasMessages()) {
			boolean isExist = userDao.emailAddrIsExist(id, email);
			if (isExist) {
				response.addMessage(new LocalizableMessage(
						"users.validate.emailUnique"));
			}
		}
		// 验证手机号码
		if (!response.getHasMessages()) {
			boolean isExist = userDao.phoneIsExist(id, phone);
			if (isExist) {
				response.addMessage(new LocalizableMessage(
						"users.validate.phoneUnique"));
			}
		}
		if (!response.getHasMessages()) {
			userDao.saveUser(user);
			updateUser.setCurrentScope(user.getHomeScope());
			userDao.addUserRole(updateUser);
			// Update the user object in session too. Why not?
			Common.setUser(request, updateUser);
		}

		return response;
	}

	public Map<String, Object> sendTestEmail(String email, String username) {
		Permissions.ensureAdmin();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			ResourceBundle bundle = Common.getBundle();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("message", new LocalizableMessage("ftl.userTestEmail",
					username));
			if (SystemSettingsDao.isExchange()) {
				List<EmailAddress> address = new ArrayList<EmailAddress>();
				EmailAddress add = new EmailAddress();
				add.setAddress(email);
				address.add(add);
				Content exchangeContent = new Content("testEmail", model,
						bundle, I18NUtils.getMessage(bundle, "ftl.testEmail"),
						Common.UTF8);
				ExchangeSend exchangeSend = new ExchangeSend(address,
						exchangeContent);
				exchangeSend.sendMail();

			} else {
				MangoEmailContent cnt = new MangoEmailContent("testEmail",
						model, bundle, I18NUtils.getMessage(bundle,
								"ftl.testEmail"), Common.UTF8);
				EmailWorkItem.queueEmail(email, cnt);
			}

			result.put("message", new LocalizableMessage(
					"common.testEmailSent", email));
			User user = Common.getUser();
			EmailTempDao tempDao = new EmailTempDao();
			EmailTempVO tempVO = new EmailTempVO();
			tempVO.setUid(user.getId());
			tempVO.setEmailAddress(email.toString());
			tempVO.setTs(new Date().getTime());
			tempDao.saveEmailTemp(tempVO);

		} catch (Exception e) {
			result.put("exception", e.getMessage());
		}
		return result;
	}

	public DwrResponseI18n deleteUser(int id) {
		Permissions.ensureAdmin();
		DwrResponseI18n response = new DwrResponseI18n();
		User currentUser = Common.getUser();
		UserDao userDao = new UserDao();
		if (currentUser.getId() == id)
			// You can't delete yourself.
			response.addMessage(new LocalizableMessage(
					"users.validate.badDelete"));
		else {
			User user = userDao.getUserForLimt(id);
			if (user == null) {
				response.addMessage(new LocalizableMessage(
						"users.validate.error"));
			}
			if (user.isAdmin()) {
				response.addMessage(new LocalizableMessage(
						"users.validate.badDeleteforAdmin"));
			} else if (user.getHomeScope().getScopetype() == 3) {
				userDao.deleteCount(user.getHomeScope().getId());
				new UserEventLimtDao().deleteCount(user.getHomeScope().getId());
			}
			userDao.deleteUser(user.getId());
		}

		return response;
	}

	private void validatePhone(DwrResponseI18n response, UserDao dao) {

	}
}
