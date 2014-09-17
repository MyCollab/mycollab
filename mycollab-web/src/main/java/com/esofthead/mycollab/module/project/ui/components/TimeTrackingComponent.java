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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.project.domain.SimpleItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserLink;
import com.esofthead.mycollab.module.project.view.time.TimeTableFieldDef;
import com.esofthead.mycollab.module.project.view.time.TimeTrackingTableDisplay;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.1
 * 
 */
public class TimeTrackingComponent extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"EEEE, dd MMMM yyyy");
	private static final String ORDERBY_ASCENDING = "Ascending";
	private static final String ORDERBY_DESCENDING = "Descending";
	private static final String GROUPBY_USER = "User";
	private static final String GROUPBY_DATE = "Date";

	private List<TableViewField> visibleFields;
	private TableClickListener tableClickListener;
	private ItemTimeLoggingService itemTimeLoggingService;

	private String groupBy;
	private double billable = 0, nonbillable = 0;

	private Date current = new Date(0);
	private String username = "", avatar = "", fullname = "";

	private List<SimpleItemTimeLogging> list = new ArrayList<SimpleItemTimeLogging>();

	public TimeTrackingComponent(List<TableViewField> fields,
			TableClickListener tableClickListener) {
		super();
		addStyleName(UIConstants.LAYOUT_LOG);

		this.visibleFields = fields;
		this.tableClickListener = tableClickListener;
		this.itemTimeLoggingService = ApplicationContextUtil
				.getSpringBean(ItemTimeLoggingService.class);
	}

	public void show(ItemTimeLoggingSearchCriteria searchCriteria,
			final String groupBy, final String orderBy) {
		this.removeAllComponents();
		this.list.clear();
		this.billable = this.nonbillable = 0;
		this.groupBy = groupBy;

		@SuppressWarnings("unchecked")
		List<SimpleItemTimeLogging> itemTimeLoggingList = itemTimeLoggingService
				.findPagableListByCriteria(new SearchRequest<ItemTimeLoggingSearchCriteria>(
						searchCriteria));

		Collections.sort(itemTimeLoggingList,
				new Comparator<SimpleItemTimeLogging>() {
					@Override
					public int compare(SimpleItemTimeLogging item1,
							SimpleItemTimeLogging item2) {
						if (groupBy.equals(GROUPBY_USER)) {
							return item1.getCreateduser().compareTo(
									item2.getCreateduser());
						} else {
							return item1.getLogforday().compareTo(
									item2.getLogforday());
						}
					}
				});

		if (orderBy.equals(ORDERBY_ASCENDING)) {
			for (int i = 0; i < itemTimeLoggingList.size(); i++) {
				addItem(itemTimeLoggingList.get(i));
			}
		} else if (orderBy.equals(ORDERBY_DESCENDING)) {
			for (int i = itemTimeLoggingList.size() - 1; i >= 0; i--) {
				addItem(itemTimeLoggingList.get(i));
			}
		}
		showRecord();
	}

	private void addItem(SimpleItemTimeLogging itemTimeLogging) {
		if (groupBy.equals(GROUPBY_DATE)
				&& (DateTimeUtils.compareByDate(itemTimeLogging.getLogforday(),
						current) != 0)) {
			showRecord();
			refreshData(itemTimeLogging);
		} else if (groupBy.equals(GROUPBY_USER)
				&& !itemTimeLogging.getLoguser().equals(username)) {
			showRecord();
			refreshData(itemTimeLogging);
		}

		list.add(itemTimeLogging);
		billable += itemTimeLogging.getIsbillable() ? itemTimeLogging
				.getLogvalue() : 0;
		nonbillable += !itemTimeLogging.getIsbillable() ? itemTimeLogging
				.getLogvalue() : 0;
	}

	private void refreshData(SimpleItemTimeLogging itemTimeLogging) {
		current = itemTimeLogging.getLogforday();
		username = itemTimeLogging.getLoguser();
		avatar = itemTimeLogging.getLogUserAvatarId();
		fullname = itemTimeLogging.getLogUserFullName();
		list.clear();
		billable = nonbillable = 0;
	}

	private void showRecord() {
		if (list.size() > 0) {
			if (groupBy.equals(GROUPBY_DATE)) {
				Label label = new Label(DATE_FORMAT.format(current));
				label.addStyleName(UIConstants.TEXT_LOG_DATE);
				addComponent(label);
			} else {
				addComponent(new ProjectUserLink(username, avatar, fullname));
			}
			List<TableViewField> fields = new ArrayList<TableViewField>();
			for (TableViewField field : visibleFields) {
				if ((groupBy.equals(GROUPBY_DATE) && !field
						.equals(TimeTableFieldDef.logForDate))
						|| (groupBy.equals(GROUPBY_USER) && !field
								.equals(TimeTableFieldDef.logUser))) {
					fields.add(field);
				}
			}

			TimeTrackingTableDisplay table = new TimeTrackingTableDisplay(
					fields);
			table.addStyleName(UIConstants.FULL_BORDER_TABLE);
			table.setMargin(new MarginInfo(true, false, false, false));
			table.addTableListener(this.tableClickListener);
			table.setCurrentDataList(list);
			addComponent(table);

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
