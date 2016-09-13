/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event.handlers;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import com.serotonin.mango.db.dao.UserDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.serotonin.mango.vo.User;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.MailingListDao;
import com.serotonin.mango.db.dao.SystemSettingsDao;
import com.serotonin.mango.rt.event.EventInstance;
import com.serotonin.mango.rt.event.type.SystemEventType;
import com.serotonin.mango.rt.maint.work.EmailWorkItem;
import com.serotonin.mango.util.timeout.ModelTimeoutClient;
import com.serotonin.mango.util.timeout.ModelTimeoutTask;
import com.serotonin.mango.vo.event.EventHandlerVO;
import com.serotonin.mango.web.email.MangoEmailContent;
import com.serotonin.mango.web.email.UsedImagesDirective;
import com.serotonin.timer.TimerTask;
import com.serotonin.util.StringUtils;
import com.serotonin.web.email.EmailInline;
import com.serotonin.web.i18n.LocalizableMessage;
import com.serotonin.mango.db.dao.EventDao;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.PointValueDao;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.web.dwr.beans.RecipientListEntryBean;
import com.serotonin.mango.web.taglib.Functions;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.db.dao.SendDao;

import microsoft.exchange.webservices.data.EmailAddress;
import com.serotonin.mango.rt.exchange.Content;
import com.serotonin.web.i18n.I18NUtils;
import com.serotonin.mango.rt.exchange.ExchangeSend;
import com.serotonin.mango.rt.exchange.ExchangeAttachment;
/**
 * @author Administrator
 *
 */
