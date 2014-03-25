package com.esofthead.mycollab.vaadin.ui;

import java.util.Date;

import org.vaadin.risto.stylecalendar.StyleCalendarField;

import com.esofthead.mycollab.core.utils.DateTimeUtils;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class StyleCalendarFieldExp extends StyleCalendarField {
	private static final long serialVersionUID = 1L;

	public void setPopupClose() {
		this.setShowPopup(false);
	}

	protected String getPaintValue() {
		Object value = getValue();

		if (value == null) {
			if (getNullRepresentation() != null) {
				return getNullRepresentation();

			} else {
				return "null";
			}

		} else {
			Date selectedDate = (Date) value;
			Date[] bounceDateofWeek = DateTimeUtils
					.getBounceDateofWeek(selectedDate);
			return DateTimeUtils.formatDate(bounceDateofWeek[0]) + " - "
					+ DateTimeUtils.formatDate(bounceDateofWeek[1]);
		}
	}
}
