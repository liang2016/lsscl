package com.serotonin.mango.web.dwr.scope;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.event.EventHandlerVO;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.scope.ScopeSettingDao;
import com.serotonin.mango.web.dwr.beans.RecipientListEntryBean;
import com.serotonin.web.i18n.LocalizableMessage;
import com.serotonin.mango.db.dao.power.UserEventLimtDao;
import com.serotonin.mango.db.dao.MailingListDao;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.web.dwr.BaseDwr;

public class SendSettingDwr extends BaseDwr {
	public Map<String, Object> getInitData() {
		UserDao userDao = new UserDao();
		User user = Common.getUser();
		int scopeId = user.getCurrentScope().getId();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("mailingLists", new MailingListDao().getMailingLists(scopeId));
		model.put("users", userDao.getUsers(scopeId));
		model.put("setting", new ScopeSettingDao().getScopeSendSetting(scopeId));
		return model;
	}

	public DwrResponseI18n saveSendSetting(int id, int scopeId, int scopeType,
			List<RecipientListEntryBean> activeRecipients) {
		DwrResponseI18n response = new DwrResponseI18n();
		EventHandlerVO handler = new EventHandlerVO();
		handler.setHandlerType(EventHandlerVO.TYPE_EMAIL);
		handler.setId(id);
		if (scopeType == 2)// 子区域
			handler.setActiveRecipients(activeRecipients);
		if (scopeType == 1) {// 区域
			handler.setActiveRecipients(activeRecipients);// 可忽略(过滤验证)
			handler.setEscalationDelay(4);// 可忽略
			handler.setEscalationRecipients(activeRecipients);
		}
		if (scopeType == 0) {// 总部
			handler.setEscalationDelay2(4);// 可忽略
			handler.setActiveRecipients(activeRecipients);// 可忽略
			handler.setEscalationRecipients2(activeRecipients);
		}
		ScopeSettingDao scopeSettingDao = new ScopeSettingDao();
		handler.validate(response);
		if (!response.getHasMessages()) {
			handler = scopeSettingDao.saveSendSetting(id, scopeId, handler);
			response.addData("handler", handler);
		}
		return response;
	}

	
	
	
	
	/**
	 * 修改用户事件处理器设置上限
	 * 
	 * @param userId
	 *            用户编号
	 * @param limit
	 *            上限
	 * @return
	 */
	public DwrResponseI18n updateUserEventhandlerLimit(int userId, int limit) {
		DwrResponseI18n response = new DwrResponseI18n();
		UserEventLimtDao UELDao = new UserEventLimtDao();
		if (limit < 0)
			limit = 0;
		if(limit >100)
			response.addMessage(new LocalizableMessage("users.handlerLimit.overtake"));
		if(!response.getHasMessages())
			UELDao.updateUserEventHandler(userId, limit);
		return response;
	}

	/**
	 * 批量修改用户事件处理器设置上限
	 * 
	 * @param userId
	 *            用户编号
	 * @param limit
	 *            上限
	 * @return
	 */
	public DwrResponseI18n updateAllUserEventhandlerLimit(int[][] userLimits) {
		DwrResponseI18n response = new DwrResponseI18n();
		UserEventLimtDao UELDao = new UserEventLimtDao();
		for (int i = 0; i < userLimits.length; i++) {
			if(userLimits[i][1]>100){
				response.addMessage(new LocalizableMessage(
				"users.handlerLimit.overtake"));
				break;
			}
			if(!response.getHasMessages())
				UELDao.updateUserEventHandler(userLimits[i][1], userLimits[i][0]);
		}
		return response;
	}
}
