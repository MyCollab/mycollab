package com.esofthead.mycollab.vaadin.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.client.Util;
import com.vaadin.client.ui.VCalendar;
import com.vaadin.client.ui.calendar.schedule.WeekGrid;

public class WeekGridExt extends WeekGrid {

	Logger logger = Logger.getLogger(WeekGridExt.class.getName()); 

	public WeekGridExt(VCalendar parent, boolean format24h) {
		super(parent, format24h);
	}

	@Override
	public void setWidthPX(int width) {
		logger.log(Level.INFO, "Calendar's width: " + width);
		logger.log(Level.INFO, "Scrollbar's size: " + Util.getNativeScrollbarSize());
		logger.log(Level.INFO, "Timebar's width: " + getTimeBar().getOffsetWidth());

		width = width - (20 - Util.getNativeScrollbarSize());

		super.setWidthPX(width);


		int[] cellWidths = getDateCellWidths();
		if (cellWidths.length > 0)
		{
			int expetedWidth = (width - 20 - getTimeBar().getOffsetWidth()) / cellWidths.length;
			logger.log(Level.INFO, "Expected cell's width: " + expetedWidth);
			logger.log(Level.INFO, "Cell's width: " + cellWidths[0]);
		}
	}

}
