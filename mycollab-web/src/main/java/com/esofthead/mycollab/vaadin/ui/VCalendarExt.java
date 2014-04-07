package com.esofthead.mycollab.vaadin.ui;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.DockPanel;
import com.vaadin.client.Util;
import com.vaadin.client.ui.VCalendar;
import com.vaadin.client.ui.calendar.schedule.CalendarDay;
import com.vaadin.client.ui.calendar.schedule.CalendarEvent;
import com.vaadin.client.ui.calendar.schedule.WeeklyLongEvents;

public class VCalendarExt extends VCalendar {
	private int adjustedWidth;
	private WeeklyLongEvents weeklyLongEvents;

	@Override
	public void setSizeForChildren(int newWidth, int newHeight) {

		if (newHeight == -1) {
			adjustedWidth = newWidth - 20;
		} else {
			adjustedWidth = newWidth - (20 - Util.getNativeScrollbarSize());
		}
		super.setSizeForChildren(newWidth, newHeight);
	}

	@Override
	protected void recalculateWidths() {
		super.recalculateWidths();

		if (getWeekGrid() != null) {
			getWeekGrid().setWidthPX(adjustedWidth);
			weeklyLongEvents.setWidthPX(getWeekGrid().getInternalWidth());
		}
	}

	@Override
	public void updateWeekView(int scroll, Date today, int daysInMonth,
			int firstDayOfWeek, Collection<CalendarEvent> events,
			List<CalendarDay> days) {
		super.updateWeekView(scroll, today, daysInMonth, firstDayOfWeek,
				events, days);
		weeklyLongEvents = (WeeklyLongEvents) ((DockPanel) getWidget())
				.getWidget(1);
	}

}
