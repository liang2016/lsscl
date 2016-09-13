package com.serotonin.mango.web.dwr;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.serotonin.mango.vo.factory.FactorySetting;
import com.serotonin.mango.vo.event.EventHandlerVO;
import com.serotonin.mango.web.dwr.EventHandlersDwr;

import com.serotonin.mango.vo.User;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.MailingListDao;
import com.serotonin.mango.db.dao.scope.ScopeSettingDao;
import com.serotonin.mango.web.dwr.beans.RecipientListEntryBean;
import com.serotonin.web.dwr.DwrResponseI18n;
/**
 * 工厂配置设置(主要用于设置工厂配置的共同属性,暂时[邮件报警])
 * 
 * @author 刘建坤
 * 
 */
public class FactorySettingDwr extends BaseDwr {
	/**
	 * emailhandler邮件发送配置
	 */
	public DwrResponseI18n save(int id,int factoryId,
			List<RecipientListEntryBean> activeRecipients,
			boolean sendEscalation, int escalationDelayType,
			int escalationDelay,
			List<RecipientListEntryBean> escalationRecipients,
			boolean sendEscalation2, int escalationDelayType2,
			int escalationDelay2,
			List<RecipientListEntryBean> escalationRecipients2,
			boolean sendInactive, boolean inactiveOverride,
			List<RecipientListEntryBean> inactiveRecipients, boolean userSMS) {
		DwrResponseI18n response = new DwrResponseI18n();
		EventHandlerVO handler = new EventHandlerVO();
		
		handler.setHandlerType(EventHandlerVO.TYPE_EMAIL);
		handler.setId(id);
		handler.setActiveRecipients(activeRecipients);
		handler.setSendEscalation(sendEscalation);
		handler.setEscalationDelayType(escalationDelayType);
		handler.setEscalationDelay(escalationDelay);
		handler.setEscalationRecipients(escalationRecipients);
		handler.setSendEscalation2(sendEscalation2);
		handler.setEscalationDelayType2(escalationDelayType2);
		handler.setEscalationDelay2(escalationDelay2);
		handler.setEscalationRecipients2(escalationRecipients2);
		handler.setSendInactive(sendInactive);
		handler.setInactiveOverride(inactiveOverride);
		handler.setInactiveRecipients(inactiveRecipients);
		handler.setUseSMS(userSMS);
		
		ScopeSettingDao scopeSettingDao = new ScopeSettingDao();
		handler.validate(response);
		if (!response.getHasMessages()) {
			handler = scopeSettingDao.saveSendSetting(id, factoryId, handler);
			response.addData("handler", handler);
		}
		return response;
	}

	/**
	 * 初始化工厂设置页面
	 */
	public void Init() {

	}

	/**
	 * 根据工厂编号获得用户设置
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> getSettingById(int id) {
		UserDao userDao = new UserDao();
		User user = Common.getUser();
		int scopeId = user.getCurrentScope().getId();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("mailingLists", new MailingListDao().getMailingLists(scopeId));
		
		model.put("users", userDao.getUsers(scopeId));
	
		model.put("setting", new ScopeSettingDao().getScopeSendSetting(scopeId));
		return model;
	}

}
