package com.serotonin.mango.rt.exchange;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.util.regex.Pattern;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.SystemSettingsDao;
import com.serotonin.mango.web.email.MessageFormatDirective;
import com.serotonin.mango.web.email.SubjectDirective;

public class Content extends ExchangeContent {
	public static final int CONTENT_TYPE_BOTH = 0;
	public static final int CONTENT_TYPE_HTML = 1;
	public static final int CONTENT_TYPE_TEXT = 2;
	private final String defaultSubject;
	private final SubjectDirective subjectDirective;
	protected static final Map<Pattern, String> REPLACEMENT_EMPTY_TAG = new HashMap();

	

	public Content(String subject, String plainContent, String htmlContent,String defaultSubject,SubjectDirective subjectDirective) {
		super(subject, plainContent, htmlContent);
		this.defaultSubject=defaultSubject;
		this.subjectDirective=subjectDirective;
	}

	public Content(String templateName, Map<String, Object> model,
			ResourceBundle bundle, String defaultSubject, String encoding)
			throws TemplateException,IOException {

		int type = SystemSettingsDao
				.getIntValue(SystemSettingsDao.EMAIL_CONTENT_TYPE);

		this.defaultSubject = defaultSubject;
		this.subjectDirective = new SubjectDirective(bundle);

		model.put("fmt", new MessageFormatDirective(bundle));
		model.put("subject", subjectDirective);
		if (null != model.get("instanceDescription")) {
			String telInfo = model.get("instanceDescription").toString();
			if (telInfo.contains("[TEL]") && telInfo.contains("[END]")) {
				setPlainTemplate(getTemplate(templateName, false), model);
				return;
			}
		}

		if (type == CONTENT_TYPE_HTML || type == CONTENT_TYPE_BOTH)
			setHtmlTemplate(getTemplate(templateName, true), model);
		if (type == CONTENT_TYPE_TEXT || type == CONTENT_TYPE_BOTH)
			setPlainTemplate(getTemplate(templateName, false), model);
	}

	private Template getTemplate(String name, boolean html) throws IOException {
		if (html)
			name = "html/" + name + ".ftl";
		else
			name = "text/" + name + ".ftl";

		return Common.ctx.getFreemarkerConfig().getTemplate(name);
	}

	public void setHtmlTemplate(Template htmlTpl, Object model)
			throws TemplateException,IOException {
		if (htmlTpl != null) {
			StringWriter html = new StringWriter();
			htmlTpl.process(model, html);
			this.htmlContent = html.toString();
		}
	}

	public void setPlainTemplate(Template plainTpl, Object model)
			throws TemplateException,IOException {
		if (plainTpl != null) {
			StringWriter plain = new StringWriter();
			plainTpl.process(model, plain);
			this.plainContent = plain.toString();

			for (Map.Entry entry : REPLACEMENT_EMPTY_TAG.entrySet())
				this.plainContent = ((Pattern) entry.getKey()).matcher(
						this.plainContent)
						.replaceAll((String) entry.getValue());
		}
	}
	  static
	  {
	    REPLACEMENT_EMPTY_TAG.put(Pattern.compile("<br\\s*/>"), "\r\n");
	    REPLACEMENT_EMPTY_TAG.put(Pattern.compile("&nbsp;"), " ");
	  }
}
