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
package com.esofthead.mycollab.module.project.view.milestone;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.Milestone;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.view.bug.BugChartComponent;
import com.esofthead.mycollab.module.project.view.bug.BugListWidget;
import com.esofthead.mycollab.module.project.view.bug.BugSimpleDisplayWidget;
import com.esofthead.mycollab.module.project.view.bug.IBugReportDisplayContainer;
import com.esofthead.mycollab.module.project.view.bug.UnresolvedBugsByAssigneeWidget;
import com.esofthead.mycollab.module.project.view.bug.UnresolvedBugsByPriorityWidget;
import com.esofthead.mycollab.module.tracker.BugStatusConstants;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.ToggleButtonGroup;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class MilestoneBugListComp extends VerticalLayout implements
		IBugReportDisplayContainer {
	private static final long serialVersionUID = 1L;

	private Milestone milestone;
	private ToggleButtonGroup viewGroup;

	public MilestoneBugListComp() {
		this.setMargin(true);
		this.constructHeader();
	}

	private void constructHeader() {
		final HorizontalLayout header = new HorizontalLayout();
		header.setSpacing(true);
		header.setWidth("100%");
		final Label taskGroupSelection = new Label(
				AppContext.getMessage(MilestoneI18nEnum.RELATED_BUGS_TAB));
		taskGroupSelection.addStyleName("h2");
		taskGroupSelection.addStyleName(UIConstants.THEME_NO_BORDER);
		header.addComponent(taskGroupSelection);
		header.setExpandRatio(taskGroupSelection, 1.0f);
		header.setComponentAlignment(taskGroupSelection, Alignment.MIDDLE_LEFT);

		this.viewGroup = new ToggleButtonGroup();

		final Button simpleDisplay = new Button(null,
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						MilestoneBugListComp.this.displaySimpleView();
					}
				});
		simpleDisplay.setIcon(MyCollabResource
				.newResource("icons/16/project/list_display.png"));

		this.viewGroup.addButton(simpleDisplay);

		final Button advanceDisplay = new Button(null,
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						MilestoneBugListComp.this.displayAdvancedView();
					}
				});
		advanceDisplay.setIcon(MyCollabResource
				.newResource("icons/16/project/bug_advanced_display.png"));
		this.viewGroup.addButton(advanceDisplay);
		header.addComponent(this.viewGroup);
		header.setComponentAlignment(this.viewGroup, Alignment.MIDDLE_RIGHT);
		this.addComponent(header);
	}

	private void displayAdvancedView() {

		if (this.getComponentCount() > 1) {
			this.removeComponent(this.getComponent(1));
		}

		final HorizontalLayout bodyLayout = new HorizontalLayout();
		bodyLayout.setSpacing(false);
		bodyLayout.setWidth("100%");
		final VerticalLayout leftColumn = new VerticalLayout();
		leftColumn.setMargin(new MarginInfo(false, true, false, false));
		bodyLayout.addComponent(leftColumn);
		bodyLayout.setExpandRatio(leftColumn, 1.0f);
		final VerticalLayout rightColumn = new VerticalLayout();
		bodyLayout.addComponent(rightColumn);

		final UnresolvedBugsByPriorityWidget unresolvedBugWidget = new UnresolvedBugsByPriorityWidget(
				this);
		unresolvedBugWidget.setWidth("100%");
		leftColumn.addComponent(unresolvedBugWidget);

		final BugSearchCriteria unresolvedByPrioritySearchCriteria = new BugSearchCriteria();
		unresolvedByPrioritySearchCriteria.setProjectId(new NumberSearchField(
				CurrentProjectVariables.getProjectId()));
		unresolvedByPrioritySearchCriteria
				.setMilestoneIds(new SetSearchField<Integer>(this.milestone
						.getId()));
		unresolvedByPrioritySearchCriteria
				.setStatuses(new SetSearchField<String>(SearchField.AND,
						new String[] { BugStatusConstants.INPROGRESS,
								BugStatusConstants.OPEN,
								BugStatusConstants.REOPENNED }));
		unresolvedBugWidget
				.setSearchCriteria(unresolvedByPrioritySearchCriteria);

		final UnresolvedBugsByAssigneeWidget unresolvedByAssigneeWidget = new UnresolvedBugsByAssigneeWidget(
				this);
		unresolvedByAssigneeWidget.setWidth("100%");
		leftColumn.addComponent(unresolvedByAssigneeWidget);

		final BugSearchCriteria unresolvedByAssigneeSearchCriteria = new BugSearchCriteria();
		unresolvedByAssigneeSearchCriteria.setProjectId(new NumberSearchField(
				CurrentProjectVariables.getProjectId()));
		unresolvedByAssigneeSearchCriteria
				.setMilestoneIds(new SetSearchField<Integer>(this.milestone
						.getId()));
		unresolvedByAssigneeSearchCriteria
				.setStatuses(new SetSearchField<String>(SearchField.AND,
						new String[] { BugStatusConstants.INPROGRESS,
								BugStatusConstants.OPEN,
								BugStatusConstants.REOPENNED }));
		unresolvedByAssigneeWidget
				.setSearchCriteria(unresolvedByAssigneeSearchCriteria);

		final BugSearchCriteria chartSearchCriteria = new BugSearchCriteria();
		chartSearchCriteria.setProjectId(new NumberSearchField(
				CurrentProjectVariables.getProjectId()));
		chartSearchCriteria.setMilestoneIds(new SetSearchField<Integer>(
				this.milestone.getId()));
		BugChartComponent bugChartComponent = null;
		bugChartComponent = new BugChartComponent(chartSearchCriteria, 400, 200);
		rightColumn.addComponent(bugChartComponent);
		rightColumn.setWidth("400px");

		this.addComponent(bodyLayout);
	}

	@Override
	public void displayBugListWidget(final String title,
			final BugSearchCriteria criteria) {
		if (this.getComponentCount() > 1) {
			this.removeComponent(this.getComponent(1));
		}
		final BugListWidget bugListWidget = new BugListWidget(title,
				"Back to milestone dashboard", criteria, this);
		bugListWidget.setWidth("100%");
		this.addComponent(bugListWidget);
	}

	@Override
	public void displayBugReports() {
		this.displayAdvancedView();
	}

	public void displayBugs(final Milestone milestone) {
		this.milestone = milestone;
		this.displayBugReports();
	}

	private void displaySimpleView() {
		if (this.getComponentCount() > 1) {
			this.removeComponent(this.getComponent(1));
		}

		final BugSearchCriteria criteria = new BugSearchCriteria();
		criteria.setProjectId(new NumberSearchField(CurrentProjectVariables
				.getProjectId()));
		criteria.setMilestoneIds(new SetSearchField<Integer>(this.milestone
				.getId()));

		final BugSimpleDisplayWidget displayWidget = new BugSimpleDisplayWidget();
		this.addComponent(displayWidget);
		displayWidget.setSearchCriteria(criteria);
	}

}
