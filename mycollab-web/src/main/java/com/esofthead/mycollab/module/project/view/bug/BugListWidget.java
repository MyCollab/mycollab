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
package com.esofthead.mycollab.module.project.view.bug;

import java.util.Arrays;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickEvent;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class BugListWidget extends Depot {
	private static final long serialVersionUID = 1L;

	private BugSearchCriteria bugSearchCriteria;
	private BugTableDisplay tableItem;

	public BugListWidget(final String title, final String backBtnLabel,
			final BugSearchCriteria bugSearchCriteria,
			final IBugReportDisplayContainer bugReportDisplayContainer) {
		super(title, new VerticalLayout());

		final VerticalLayout contentLayout = (VerticalLayout) this.bodyContent;
		contentLayout.setSpacing(true);
		contentLayout.setWidth("100%");

		final Button backToBugReportsBtn = new Button(backBtnLabel,
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						bugReportDisplayContainer.displayBugReports();
					}
				});
		// contentLayout.addComponent(backToBugReportsBtn);
		final VerticalLayout backBtnWrapper = new VerticalLayout();
		backBtnWrapper.setMargin(new MarginInfo(false, false, true, false));

		backToBugReportsBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		backBtnWrapper.addComponent(backToBugReportsBtn);

		this.addComponentAsFirst(backBtnWrapper);

		this.tableItem = new BugTableDisplay(BugTableFieldDef.action,
				Arrays.asList(BugTableFieldDef.summary,
						BugTableFieldDef.assignUser, BugTableFieldDef.severity,
						BugTableFieldDef.resolution, BugTableFieldDef.duedate));

		this.tableItem.addTableListener(new TableClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void itemClick(final TableClickEvent event) {
				final SimpleBug bug = (SimpleBug) event.getData();
				if ("summary".equals(event.getFieldName())) {
					EventBusFactory.getInstance().post(
							new BugEvent.GotoRead(BugListWidget.this, bug
									.getId()));
				}
			}
		});

		this.tableItem.setWidth("100%");
		contentLayout.addComponent(this.tableItem);

		this.setSearchCriteria(bugSearchCriteria);
	}

	private void setSearchCriteria(final BugSearchCriteria searchCriteria) {
		this.bugSearchCriteria = searchCriteria;
		this.tableItem.setSearchCriteria(this.bugSearchCriteria);
	}
}
