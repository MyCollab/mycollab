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

import com.esofthead.mycollab.schedule.email.MailContext;
import com.hp.gagawa.java.elements.Span;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class DefaultFieldFormat extends FieldFormat {
	private static final Logger LOG = LoggerFactory
			.getLogger(DefaultFieldFormat.class);

	public DefaultFieldFormat(String fieldname, Enum<?> displayName) {
		super(fieldname, displayName);
	}

	public DefaultFieldFormat(String fieldname, Enum<?> displayName,
			boolean isColspan) {
		super(fieldname, displayName, isColspan);
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
			LOG.error("Can not generate email field: " + fieldName, e);
			return new Span().write();
		}

	}

	@Override
	public String formatField(MailContext<?> context, String value) {
		if (value == null || "".equals(value)) {
			return new Span().write();
		}

		return value;
	}
}
