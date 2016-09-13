package com.serotonin.mango.rt.exchange;

import java.util.ArrayList;
import java.util.List;

import microsoft.exchange.webservices.data.Attachment;

//exchange content
public class ExchangeContent {
	// attachment
	private List<ExchangeAttachment> attachments = new ArrayList<ExchangeAttachment>();
	protected String subject;
	protected String plainContent;
	protected String htmlContent;


	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPlainContent() {
		return plainContent;
	}

	public void setPlainContent(String plainContent) {
		this.plainContent = plainContent;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public void addAttachment(ExchangeAttachment attachment) {
		this.attachments.add(attachment);
	}

	public List<ExchangeAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<ExchangeAttachment> attachments) {
		this.attachments = attachments;
	}

	public ExchangeContent(String subject, String plainContent,
			String htmlContent) {
		super();
		this.subject = subject;
		this.plainContent = plainContent;
		this.htmlContent = htmlContent;
	}

	public ExchangeContent(List<ExchangeAttachment> attachments, String subject,
			String plainContent, String htmlContent) {
		super();
		this.attachments = attachments;
		this.subject = subject;
		this.plainContent = plainContent;
		this.htmlContent = htmlContent;
	}

	public ExchangeContent() {
		super();
	}

	public boolean isMultipart() {
		// TODO Auto-generated catch block
		return false;
	}
}
