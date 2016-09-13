package com.serotonin.mango.rt;

import java.net.URI;
import java.util.List;

import com.serotonin.web.email.EmailAttachment;
import com.serotonin.web.email.EmailContent;
import com.serotonin.web.email.EmailInline;

import microsoft.exchange.webservices.data.Attachment;
import microsoft.exchange.webservices.data.AutodiscoverService;
import microsoft.exchange.webservices.data.EmailAddress;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.ExchangeCredentials;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.ExchangeVersion;
import microsoft.exchange.webservices.data.MessageBody;
import microsoft.exchange.webservices.data.WebCredentials;

public class ExchangeSender extends Thread {
	private static String text;
	private static String addres;
	private static String subString;
	private static EmailContent content;

	public ExchangeSender(EmailContent content, String addres, String subString) {
		super();
		this.content = content;
		this.addres = addres;
		this.subString = subString;
	}

	public ExchangeSender() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			sendMail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendMail() throws Exception {
		ExchangeService service = new ExchangeService(
				ExchangeVersion.Exchange2010_SP1);
		ExchangeCredentials credentials = new WebCredentials("sv-ireachreport",
				"hXqAU6WE", "corp");
		service.setUrl(new URI("https://webmail.irco.com/EWS/Exchange.asmx"));
		service.setCredentials(credentials);
		AutodiscoverService autodiscover = new AutodiscoverService("irco.com");
		autodiscover.setCredentials(new WebCredentials("sv-ireachreport",
				"hXqAU6WE", "corp"));
		EmailMessage msg = new EmailMessage(service);
		msg.setSubject(subString);
		msg.setBody(MessageBody.getMessageBodyFromText(content.getHtmlContent()));
		msg.getToRecipients().add(addres);
		if (!content.getAttachments().isEmpty()) {
			List<EmailAttachment>  atts = content.getAttachments();
			for (int i = 0; i < content.getAttachments().size(); i++) {
				EmailAttachment attachment=atts.get(i);
				//msg.getAttachments().addFileAttachment("D:\\default.css");
			}
		}
		if(!content.getInlines().isEmpty()){
			List<EmailInline> listinline=content.getInlines();
			for (int j = 0; j < listinline.size()-1; j++) {
				//取出含在页面上的文件
				EmailInline inlines=listinline.get(j);
				//Attachment attachment=msg.getAttachments().addFileAttachment(inlines.getContentId(),inlines.ByteArrayInline.content);
				//attachment.setContentId(inlines.getContentId());
				//attachment.setIsInline(true);
				//实例化一个attachment对象
			//	ItemAttachment attachment=((Object) inlines).File("D:\\default.css");
				//给他添加一个id
			//	attachment.setContentId(inlines.getContentId());
			//	attachment.setIsInline(true);
				//msg.getAttachments().addItemAttachment(attachment);
			}
			
		}
	
		Attachment attachment=msg.getAttachments().addFileAttachment("E://tomcat/webapps/ROOT/images/mangoLogoMed.jpg");
		attachment.setContentId("images/mangoLogoMed.jpg");
		attachment.setIsInline(true);
		msg.send();
	}
}