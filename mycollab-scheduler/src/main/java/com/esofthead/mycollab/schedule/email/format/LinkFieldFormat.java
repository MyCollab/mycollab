package com.esofthead.mycollab.schedule.email.format;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.schedule.email.MailContext;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;

/**
 * 
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public abstract class LinkFieldFormat extends FieldFormat {
	private static Logger log = LoggerFactory.getLogger(LinkFieldFormat.class);

	private Span span;

	public LinkFieldFormat(String fieldName, String displayName) {
		super(fieldName, displayName);
	}

	@Override
	public String formatField(MailContext<?> context) {
		span = new Span();
		Object wrappedBean = context.getWrappedBean();
		Object value;
		try {
			value = PropertyUtils.getProperty(wrappedBean, fieldName);
			if (value != null) {

				Img linkIcon = buildImage(context);
				if (linkIcon != null) {
					linkIcon.setStyle("vertical-align: middle; margin-right: 3px;");
					span.appendChild(linkIcon);
				}

				A link = buildLink(context);
				link.setStyle("text-decoration: none; color: rgb(36, 127, 211);");
				span.appendChild(link);
			}
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			log.error("Can not generate email field: " + fieldName, e);
		}

		return span.write();
	}

	abstract protected Img buildImage(MailContext<?> context);

	abstract protected A buildLink(MailContext<?> context);

}
