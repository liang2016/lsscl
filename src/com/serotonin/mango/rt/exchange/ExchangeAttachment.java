package com.serotonin.mango.rt.exchange;

public class ExchangeAttachment {
	private String contentId;
	private boolean isInline=false;
	private String file;
	private byte[] content;

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public boolean isInline() {
		return isInline;
	}

	public void setInline(boolean isInline) {
		this.isInline = isInline;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public ExchangeAttachment() {
		super();
	}

	public ExchangeAttachment(String contentId, boolean isInline, String file) {
		super();
		this.contentId = contentId;
		this.isInline = isInline;
		this.file = file;
	}

	public ExchangeAttachment(String contentId, boolean isInline, byte[] content) {
		super();
		this.contentId = contentId;
		this.isInline = isInline;
		this.content = content;
	}

	public ExchangeAttachment(String contentId, String file) {
		super();
		this.contentId = contentId;
		this.file = file;
	}
}
