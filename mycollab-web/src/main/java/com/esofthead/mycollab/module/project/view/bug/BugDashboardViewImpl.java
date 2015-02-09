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

import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.events.BugComponentEvent;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.events.BugVersionEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.ui.components.HeaderView;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractLazyPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.SplitButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.WebResourceIds;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.GregorianCalendar;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
@ViewComponent
public class BugDashboardViewImpl extends AbstractLazyPageView implements
		BugDashboardView {

	private MVerticalLayout leftColumn, rightColumn;
	private MHorizontalLayout header;

	private void initUI() {
		this.setMargin(new MarginInfo(false, true, false, true));
		header = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false))
				.withWidth("100%");
		header.addStyleName("hdr-view");
		header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		this.addComponent(header);

		final MHorizontalLayout body = new MHorizontalLayout().withMargin(false).withWidth("100%");

		this.leftColumn = new MVerticalLayout().withMargin(new MarginInfo(false, true, false, false));
		body.with(this.leftColumn).expand(leftColumn);

		this.rightColumn = new MVerticalLayout().withMargin(false);

		body.with(this.rightColumn).withAlign(rightColumn, Alignment.TOP_RIGHT);

		this.addComponent(body);

		initHeader();
	}

	private void initHeader() {
		final Label title = new HeaderView(ProjectTypeConstants.BUG,
				AppContext.getMessage(BugI18nEnum.VIEW_BUG_DASHBOARD_TITLE));
		header.with(title).withAlign(title, Alignment.MIDDLE_LEFT).expand(title);

		final Button createBugBtn = new Button(
				AppContext.getMessage(BugI18nEnum.BUTTON_NEW_BUG),
				new Button.ClickListener() {
					@Override
					public void buttonClick(final ClickEvent event) {
						EventBusFactory.getInstance().post(
								new BugEvent.GotoAdd(this, null));
					}
				});
		createBugBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.BUGS));
		createBugBtn.setIcon(FontAwesome.PLUS);
		final SplitButton controlsBtn = new SplitButton(createBugBtn);
		controlsBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
		controlsBtn.setWidthUndefined();

		final VerticalLayout btnControlsLayout = new VerticalLayout();
		final Button createComponentBtn = new Button(
				AppContext.getMessage(BugI18nEnum.BUTTON_NEW_COMPONENT),
				new Button.ClickListener() {
					@Override
					public void buttonClick(final ClickEvent event) {
						controlsBtn.setPopupVisible(false);
						EventBusFactory.getInstance().post(
								new BugComponentEvent.GotoAdd(this, null));
					}
				});
		createComponentBtn.setStyleName("link");
		createComponentBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.COMPONENTS));
		btnControlsLayout.addComponent(createComponentBtn);

		final Button createVersionBtn = new Button(
				AppContext.getMessage(BugI18nEnum.BUTTON_NEW_VERSION),
				new Button.ClickListener() {
					@Override
					public void buttonClick(final ClickEvent event) {
						controlsBtn.setPopupVisible(false);
						EventBusFactory.getInstance().post(
								new BugVersionEvent.GotoAdd(this, null));
					}
				});
		createVersionBtn.setStyleName("link");
		createVersionBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.VERSIONS));
		btnControlsLayout.addComponent(createVersionBtn);

		controlsBtn.setContent(btnControlsLayout);
		header.addComponent(controlsBtn);
	}

	@Override
	protected void displayView() {
		initUI();

		this.rightColumn.setWidth("400px");

		final SimpleProject project = CurrentProjectVariables.getProject();

		final MyBugListWidget myBugListWidget = new MyBugListWidget();
		this.leftColumn.addComponent(myBugListWidget);
		final BugSearchCriteria myBugsSearchCriteria = new BugSearchCriteria();
		myBugsSearchCriteria
				.setProjectId(new NumberSearchField(project.getId()));
		myBugsSearchCriteria.setStatuses(new SetSearchField<>(
				SearchField.AND, new String[] { BugStatus.InProgress.name(),
						BugStatus.Open.name(), BugStatus.ReOpened.name(),
						BugStatus.Resolved.name() }));
		myBugsSearchCriteria.setAssignuser(new StringSearchField(AppContext
				.getUsername()));

		myBugListWidget.setSearchCriteria(myBugsSearchCriteria);

		final DueBugWidget dueBugWidget = new DueBugWidget();
		this.leftColumn.addComponent(dueBugWidget);
		final BugSearchCriteria dueDefectsCriteria = new BugSearchCriteria();
		dueDefectsCriteria.setProjectId(new NumberSearchField(project.getId()));
		dueDefectsCriteria.setDueDate(new DateSearchField(SearchField.AND,
				DateTimeSearchField.LESSTHANEQUAL, new GregorianCalendar()
						.getTime()));
		dueDefectsCriteria.setStatuses(new SetSearchField<>(
				SearchField.AND, new String[] { BugStatus.InProgress.name(),
						BugStatus.Open.name(), BugStatus.ReOpened.name(),
						BugStatus.Resolved.name() }));
		dueBugWidget.setSearchCriteria(dueDefectsCriteria);

		final RecentBugUpdateWidget updateBugWidget = new RecentBugUpdateWidget();
		this.leftColumn.addComponent(updateBugWidget);

		// Unresolved by assignee
		final UnresolvedBugsByAssigneeWidget2 unresolvedByAssigneeWidget = new UnresolvedBugsByAssigneeWidget2();
		final BugSearchCriteria unresolvedByAssigneeSearchCriteria = new BugSearchCriteria();
		unresolvedByAssigneeSearchCriteria.setProjectId(new NumberSearchField(
				project.getId()));
		unresolvedByAssigneeSearchCriteria
				.setStatuses(new SetSearchField<>(SearchField.AND,
						new String[] { BugStatus.InProgress.name(),
								BugStatus.Open.name(),
								BugStatus.ReOpened.name() }));
		unresolvedByAssigneeWidget
				.setSearchCriteria(unresolvedByAssigneeSearchCriteria);
		this.rightColumn.addComponent(unresolvedByAssigneeWidget);

		// Unresolve by priority widget
		final UnresolvedBugsByPriorityWidget2 unresolvedByPriorityWidget = new UnresolvedBugsByPriorityWidget2();
		final BugSearchCriteria unresolvedByPrioritySearchCriteria = new BugSearchCriteria();
		unresolvedByPrioritySearchCriteria.setProjectId(new NumberSearchField(
				project.getId()));
		unresolvedByPrioritySearchCriteria
				.setStatuses(new SetSearchField<>(SearchField.AND,
						new String[] { BugStatus.InProgress.name(),
								BugStatus.Open.name(),
								BugStatus.ReOpened.name() }));
		unresolvedByPriorityWidget
				.setSearchCriteria(unresolvedByPrioritySearchCriteria);
		this.rightColumn.addComponent(unresolvedByPriorityWidget);

		// bug chart
		final BugSearchCriteria recentDefectsCriteria = new BugSearchCriteria();
		recentDefectsCriteria.setProjectId(new NumberSearchField(project
				.getId()));
		updateBugWidget.setSearchCriteria(recentDefectsCriteria);

		final BugSearchCriteria chartSearchCriteria = new BugSearchCriteria();
		chartSearchCriteria.setProjectId(new NumberSearchField(
				CurrentProjectVariables.getProjectId()));
		BugChartComponent bugChartComponent = new BugChartComponent(chartSearchCriteria, 400, 200);
		this.rightColumn.addComponent(bugChartComponent);
	}
}
