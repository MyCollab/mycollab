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

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.schedule.email.MailContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 * @param <T>
 */
public abstract class FieldFormat {

	public static enum Type {
		DEFAULT, DATE, DATE_TIME, CURRENCY
	}

	protected String fieldName;
	protected String displayName;
	protected boolean isColSpan = false;

	public FieldFormat(String fieldName, String displayName) {
		this(fieldName, displayName, false);
	}

	public FieldFormat(String fieldName, String displayName, boolean isColSpan) {
		this.fieldName = fieldName;
		this.displayName = displayName;
		this.isColSpan = isColSpan;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean getIsColSpan() {
		return isColSpan;
	}

	public void setColSpan(boolean isColSpan) {
		this.isColSpan = isColSpan;
	}

	abstract public String formatField(MailContext<?> context);

	abstract public String formatField(MailContext<?> context, String value);

	public static FieldFormat createFieldFormat(Type fieldType,
			String fieldName, String displayName) {
		if (fieldType == Type.DEFAULT) {
			return new DefaultFieldFormat(fieldName, displayName);
		} else if (fieldType == Type.DATE) {
			return new DateFieldFormat(fieldName, displayName);
		} else if (fieldType == Type.DATE_TIME) {
			return new DateTimeFieldFormat(fieldName, displayName);
		} else if (fieldType == Type.CURRENCY) {
			return new CurrencyFieldFormat(fieldName, displayName);
		} else {
			throw new MyCollabException("Do not support field type "
					+ fieldType);
		}
	}
}
