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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.core.arguments.Order;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.module.project.domain.SimpleItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.module.project.view.time.TimeTrackingTableDisplay;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickListener;
import com.google.common.collect.Ordering;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.1
 * 
 */
public abstract class AbstractTimeTrackingDisplayComp extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	protected List<TableViewField> visibleFields;
	protected TableClickListener tableClickListener;
	protected ItemTimeLoggingService itemTimeLoggingService;

	public AbstractTimeTrackingDisplayComp(List<TableViewField> fields,
			TableClickListener tableClickListener) {
		super();
		addStyleName(UIConstants.LAYOUT_LOG);

		this.visibleFields = fields;
		this.tableClickListener = tableClickListener;
		this.itemTimeLoggingService = ApplicationContextUtil
				.getSpringBean(ItemTimeLoggingService.class);
	}

	@SuppressWarnings({"unchecked"})
	public void queryData(ItemTimeLoggingSearchCriteria searchCriteria,
			Order orderBy) {
		this.removeAllComponents();

		List<SimpleItemTimeLogging> timeLoggingEntries = itemTimeLoggingService
				.findPagableListByCriteria(new SearchRequest<ItemTimeLoggingSearchCriteria>(
						searchCriteria));

		Ordering<SimpleItemTimeLogging> ordering = sortEntries()
				.compound(new BillableComparator())
				.compound(new ValueComparator())
				.compound(new SummaryComparator());
		if (orderBy == Order.DESCENDING) {
			Collections.sort(timeLoggingEntries, ordering.reverse());
		} else if (orderBy == Order.ASCENDING) {
			Collections.sort(timeLoggingEntries, ordering);
		}

		List<SimpleItemTimeLogging> groupLogEntries = new ArrayList<SimpleItemTimeLogging>();
		String groupCriteria = null;

		for (SimpleItemTimeLogging timeLoggingEntry : timeLoggingEntries) {
			String itemCriteria = getGroupCriteria(timeLoggingEntry);

			if (!itemCriteria.equals(groupCriteria)) {
				displayGroupItems(groupLogEntries);
				groupLogEntries.clear();
				groupCriteria = itemCriteria;
			}

			groupLogEntries.add(timeLoggingEntry);
		}

		if (groupLogEntries.size() > 0) {
			displayGroupItems(groupLogEntries);
		}
	}

	abstract protected Ordering<SimpleItemTimeLogging> sortEntries();

	abstract String getGroupCriteria(SimpleItemTimeLogging timeEntry);

	protected abstract void addItem(SimpleItemTimeLogging itemTimeLogging,
			List<SimpleItemTimeLogging> list);

	protected abstract void displayGroupItems(List<SimpleItemTimeLogging> list);

	protected static class ProjectComparator
			implements
				Comparator<SimpleItemTimeLogging> {
		@Override
		public int compare(SimpleItemTimeLogging o1, SimpleItemTimeLogging o2) {
			return o1.getProjectid().compareTo(o2.getProjectid());
		}
	}

	protected static class UserComparator
			implements
				Comparator<SimpleItemTimeLogging> {
		@Override
		public int compare(SimpleItemTimeLogging o1, SimpleItemTimeLogging o2) {
			return o1.getLoguser().compareTo(o2.getLoguser());
		}
	}

	protected static class DateComparator
			implements
				Comparator<SimpleItemTimeLogging> {
		@Override
		public int compare(SimpleItemTimeLogging o1, SimpleItemTimeLogging o2) {
			return o1.getLogforday().compareTo(o2.getLogforday());
		}
	}

	protected static class BillableComparator
			implements
				Comparator<SimpleItemTimeLogging> {
		@Override
		public int compare(SimpleItemTimeLogging o1, SimpleItemTimeLogging o2) {
			return o1.getIsbillable().compareTo(o2.getIsbillable());
		}
	}

	protected static class ValueComparator
			implements
				Comparator<SimpleItemTimeLogging> {
		@Override
		public int compare(SimpleItemTimeLogging o1, SimpleItemTimeLogging o2) {
			return o1.getLogvalue().compareTo(o2.getLogvalue());
		}
	}

	protected static class SummaryComparator
			implements
				Comparator<SimpleItemTimeLogging> {
		@Override
		public int compare(SimpleItemTimeLogging o1, SimpleItemTimeLogging o2) {
			return o1.getSummary().compareTo(o2.getSummary());
		}
	}

	protected static class TimeLoggingBockLayout extends VerticalLayout {

		private static final long serialVersionUID = 1L;

		public TimeLoggingBockLayout(List<TableViewField> visibleFields,
				TableClickListener tableClickListener,
				List<SimpleItemTimeLogging> timeLoggingEntries) {
			TimeTrackingTableDisplay table = new TimeTrackingTableDisplay(
					visibleFields);
			table.addStyleName(UIConstants.FULL_BORDER_TABLE);
			table.setMargin(new MarginInfo(true, false, false, false));
			table.addTableListener(tableClickListener);
			table.setCurrentDataList(timeLoggingEntries);
			addComponent(table);

			double billable = 0, nonbillable = 0;
			for (SimpleItemTimeLogging item : timeLoggingEntries) {
				billable += item.getIsbillable() ? item.getLogvalue() : 0;
				nonbillable += !item.getIsbillable() ? item.getLogvalue() : 0;
			}

			Label labelTotalHours = new Label(
					("Total Hours: " + (billable + nonbillable)));
			labelTotalHours.addStyleName(UIConstants.TEXT_LOG_HOURS_TOTAL);
			addComponent(labelTotalHours);

			Label labelBillableHours = new Label(
					("Billable Hours: " + billable));
			labelBillableHours.setStyleName(UIConstants.TEXT_LOG_HOURS);
			addComponent(labelBillableHours);

			Label labelNonbillableHours = new Label(
					("Non Billable Hours: " + nonbillable));
			labelNonbillableHours.setStyleName(UIConstants.TEXT_LOG_HOURS);
			addComponent(labelNonbillableHours);
		}
	}
}
