/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import com.serotonin.mango.vo.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.MailingListDao;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.rt.maint.work.EmailWorkItem;
import com.serotonin.mango.vo.mailingList.EmailRecipient;
import com.serotonin.mango.vo.mailingList.MailingList;
import com.serotonin.mango.web.dwr.beans.RecipientListEntryBean;
import com.serotonin.mango.web.email.MangoEmailContent;
import com.serotonin.util.StringUtils;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.I18NUtils;
import com.serotonin.web.i18n.LocalizableMessage;

import com.serotonin.mango.db.dao.SystemSettingsDao;
import com.serotonin.mango.rt.exchange.ExchangeSend;
import com.serotonin.mango.rt.exchange.Content;
import microsoft.exchange.webservices.data.EmailAddress;
public class MailingListsDwr extends BaseDwr {
    private final Log log = LogFactory.getLog(MailingListsDwr.class);

    public DwrResponseI18n init(int scopeId) {
        DwrResponseI18n response = new DwrResponseI18n();
        response.addData("lists", new MailingListDao().getMailingLists(scopeId));
        response.addData("users", new UserDao().getUsers(scopeId));
        return response;
    }

    public MailingList getMailingList(int id) {
        if (id == Common.NEW_ID) {
            MailingList ml = new MailingList();
            ml.setId(Common.NEW_ID);
            ml.setXid(new MailingListDao().generateUniqueXid());
            ml.setEntries(new LinkedList<EmailRecipient>());
            return ml;
        }
        return new MailingListDao().getMailingList(id);
    }

    public DwrResponseI18n saveMailingList(int id, String xid, String name, List<RecipientListEntryBean> entryBeans,
            List<Integer> inactiveIntervals,int scopeId) {
        DwrResponseI18n response = new DwrResponseI18n();
        MailingListDao mailingListDao = new MailingListDao();

        // Validate the given information. If there is a problem, return an appropriate error message.
        MailingList ml = createMailingList(id, xid, name, entryBeans);
        ml.getInactiveIntervals().addAll(inactiveIntervals);
        ml.setScopeId(scopeId);
        if (StringUtils.isEmpty(xid))
            response.addContextualMessage("xid", "validate.required");
        else if (!mailingListDao.isXidUnique(xid, id))
            response.addContextualMessage("xid", "validate.xidUsed");

        ml.validate(response);

        if (!response.getHasMessages()) {
            // Save the mailing list
            mailingListDao.saveMailingList(ml);
            response.addData("mlId", ml.getId());
        }

        return response;
    }

    public void deleteMailingList(int mlId) {
        new MailingListDao().deleteMailingList(mlId);
    }

    public DwrResponseI18n sendTestEmail(int id, String name, List<RecipientListEntryBean> entryBeans) {
        DwrResponseI18n response = new DwrResponseI18n();

        MailingList ml = createMailingList(id, null, name, entryBeans);
        new MailingListDao().populateEntrySubclasses(ml.getEntries());

        Set<String> addresses = new HashSet<String>();
        ml.appendAddresses(addresses, null);
        String[] toAddrs = addresses.toArray(new String[0]);

        try {
            ResourceBundle bundle = Common.getBundle();
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("message", new LocalizableMessage("ftl.userTestEmail", ml.getName()));
            if(SystemSettingsDao.isExchange()){
            	List<EmailAddress> address=new ArrayList<EmailAddress>();
        		for (String email : toAddrs) {
        			EmailAddress add=new EmailAddress();
            		add.setAddress(email);
            		address.add(add);
        		}
        		 Content exchangeContent = new Content("testEmail", model, bundle, I18NUtils.getMessage(bundle,
	                "ftl.testEmail"), Common.UTF8);
	                ExchangeSend exchangeSend=new ExchangeSend(address,exchangeContent);
	                exchangeSend.sendMail();
            }else{
            	MangoEmailContent cnt = new MangoEmailContent("testEmail", model, bundle, I18NUtils.getMessage(bundle,
                    "ftl.testEmail"), Common.UTF8);
            	EmailWorkItem.queueEmail(toAddrs, cnt);
            	}
            }
        catch (Exception e) {
            response.addGenericMessage("mailingLists.testerror", e.getMessage());
            log.warn("", e);
        }

        return response;
    }

    //
    // /
    // / Private helper methods
    // /
    //
    private MailingList createMailingList(int id, String xid, String name, List<RecipientListEntryBean> entryBeans) {
        // Convert the incoming information into more useful types.
        MailingList ml = new MailingList();
        ml.setId(id);
        ml.setXid(xid);
        ml.setName(name);

        List<EmailRecipient> entries = new ArrayList<EmailRecipient>(entryBeans.size());
        for (RecipientListEntryBean bean : entryBeans)
            entries.add(bean.createEmailRecipient());
        ml.setEntries(entries);

        return ml;
    }
}