public class EmailHandlerRT extends EventHandlerRT implements
		ModelTimeoutClient<EventInstance> {
	private static final Log LOG = LogFactory.getLog(EmailHandlerRT.class);
    private static final ResourceBundle SMS = ResourceBundle.getBundle("SMS");
	private TimerTask escalationTask;
	private TimerTask escalationTask2;
	private static int scopeId;
	private Set<String> activeRecipients;
    private boolean sendSMS=false;
    

	public  int getScopeId() {
		return scopeId;
	}

	public  void setScopeId(int scopeId) {
		this.scopeId = scopeId;
	}

	public boolean isSendSMS() {
		return sendSMS;
	}

	public void setSendSMS(boolean sendSMS) {
		this.sendSMS = sendSMS;
	}

	private enum NotificationType {
		ACTIVE("active", "ftl.subject.active"), //
		ESCALATION("escalation", "ftl.subject.escalation"), //
		ESCALATION2("escalation2", "ftl.subject.escalation2"),
		INACTIVE("inactive", "ftl.subject.inactive");

		String file;
		String key;

		private NotificationType(String file, String key) {
			this.file = file;
			this.key = key;
		}

		public String getFile() {
			return file;
		}

		public String getKey() {
			return key;
		}
	}

	/**
	 * The list of all of the recipients - active and escalation - for sending
	 * upon inactive if configured to do so.
	 */
	private Set<String> inactiveRecipients;

	public EmailHandlerRT(EventHandlerVO vo) {
		this.vo = vo;
	}

	public Set<String> getActiveRecipients() {
		return activeRecipients;
	}

	@Override
	public void eventRaised(EventInstance evt) {
		EmailAddressHelper helper=new EmailAddressHelper();
		EventDao eventDao=new EventDao();
		Map<String,Object> map=helper.getFactoryParentSeeting(vo.getScopeId());
		List<RecipientListEntryBean> active=vo.getActiveRecipients();
		scopeId=vo.getScopeId();
		evt.setSMS(vo.isUseSMS());
	
		if(map.get("active")!=null)
 			active.addAll((List<RecipientListEntryBean>)map.get("active"));
		// Get the email addresses to send to
		activeRecipients = new MailingListDao().getRecipientAddresses(active, new DateTime(evt.getActiveTimestamp()));
		
		// Send an email to the active recipients.
		 sendEmail(evt, NotificationType.ACTIVE, activeRecipients);
		 if(evt.isSMS()){
				eventDao.updateEventEmail(1,evt.getId());//2表示事件处理器警报等级为escalation状态
				evt.setWarin(1);
		 }
		// If an inactive notification is to be sent, save the active
		// recipients.
		if (vo.isSendInactive()) {
			if (vo.isInactiveOverride())
				inactiveRecipients = new MailingListDao()
						.getRecipientAddresses(vo.getInactiveRecipients(),
								new DateTime(evt.getActiveTimestamp()));
			else
				inactiveRecipients = activeRecipients;
		}

		// If an escalation is to be sent, set up timeout to trigger it.
		if (vo.isSendEscalation()) {
			long delayMS = Common.getMillis(vo.getEscalationDelayType(), vo
					.getEscalationDelay());
			escalationTask = new ModelTimeoutTask<EventInstance>(delayMS, this,
					evt);
		}
		if(vo.isSendEscalation2()){
			long delayMS2 = Common.getMillis(vo.getEscalationDelayType2(), vo
					.getEscalationDelay2());
			escalationTask2 = new ModelTimeoutTask<EventInstance>(delayMS2, this,
					evt);
		}
		System.out.println("sendEmail1");	
	}

	//
	// TimeoutClient
	//
	synchronized public void scheduleTimeout(EventInstance evt, long fireTime) {
		Set<String> addresses=null;
        EventDao eventDao=new EventDao();
		EmailAddressHelper helper=new EmailAddressHelper();
		// Send the escalation.
		if(escalationTask!=null){
			Map<String,Object> map=helper.getFactoryParentSeeting(vo.getScopeId());
			List<RecipientListEntryBean> escalation=vo.getEscalationRecipients();
			if(map.get("escalation")!=null)
				escalation.addAll((List<RecipientListEntryBean>)map.get("escalation"));
			// Get the email addresses to send to
			addresses = new MailingListDao().getRecipientAddresses(escalation, new DateTime(fireTime));
			sendEmail(evt, NotificationType.ESCALATION, addresses);
			if(evt.getWarin()!=null&&evt.getWarin()==1){
				eventDao.updateEventEmail(2,evt.getId());//2表示事件处理器警报等级为escalation状态
				evt.setWarin(2);
			}
			escalationTask=null;
		}
		else if(escalationTask2!=null){
			Map<String,Object> map=helper.getFactoryParentSeeting(vo.getScopeId());
			List<RecipientListEntryBean> escalation2=vo.getEscalationRecipients2();
			if(map.get("escalation2")!=null)
				escalation2.addAll((List<RecipientListEntryBean>)map.get("escalation2"));
			// Get the email addresses to send to
        	addresses = new MailingListDao().getRecipientAddresses(escalation2, new DateTime(fireTime));
        	sendEmail(evt, NotificationType.ESCALATION2, addresses);
        	if(evt.getWarin()!=null&&evt.getWarin()==2)
        		eventDao.updateEventEmail(3,evt.getId());//3表示事件处理器警报等级为escalation2状态
        	escalationTask2=null;
         }
		// If an inactive notification is to be sent, save the escalation
		// recipients, but only if inactive recipients
		// have not been overridden.
		if (vo.isSendInactive() && !vo.isInactiveOverride())
			inactiveRecipients.addAll(addresses);
	}

	@Override
	synchronized public void eventInactive(EventInstance evt) {
		// Cancel the escalation job in case it's there
		if (escalationTask != null)
			escalationTask.cancel();
		   // escalationTask2.cancel();
        if(escalationTask2 !=null)
        	 escalationTask2.cancel();
		if (inactiveRecipients != null && inactiveRecipients.size() > 0)
			// Send an email to the inactive recipients.
			sendEmail(evt, NotificationType.INACTIVE, inactiveRecipients);
		
		
	}
	
	public static void sendActiveEmail(EventInstance evt, Set<String> addresses) {
		sendEmail(evt, NotificationType.ACTIVE, addresses, null);
	}

	private void sendEmail(EventInstance evt,
			NotificationType notificationType, Set<String> addresses) {
		sendEmail(evt, notificationType, addresses, vo.getAlias());
	}

	private static void sendEmail(EventInstance evt,
			NotificationType notificationType, Set<String> addresses,
			String alias) {
		if (evt.getEventType().isSystemMessage()) {
			if (((SystemEventType) evt.getEventType()).getSystemEventTypeId() == SystemEventType.TYPE_EMAIL_SEND_FAILURE) {
				// Don't send email notifications about email send failures.
				LOG.info("Not sending email for event raised due to email failure");
				return;
			}
		}
		ResourceBundle bundle = Common.getBundle();
		
		// Determine the subject to use.
		LocalizableMessage subjectMsg;
		LocalizableMessage notifTypeMsg = new LocalizableMessage(
				notificationType.getKey());
		if (StringUtils.isEmpty(alias)) {
			if (evt.getId() == Common.NEW_ID)
				subjectMsg = new LocalizableMessage("ftl.subject.default",
						notifTypeMsg);
			else
				subjectMsg = new LocalizableMessage("ftl.subject.default.id",
						notifTypeMsg, evt.getId());
		} else {
			if (evt.getId() == Common.NEW_ID) 
				subjectMsg = new LocalizableMessage("ftl.subject.alias", alias,
						notifTypeMsg);
			else
				subjectMsg = new LocalizableMessage("ftl.subject.alias.id",
						alias, notifTypeMsg, evt.getId());
		}

		String subject = subjectMsg.getLocalizedMessage(bundle);
		
		//////exchange
		boolean isExchange=SystemSettingsDao.isExchange();
		List<ExchangeAttachment> attachments = new ArrayList<ExchangeAttachment>();
		
		try {
			String[] toAddrs = addresses.toArray(new String[0]);
			UsedImagesDirective inlineImages = new UsedImagesDirective();

			// Send the email.
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("evt", evt);
			if(!(null==evt.getContext()||evt.getContext().isEmpty()))
			model.putAll(evt.getContext());
			model.put("img", inlineImages);
			model.put("remind", new LocalizableMessage("evt.remind").getLocalizedMessage(bundle));
			//getWarnCode
			if(!(null==evt.getContext()||evt.getContext().isEmpty())){
				if(null!=evt.getContext().get("point")){
					DataPointVO dp=(DataPointVO)evt.getContext().get("point");
					//ScopeVO scope=new ScopeDao().findFactoryByDataSourceId(dp.getDataSourceId());
					int pointId=dp.getId();
					//if(new DataPointDao().isWarningCode(pointId)){
						PointValueTime warnCode=new PointValueDao().getPointValuesForWarnCode(pointId,evt.getActiveTimestamp()+500);//是否会有延迟
						 String prettyText = Functions.getHtmlTextForSMS(dp, warnCode);
						if(warnCode.getIntegerValue()==256||warnCode.getDoubleValue()==256||warnCode.getIntegerValue()==0){
							System.out.println("return");
							return;
						}
						//warnCodeVo=new WarningCodeDetailDao().getWarningCodeDetailByDpid(pointId,String.valueOf(warnCodeId));
						//if(null!=warnCodeVo)
						 model.put("prettyText",prettyText);
					//}
				}
			}
			
			ScopeVO scope=new ScopeDao().findFactoryById(scopeId);
			model.put("factory",scope.getScopename());
			model.put("instanceDescription", SystemSettingsDao
					.getValue(SystemSettingsDao.INSTANCE_DESCRIPTION));
			MangoEmailContent content = new MangoEmailContent(notificationType
					.getFile(), model, bundle, subject, Common.UTF8);

			for (String s : inlineImages.getImageList()){
				if(isExchange){
					ExchangeAttachment ea=new ExchangeAttachment(s,true,Common.ctx
							.getServletContext().getRealPath(s)); 
					attachments.add(ea);
				}
				else{
					content.addInline(new EmailInline.FileInline(s, Common.ctx
							.getServletContext().getRealPath(s)));
	              //System.out.println(content.getPlainContent());
	              //content.setPlainContent("");
				/*if(!sendSMS){
					return;*/
				}
			}
		   if(evt.isSMS()){
				setSMSMessage(evt,inlineImages,toAddrs,notificationType,bundle,subject,model);
			}
//			else if(evt.getEventType()!=null){
//				EventDao eventDao = new EventDao();
//				List<EventHandlerVO> vos = eventDao.getEventHandlers(evt.getEventType());
//				EventHandlerVO evo=new EventHandlerVO();
//				if(vos.size()>0){
//					evo=vos.get(0);
//				if(evo!=null&&evo.isUseSMS()!=false) {
//					setSMSMessage(evt,inlineImages,toAddrs,notificationType,bundle,subject,model);
//				}
//			}
//         }
		   //exchange
           if(isExchange){
        		List<EmailAddress> address=new ArrayList<EmailAddress>();
            	for (String email : toAddrs) {
            		EmailAddress add=new EmailAddress();
            		add.setAddress(email);
            		address.add(add);
				}
               Content exchangeContent = new Content(notificationType
    					.getFile(), model, bundle,subject, Common.UTF8);
               exchangeContent.setSubject(subject);      
               exchangeContent.setAttachments(attachments);
               ExchangeSend exchangeSend=new ExchangeSend(address,exchangeContent);
               exchangeSend.sendMail();
           }
           else{
			EmailWorkItem.queueEmail(toAddrs, content);
           }
		} catch (Exception e) {
			LOG.error("", e);
		}

	}

	// 解析电话号码
	private static List<String> getTel(String[] toAddrs) {
		List<String> telInfo=new ArrayList<String>();
		UserDao userDao = new UserDao();
		for (int i = 0; i < toAddrs.length; i++) {
			List<User> users = new ArrayList<User>();
			if(toAddrs[i]==null)
				continue;
			users = userDao.getUserByEmail(toAddrs[i].toString());
			for (int j = 0; j < users.size(); j++) {
				telInfo.add(users.get(j).getPhone());
			}
		}
		return telInfo;
	}
	private static void setSMSMessage(EventInstance evt,UsedImagesDirective inlineImages,String[] toAddrs,NotificationType notificationType,ResourceBundle bundle,String subject,Map<String, Object> model){
		String userAdd[] = {SMS.getString("SMS_userName")};
	//	Map<String, Object> model1 = new HashMap<String, Object>();
//		model1.put("evt", evt);
//		if(!(null==evt.getContext()||evt.getContext().isEmpty()))
//		model1.putAll(evt.getContext());
//		model1.put("img", inlineImages);

		// 这里添加用户电话
		//model.put("instanceDescription", getTel(toAddrs));
		String alarmType=new LocalizableMessage(notificationType.getKey()).getLocalizedMessage(bundle);
		String info=model.get("factory")+": "+alarmType;
		String message=info+"\n"+evt.getPrettyActiveTimestamp()+"-"+evt.getMessage().getLocalizedMessage(bundle);
		if(model.get("prettyText")!=null)
			message+=" :"+model.get("prettyText");
		message+="\n"+model.get("remind");
		SendDao sd=new SendDao();
		try{
			sd.insertSMS(getTel(toAddrs),evt.getActiveTimestamp(),message,0);//SMSSend.READY
		}
		catch (Exception e) {
			LOG.error("", e);
		}
	}
}
