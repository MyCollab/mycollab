/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.esofthead.mycollab.vaadin.ui;

import java.util.Calendar;
import java.util.Date;

import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.RangeDateSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public class DateSelectionField extends GridLayout {

	private DateFieldExt dateStart = new DateFieldExt();
	private DateFieldExt dateEnd = new DateFieldExt();

	private DateSelectionComboBox dateSelectionBox;

	public DateFieldExt getDateStart() {
		return dateStart;
	}

	public void setDateStart(DateFieldExt dateStart) {
		this.dateStart = dateStart;
	}

	public DateFieldExt getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(DateFieldExt dateEnd) {
		this.dateEnd = dateEnd;
	}

	public DateSelectionComboBox getDateSelectionBox() {
		return dateSelectionBox;
	}

	public void setDateSelectionBox(DateSelectionComboBox dateSelectionBox) {
		this.dateSelectionBox = dateSelectionBox;
	}

	public DateSelectionField(String width) {
		this();
		dateSelectionBox.setWidth(width);
	}

	public DateSelectionField() {
		setDateWidth(120);
		this.setSpacing(true);
		dateSelectionBox = new DateSelectionComboBox();
		dateSelectionBox.setFilteringMode(FilteringMode.OFF);
		dateSelectionBox.setImmediate(true);

		dateStart.setResolution(Resolution.DAY);
		dateEnd.setResolution(Resolution.DAY);

		dateSelectionBox
				.addValueChangeListener(new Property.ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						String filterStr = (String) event.getProperty()
								.getValue();
						filterStr = (filterStr != null) ? filterStr : "";

						setComponentByValue(filterStr);
					}
				});
		this.addComponent(dateSelectionBox, 0, 0);
	}

	public void setComponentByValue(String filterStr) {

		removeAllDatefield();

		if (filterStr.equals(DateSelectionComboBox.EQUAL)) {
			addOneDate();
		} else if (filterStr.equals(DateSelectionComboBox.NOTON)) {
			addOneDate();
		} else if (filterStr.equals(DateSelectionComboBox.AFTER)) {
			addOneDate();
		} else if (filterStr.equals(DateSelectionComboBox.BEFORE)) {
			addOneDate();
		} else if (filterStr.equals(DateSelectionComboBox.ISBETWEEN)) {
			addRangeDate();
		}
	}

	public SearchField getValue() {
		String filterStr = (String) dateSelectionBox.getValue();
		filterStr = (filterStr != null) ? filterStr : "";
		Date fDate = dateStart.getValue();
		Date tDate = dateEnd.getValue();

		if (filterStr.equals(DateSelectionComboBox.ISBETWEEN)) {
			if (fDate == null || tDate == null)
				return null;
			return new RangeDateSearchField(fDate, tDate);
		}

		if (filterStr.equals(DateSelectionComboBox.EQUAL)) {
			if (fDate == null)
				return null;
			return new DateSearchField(SearchField.AND, DateSearchField.EQUAL,
					fDate);
		}

		if (filterStr.equals(DateSelectionComboBox.NOTON)) {
			if (fDate == null)
				return null;
			return new DateSearchField(SearchField.AND,
					DateSearchField.NOTEQUAL, fDate);
		}

		if (filterStr.equals(DateSelectionComboBox.AFTER)) {
			if (fDate == null)
				return null;
			return new DateSearchField(SearchField.AND,
					DateSearchField.GREATERTHAN, fDate);
		}

		if (filterStr.equals(DateSelectionComboBox.BEFORE)) {
			if (fDate == null)
				return null;
			return new DateSearchField(SearchField.AND,
					DateSearchField.LESSTHAN, fDate);
		}

		if (filterStr.equals(DateSelectionComboBox.LAST7DAYS)) {
			return getLastNumberDays(7);
		}

		if (filterStr.equals(DateSelectionComboBox.NEXT7DAYS)) {
			return getNextNumberDays(7);
		}

		if (filterStr.equals(DateSelectionComboBox.LAST30DAYS)) {
			return getLastNumberDays(30);
		}

		if (filterStr.equals(DateSelectionComboBox.NEXT30DAYS)) {
			return getNextNumberDays(30);
		}

		if (filterStr.equals(DateSelectionComboBox.LASTMONTH)) {
			return getMonthFilterByDuration(Calendar.getInstance().get(
					Calendar.MONTH) - 1);
		}

		if (filterStr.equals(DateSelectionComboBox.THISMONTH)) {
			return getMonthFilterByDuration(Calendar.getInstance().get(
					Calendar.MONTH));
		}

		if (filterStr.equals(DateSelectionComboBox.NEXTMONTH)) {
			return getMonthFilterByDuration(Calendar.getInstance().get(
					Calendar.MONTH) + 1);
		}

		if (filterStr.equals(DateSelectionComboBox.LASTYEAR)) {
			return getYearFilterByDuration(Calendar.getInstance().get(
					Calendar.YEAR) - 1);
		}

		if (filterStr.equals(DateSelectionComboBox.THISYEAR)) {
			return getYearFilterByDuration(Calendar.getInstance().get(
					Calendar.YEAR));
		}

		if (filterStr.equals(DateSelectionComboBox.NEXTYEAR)) {
			return getYearFilterByDuration(Calendar.getInstance().get(
					Calendar.YEAR) + 1);
		}

		return null;
	}

	public void setDefaultSelection() {
		dateSelectionBox.setValue(null);
	}

	private RangeDateSearchField getLastNumberDays(int duration) {
		Date fDate = DateTimeUtils.subtractOrAddDayDuration(new Date(),
				-duration);
		Date tDate = new Date();
		return new RangeDateSearchField(fDate, tDate);
	}

	private RangeDateSearchField getNextNumberDays(int duration) {
		Date fDate = new Date();
		Date tDate = DateTimeUtils.subtractOrAddDayDuration(new Date(),
				duration);
		return new RangeDateSearchField(fDate, tDate);
	}

	private RangeDateSearchField getYearFilterByDuration(int yearDuration) {

		Calendar c = Calendar.getInstance();
		c.set(yearDuration, 0, 1);
		int yearMaxDays = c.getActualMaximum(Calendar.DAY_OF_YEAR);
		Date fDate = c.getTime();
		Date tDate = DateTimeUtils.subtractOrAddDayDuration(
				(Date) fDate.clone(), yearMaxDays - 1);
		return new RangeDateSearchField(fDate, tDate);
	}

	private RangeDateSearchField getMonthFilterByDuration(int monthDuration) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.getInstance().get(Calendar.YEAR), monthDuration, 1);
		int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		Date fDate = c.getTime();
		Date tDate = DateTimeUtils.subtractOrAddDayDuration(fDate,
				monthMaxDays - 1);
		return new RangeDateSearchField(fDate, tDate);
	}

	private void addOneDate() {
		this.setRows(2);
		this.addComponent(dateStart, 0, 1);
	}

	private void setDateWidth(float width) {
		dateStart.setWidth(width, Unit.PIXELS);
		dateEnd.setWidth(width, Unit.PIXELS);
	}

	public void addRangeDate() {
		this.setRows(2);
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setSpacing(true);
		hLayout.addComponent(dateStart);
		hLayout.addComponent(dateEnd);
		this.addComponent(hLayout, 0, 1);
	}

	public void removeAllDatefield() {
		for (int i = 0; i < this.getColumns(); i++) {
			removeComponent(i, 1);
		}

		dateStart.setValue(new Date());
		dateStart.setValue(new Date());
	}

	public void setDateFormat(String dateFormat) {
		dateStart.setDateFormat(dateFormat);
		dateEnd.setDateFormat(dateFormat);
	}
}
