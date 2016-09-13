package com.serotonin.mango.rt.event.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.serotonin.mango.vo.event.EventHandlerVO;
import com.serotonin.mango.db.dao.scope.ScopeSettingDao;
import com.serotonin.mango.web.dwr.beans.RecipientListEntryBean;
import com.serotonin.mango.db.dao.scope.ScopeDao;
public class EmailAddressHelper {
	/**
	 * 取出子区域警报收信人区域设置
	 * 
	 * @param scopeId
	 * @return
	 */
	private  List<RecipientListEntryBean> getSubZoneRecipients(int scopeId) {
		List<RecipientListEntryBean> activeList = new ArrayList<RecipientListEntryBean>();
		EventHandlerVO handler = new ScopeSettingDao()
				.getScopeSendSetting(scopeId);
		activeList = handler.getActiveRecipients();
		return activeList;
	}
	/**
	 * 取出区域收信人设置
	 * 
	 * @return
	 */
	private List<RecipientListEntryBean> getZoneRecipients(int scopeId) {
		List<RecipientListEntryBean> escalationList = new ArrayList<RecipientListEntryBean>();
		EventHandlerVO handler = new ScopeSettingDao()
		.getScopeSendSetting(scopeId);
		escalationList= handler.getEscalationRecipients();
		return escalationList;
	}
	
	/**
	 * 取出总部收信人设置
	 * 
	 * @return
	 */
	private List<RecipientListEntryBean> getCenterRecipients(int scopeId) {
		List<RecipientListEntryBean> escalationList2 = new ArrayList<RecipientListEntryBean>();
		EventHandlerVO handler = new ScopeSettingDao()
		.getScopeSendSetting(scopeId);//这里总部id为1
		escalationList2 = handler.getEscalationRecipients2();
		return escalationList2;
	}
	public Map<String,Object> getFactoryParentSeeting(int scopeId){
		 Map<String,Object> map=new HashMap<String,Object>();
		 ScopeDao scopeDao=new ScopeDao();
		 int subZoneId=scopeDao.getScopeParentId(scopeId);
		 int zoneId=scopeDao.getScopeParentId(subZoneId);
		 int centerId=scopeDao.getScopeParentId(zoneId);
		 map.put("active",getSubZoneRecipients(subZoneId));
		 map.put("escalation",getZoneRecipients(zoneId));
		 map.put("escalation2",getCenterRecipients(centerId));
		 return map;
	}
}
