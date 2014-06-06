/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class DateComboboxSelectionField extends CustomField<Date> {
	private static final long serialVersionUID = 1L;

	protected ComboBox cboYear;
	protected ComboBox cboMonth;
	protected ComboBox cboDate;

	private Map<String, Integer> mapNumberMonth = new HashMap<String, Integer>();

	public DateComboboxSelectionField() {
		cboMonth = new ComboBox();
		cboMonth.setNullSelectionAllowed(true);
		cboMonth.setImmediate(true);

		addMonthItems();
		cboMonth.setWidth("117px");

		cboDate = new ComboBox();
		cboDate.setNullSelectionAllowed(true);
		cboDate.setImmediate(true);

		addDayItems();
		cboDate.setWidth("50px");

		cboYear = new ComboBox();
		cboYear.setNullSelectionAllowed(true);
		cboYear.setImmediate(true);

		addYearItems();
		cboYear.setWidth("70px");
	}

	@Override
	protected Component initContent() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);

		layout.addComponent(cboMonth);
		layout.setComponentAlignment(cboMonth, Alignment.TOP_CENTER);

		layout.addComponent(cboDate);
		layout.setComponentAlignment(cboDate, Alignment.TOP_CENTER);

		layout.addComponent(cboYear);
		layout.setComponentAlignment(cboYear, Alignment.TOP_CENTER);
		return layout;
	}

	private String formatMonth(String month) {
		SimpleDateFormat monthParse = new SimpleDateFormat("MM");
		SimpleDateFormat monthDisplay = new SimpleDateFormat("MMMM");
		try {
			return monthDisplay.format(monthParse.parse(month));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	private void addMonthItems() {
		for (int i = 1; i <= 12; i++) {
			cboMonth.addItem(formatMonth(i + ""));
			mapNumberMonth.put(formatMonth(i + ""), i);
		}
	}

	private void addDayItems() {
		for (int i = 1; i <= 31; i++) {
			if (i < 10) {
				cboDate.addItem("0" + i);
			} else {
				cboDate.addItem(i + "");
			}
		}
	}

	private void addYearItems() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int currentYear = calendar.get(Calendar.YEAR);
		for (int i = (currentYear - 10); i >= 1900; i--) {
			cboYear.addItem(i);
		}
	}

	public void setDate(Date date) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			int dateVal = calendar.get(Calendar.DATE);
			if (dateVal < 10) {
				cboDate.select("0" + dateVal);
			} else {
				cboDate.select(dateVal + "");
			}

			cboMonth.select(formatMonth(calendar.get(Calendar.MONTH) + 1 + ""));
			cboYear.select(calendar.get(Calendar.YEAR));
		} else {
			cboDate.select("");
			cboMonth.select("");
			cboYear.select("");
		}
	}

	public Date getDate() {
		if ((cboDate.getValue() != null) && (cboMonth.getValue() != null)
				&& (cboYear.getValue() != null)) {
			Calendar calendar = Calendar.getInstance();

			calendar.set(Integer.parseInt(cboYear.getValue().toString()),
					mapNumberMonth.get(cboMonth.getValue().toString()) - 1,
					Integer.parseInt(cboDate.getValue().toString()));
			return calendar.getTime();
		} else {
			return null;
		}

	}

	@Override
	public void setPropertyDataSource(Property newDataSource) {
		Date value = (Date) newDataSource.getValue();
		if (value != null) {
			this.setDate(value);
		}
		super.setPropertyDataSource(newDataSource);
	}

	@Override
	public void commit() throws SourceException, InvalidValueException {
		Date value = getDate();
		setInternalValue(value);
		super.commit();
	}

	@Override
	public Class<Date> getType() {
		return Date.class;
	}
}
