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

import java.util.List;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.module.project.domain.SimpleItemTimeLogging;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserLink;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickListener;
import com.google.common.collect.Ordering;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.1
 * 
 */
public class TimeTrackingUserOrderComponent extends AbstractTimeTrackingDisplayComp {
	private static final long serialVersionUID = 1L;

	public TimeTrackingUserOrderComponent(List<TableViewField> fields,
			TableClickListener tableClickListener) {
		super(fields, tableClickListener);
		this.setWidth("100%");
	}

	@Override
	protected void addItem(SimpleItemTimeLogging itemTimeLogging,
			List<SimpleItemTimeLogging> timeLoggingEntries) {
		if (timeLoggingEntries.size() > 0
				&& !itemTimeLogging.getLoguser().equals(
						timeLoggingEntries.get(0).getLoguser())) {
			displayGroupItems(timeLoggingEntries);
			timeLoggingEntries.clear();
		}

		timeLoggingEntries.add(itemTimeLogging);
	}

	@Override
	protected void displayGroupItems(
			List<SimpleItemTimeLogging> timeLoggingEntries) {
		if (timeLoggingEntries.size() > 0) {
			SimpleItemTimeLogging firstItem = timeLoggingEntries.get(0);
			addComponent(new ProjectUserLink(firstItem.getLoguser(),
					firstItem.getLogUserAvatarId(),
					firstItem.getLogUserFullName()));

			addComponent(new TimeLoggingBockLayout(visibleFields,
					tableClickListener, timeLoggingEntries));
		}
	}

	@Override
	protected Ordering<SimpleItemTimeLogging> sortEntries() {
		return Ordering.from(new UserComparator())
				.compound(new ProjectComparator())
				.compound(new DateComparator());
	}

	@Override
	String getGroupCriteria(SimpleItemTimeLogging timeEntry) {
		return timeEntry.getLoguser();
	}
}
