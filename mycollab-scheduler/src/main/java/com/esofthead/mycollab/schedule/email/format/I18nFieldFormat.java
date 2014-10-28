/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.schedule.email.format;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.i18n.LocalizationHelper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.hp.gagawa.java.elements.Span;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.0
 * 
 */
public class I18nFieldFormat extends FieldFormat {
	private static final Logger LOG = LoggerFactory.getLogger(I18nFieldFormat.class);

	@SuppressWarnings("rawtypes")
	private Class<? extends Enum> enumKey;

	@SuppressWarnings("rawtypes")
	public I18nFieldFormat(String fieldName, Enum displayName,
			Class<? extends Enum> enumKey) {
		super(fieldName, displayName);
		this.enumKey = enumKey;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
			LOG.error(
					"Can not generate of object "
							+ BeanUtility.printBeanObj(wrappedBean)
							+ " field: " + fieldName + " and " + value, e);
			return new Span().write();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String formatField(MailContext<?> context, String value) {
		try {
			Enum valueEnum = Enum.valueOf(enumKey, value.toString());
			return LocalizationHelper
					.getMessage(context.getLocale(), valueEnum);
		} catch (Exception e) {
			LOG.error("Can not generate of object field: " + fieldName + " and "
					+ value, e);
			return new Span().write();
		}
	}

}
