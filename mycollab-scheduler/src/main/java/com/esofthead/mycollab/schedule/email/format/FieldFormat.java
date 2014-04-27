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

	public FieldFormat(String fieldName, String displayName) {
		this.fieldName = fieldName;
		this.displayName = displayName;
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
