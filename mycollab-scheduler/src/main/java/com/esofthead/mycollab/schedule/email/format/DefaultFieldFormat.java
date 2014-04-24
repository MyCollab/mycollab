package com.esofthead.mycollab.schedule.email.format;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.schedule.email.MailContext;
import com.hp.gagawa.java.elements.Span;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class DefaultFieldFormat extends FieldFormat {
	private static Logger log = LoggerFactory
			.getLogger(DefaultFieldFormat.class);

	public DefaultFieldFormat(String fieldname, String displayName) {
		super(fieldname, displayName);
	}

	@Override
	public String formatField(MailContext<?> context) {
		Object wrappedBean = context.getWrappedBean();
		Object value;
		try {
			value = PropertyUtils.getProperty(wrappedBean, fieldName);
			if (value == null) {
				return new Span().write();
			} else {
				return new Span().appendText(value.toString()).write();
			}
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			log.error("Can not generate email field: " + fieldName, e);
			return new Span().write();
		}

	}
}
