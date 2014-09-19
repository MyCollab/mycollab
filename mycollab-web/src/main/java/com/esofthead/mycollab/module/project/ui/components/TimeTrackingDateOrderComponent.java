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
package com.esofthead.mycollab.module.project.ui.components;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.project.domain.SimpleItemTimeLogging;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickListener;
import com.google.common.collect.Ordering;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.1
 * 
 */
public class TimeTrackingDateOrderComponent
		extends
			AbstractTimeTrackingDisplayComp {

	private static final long serialVersionUID = 1L;

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"EEEE, dd MMMM yyyy");

	public TimeTrackingDateOrderComponent(List<TableViewField> fields,
			TableClickListener tableClickListener) {
		super(fields, tableClickListener);
		this.setWidth("100%");
	}

	@Override
	protected void addItem(SimpleItemTimeLogging itemTimeLogging,
			List<SimpleItemTimeLogging> timeLoggingEntries) {
		if (timeLoggingEntries.size() > 0
				&& DateTimeUtils.compareByDate(itemTimeLogging.getLogforday(),
						timeLoggingEntries.get(0).getLogforday()) != 0) {
			displayGroupItems(timeLoggingEntries);
			timeLoggingEntries.clear();
		}

		timeLoggingEntries.add(itemTimeLogging);
	}

	@Override
	protected void displayGroupItems(
			List<SimpleItemTimeLogging> timeLoggingEntries) {
		if (timeLoggingEntries.size() > 0) {
			Label label = new Label(DATE_FORMAT.format(timeLoggingEntries
					.get(0).getLogforday()));
			label.addStyleName(UIConstants.TEXT_LOG_DATE);
			addComponent(label);

			addComponent(new TimeLoggingBockLayout(visibleFields,
					tableClickListener, timeLoggingEntries));
		}
	}

	@Override
	protected Ordering<SimpleItemTimeLogging> sortEntries() {
		return Ordering.from(new DateComparator())
				.compound(new ProjectComparator())
				.compound(new UserComparator());
	}

	@Override
	String getGroupCriteria(SimpleItemTimeLogging timeEntry) {
		return DateTimeUtils.formatDate(timeEntry.getLogforday(), "yyyy/MM/dd");
	}
}
