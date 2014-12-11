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
package com.esofthead.mycollab.module.project.view.task;

import java.util.Arrays;

import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.events.TaskListEvent;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.view.parameters.TaskFilterParameter;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.WebResourceIds;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickEvent;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class FilterTaskViewImpl extends AbstractPageView implements
		FilterTaskView {
	private static final long serialVersionUID = 1L;

	private Label headerText;
	private MVerticalLayout rightColumn;
	private MVerticalLayout leftColumn;
	private TaskTableDisplay taskTableDisplay;
	private UnresolvedTaskByAssigneeWidget unresolvedTaskByAssigneeWidget;
	private UnresolvedTaskByPriorityWidget unresolvedTaskByPriorityWidget;

	public FilterTaskViewImpl() {
		this.setMargin(new MarginInfo(false, true, true, true));

		final MHorizontalLayout header = new MHorizontalLayout()
				.withSpacing(true)
				.withMargin(new MarginInfo(true, false, true, false))
				.withStyleName(UIConstants.HEADER_VIEW).withWidth("100%");

		Image titleIcon = new Image(null,
				MyCollabResource.newResource(WebResourceIds._24_project_task));

		headerText = new Label();
		headerText.setSizeUndefined();
		headerText.setStyleName(UIConstants.HEADER_TEXT);

		Button backtoTaskListBtn = new Button(
				AppContext.getMessage(TaskI18nEnum.BUTTON_BACK_TO_DASHBOARD),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						EventBusFactory.getInstance()
								.post(new TaskListEvent.GotoTaskListScreen(
										this, null));

					}
				});
		backtoTaskListBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

		header.with(titleIcon, headerText, backtoTaskListBtn)
				.withAlign(titleIcon, Alignment.TOP_LEFT)
				.withAlign(headerText, Alignment.MIDDLE_LEFT)
				.withAlign(backtoTaskListBtn, Alignment.MIDDLE_RIGHT)
				.expand(headerText);

		this.addComponent(header);

		MHorizontalLayout contentLayout = new MHorizontalLayout().withWidth(
				"100%").withSpacing(true);
		this.addComponent(contentLayout);

		this.taskTableDisplay = new TaskTableDisplay(
				Arrays.asList(TaskTableFieldDef.taskname,
						TaskTableFieldDef.startdate, TaskTableFieldDef.duedate,
						TaskTableFieldDef.percentagecomplete));

		this.taskTableDisplay.addTableListener(new TableClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void itemClick(final TableClickEvent event) {
				final SimpleTask task = (SimpleTask) event.getData();
				if (Task.Field.taskname.equalTo(event.getFieldName())) {
					EventBusFactory.getInstance().post(
							new TaskEvent.GotoRead(FilterTaskViewImpl.this,
									task.getId()));
				}
			}
		});
		taskTableDisplay.setWidth("100%");
		taskTableDisplay.setStyleName("filter-task-table");

		leftColumn = new MVerticalLayout().withStyleName("depotComp")
				.withMargin(new MarginInfo(true, true, false, false))
				.with(taskTableDisplay);

		unresolvedTaskByAssigneeWidget = new UnresolvedTaskByAssigneeWidget();
		unresolvedTaskByPriorityWidget = new UnresolvedTaskByPriorityWidget();
		rightColumn = new MVerticalLayout()
				.withWidth("300px")
				.withMargin(false)
				.with(unresolvedTaskByAssigneeWidget,
						unresolvedTaskByPriorityWidget);

		contentLayout.with(leftColumn, rightColumn).expand(leftColumn);
	}

	@Override
	public void filterTasks(TaskFilterParameter filterParam) {
		headerText.setValue(filterParam.getScreenTitle());
		taskTableDisplay.setSearchCriteria(filterParam.getSearchCriteria());

		TaskSearchCriteria searchCriteria = new TaskSearchCriteria();
		searchCriteria.setProjectid(new NumberSearchField(
				CurrentProjectVariables.getProjectId()));
		searchCriteria.setStatuses(new SetSearchField<String>(SearchField.AND,
				new String[] { "Open" }));

		unresolvedTaskByAssigneeWidget.setSearchCriteria(searchCriteria);
		unresolvedTaskByPriorityWidget.setSearchCriteria(searchCriteria);
	}

}
