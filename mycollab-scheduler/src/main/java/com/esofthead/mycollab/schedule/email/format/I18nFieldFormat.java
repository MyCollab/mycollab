package com.esofthead.mycollab.schedule.email.format;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.i18n.LocalizationHelper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.hp.gagawa.java.elements.Span;
import com.mchange.v2.codegen.bean.BeangenUtils;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.0
 * 
 */
public class I18nFieldFormat extends FieldFormat {
	private static Logger log = LoggerFactory.getLogger(I18nFieldFormat.class);

	private Class<? extends Enum> enumKey;

	public I18nFieldFormat(String fieldName, Enum displayName,
			Class<? extends Enum> enumKey) {
		super(fieldName, displayName);
		this.enumKey = enumKey;
	}

	@Override
	public String formatField(MailContext<?> context) {
		Object wrappedBean = context.getWrappedBean();
		Object value = null;
		try {
			value = PropertyUtils.getProperty(wrappedBean, fieldName);
			if (value == null) {
				return new Span().write();
			} else {
				Enum valueEnum = Enum.valueOf(enumKey, value.toString());
				return new Span().appendText(
						LocalizationHelper.getMessage(context.getLocale(),
								valueEnum)).write();
			}
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			log.error(
					"Can not generate email of object "
							+ BeanUtility.printBeanObj(wrappedBean)
							+ " field: " + fieldName + " and " + value, e);
			return new Span().write();
		}
	}

	@Override
	public String formatField(MailContext<?> context, String value) {
		try {
			Enum valueEnum = Enum.valueOf(enumKey, value.toString());
			return LocalizationHelper
					.getMessage(context.getLocale(), valueEnum);
		} catch (Exception e) {
			log.error("Can not generate email field: " + fieldName + " and "
					+ value, e);
			return "";
		}
	}

}
