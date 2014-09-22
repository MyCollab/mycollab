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

import com.esofthead.mycollab.core.arguments.RangeDateSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class DateRangeField extends CustomField {

	private static final long serialVersionUID = 1L;

	private DateField dateStart = new DateField();
	private DateField dateEnd = new DateField();

	@Override
	protected Component initContent() {
		HorizontalLayout container = new HorizontalLayout();
		container.setSpacing(true);
		Label dateStartLb = new Label("From:");
		Label dateEndLb = new Label("To:");

		UiUtils.addComponent(container, dateStartLb, Alignment.MIDDLE_CENTER);
		container.addComponent(dateStart);
		UiUtils.addComponent(container, dateEndLb, Alignment.MIDDLE_CENTER);
		container.addComponent(dateEnd);

		setDateWidth(120);
		setDefaultValue();

		return container;
	}

	public RangeDateSearchField getRangeSearchValue() {
		Date fDate = (Date) dateStart.getValue();
		Date tDate = (Date) dateEnd.getValue();

		if (fDate == null || tDate == null)
			return null;

		return new RangeDateSearchField(fDate, tDate);
	}

	public void setDateFormat(String dateFormat) {
		dateStart.setDateFormat(dateFormat);
		dateEnd.setDateFormat(dateFormat);
	}

	public void setDefaultValue() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		Date fDate = c.getTime();
		Date tDate = DateTimeUtils.subtractOrAddDayDuration(fDate, 7);

		dateStart.setValue(fDate);
		dateEnd.setValue(tDate);
	}

	private void setDateWidth(float width) {
		dateStart.setWidth(width, Unit.PIXELS);
		dateEnd.setWidth(width, Unit.PIXELS);
		dateStart.setResolution(Resolution.DAY);
		dateEnd.setResolution(Resolution.DAY);
	}

	@Override
	public Class<?> getType() {
		return null;
	}
}
