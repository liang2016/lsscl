package com.serotonin.mango.rt.exchange;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.serotonin.mango.db.dao.SystemSettingsDao;

import microsoft.exchange.webservices.data.Attachment;
import microsoft.exchange.webservices.data.EmailAddress;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.ExchangeCredentials;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.ExchangeVersion;
import microsoft.exchange.webservices.data.MessageBody;
import microsoft.exchange.webservices.data.ServiceLocalException;
import microsoft.exchange.webservices.data.WebCredentials;

public class ExchangeSend {
	private List<EmailAddress> address;
	private Content exchangeContent;

	public ExchangeSend(List<EmailAddress> address, Content exchangeContent) {
		super();
		this.address = address;
		this.exchangeContent = exchangeContent;
	}

	private ExchangeService createService(String username, String password,
			String url, String domain) {
		ExchangeService service = new ExchangeService(
				ExchangeVersion.Exchange2010_SP1);
		ExchangeCredentials credentials = new WebCredentials(username,
				password, domain);
		try {
			service.setUrl(new URI(url));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		service.setCredentials(credentials);
		return service;
	}

	public void sendMail() {
		String url = SystemSettingsDao
			.getValue(SystemSettingsDao.EXCHANGE_URL);
		String username = SystemSettingsDao
				.getValue(SystemSettingsDao.EXCHANGE_USERNAME);
		String password = SystemSettingsDao
				.getValue(SystemSettingsDao.EXCHANGE_PASSWORD);
		String domain = SystemSettingsDao
				.getValue(SystemSettingsDao.EXCHANGE_DOMAIN);
		try {
			EmailMessage msg = new EmailMessage(createService(username,
					password, url, domain));
			setBody(msg, exchangeContent);
			msg.send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private EmailMessage setBody(EmailMessage msg, Content exchangeContent) {
		try {
			msg.setSubject(exchangeContent.getSubject());
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			msg.setBody(MessageBody.getMessageBodyFromText(exchangeContent
					.getHtmlContent()));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			for (EmailAddress email: address) {
				msg.getToRecipients().add(email);

			}
		} catch (ServiceLocalException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!exchangeContent.getAttachments().isEmpty()) {
			List<ExchangeAttachment> attachments = exchangeContent
					.getAttachments();
			for (int i = 0; i < attachments.size(); i++) {
				ExchangeAttachment attachment = attachments.get(i);
				try {
					if (attachment.isInline()
							&& attachment.getContent() == null) {
						Attachment att = msg.getAttachments()
								.addFileAttachment(attachment.getFile());
						att.setContentLocation(attachment.getContentId());
						att.setIsInline(attachment.isInline());
					} else if (attachment.getContent() != null
							&& attachment.isInline()) {
						msg.getAttachments().addFileAttachment(
								attachment.getContentId(),
								attachment.getContent());
					} else {
						msg.getAttachments()
								.addFileAttachment(attachment.getContentId(),
										attachment.getFile());
					}
				} catch (ServiceLocalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return msg;
	}
}
