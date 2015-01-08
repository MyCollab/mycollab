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

import org.vaadin.maddon.layouts.MHorizontalLayout;

import com.esofthead.mycollab.core.arguments.RangeDateSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DateRangeField extends CustomField {

	private static final long serialVersionUID = 1L;

	private DateFieldExt dateStart = new DateFieldExt();
	private DateFieldExt dateEnd = new DateFieldExt();

	@Override
	protected Component initContent() {
		MHorizontalLayout container = new MHorizontalLayout();
		container.setSpacing(true);
		Label dateStartLb = new Label("From:");
		Label dateEndLb = new Label("To:");

		container.with(dateStartLb, dateEndLb)
				.withAlign(dateStartLb, Alignment.MIDDLE_CENTER)
				.withAlign(dateEndLb, Alignment.MIDDLE_CENTER);

		setDateWidth(120);
		setDefaultValue();

		return container;
	}

	public RangeDateSearchField getRangeSearchValue() {
		Date fDate = dateStart.getValue();
		Date tDate = dateEnd.getValue();

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
