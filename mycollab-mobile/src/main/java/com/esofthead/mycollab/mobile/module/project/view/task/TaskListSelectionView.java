/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.task;

import com.esofthead.mycollab.common.i18n.DayI18nEnum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList.RowDisplayHandler;
import com.esofthead.mycollab.mobile.ui.AbstractSelectionView;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskListSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.0
 */
public class TaskListSelectionView extends
		AbstractSelectionView<SimpleTaskList> {

	private static final long serialVersionUID = 6643783263154617870L;

	private TaskListSearchCriteria searchCriteria;
	private TaskGroupListDisplay taskGroupList;

	private TaskListRowDisplayHandler rowDisplayHandler = new TaskListRowDisplayHandler();

	public TaskListSelectionView() {
		super();
		createUI();
		this.setCaption(AppContext
				.getMessage(TaskGroupI18nEnum.M_VIEW_TASKLIST_LOOKUP));
	}

	@Override
	public void load() {
		this.searchCriteria = new TaskListSearchCriteria();
		this.searchCriteria.setProjectId(new NumberSearchField(
				CurrentProjectVariables.getProjectId()));
		this.searchCriteria.setSaccountid(new NumberSearchField(AppContext
				.getAccountId()));
		this.taskGroupList.setSearchCriteria(searchCriteria);

		SimpleTaskList blankTaskList = new SimpleTaskList();
		this.taskGroupList.getListContainer().addComponentAsFirst(
				rowDisplayHandler.generateRow(blankTaskList, 0));

	}

	private void createUI() {
		this.taskGroupList = new TaskGroupListDisplay();
		this.taskGroupList.setWidth("100%");
		this.setContent(this.taskGroupList);
		this.taskGroupList.setRowDisplayHandler(rowDisplayHandler);
	}

	private class TaskListRowDisplayHandler implements
			RowDisplayHandler<SimpleTaskList> {

		@Override
		public Component generateRow(final SimpleTaskList taskList, int rowIndex) {
			HorizontalLayout taskListLayout = new HorizontalLayout();
			taskListLayout.setStyleName("task-list-layout");
			taskListLayout.addStyleName("list-item");
			taskListLayout.setWidth("100%");
			taskListLayout
					.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

						private static final long serialVersionUID = 6510342655212187338L;

						@Override
						public void layoutClick(
								LayoutEvents.LayoutClickEvent event) {
							selectionField.fireValueChange(taskList);
							TaskListSelectionView.this.getNavigationManager()
									.navigateBack();
						}
					});

			if (taskList.getId() == null) {
				taskListLayout.addStyleName("blank-item");
				return taskListLayout;
			}

			VerticalLayout taskListInfo = new VerticalLayout();
			taskListInfo.setStyleName("task-list-info");
			Label b = new Label(taskList.getName());
			b.setWidth("100%");
			b.setStyleName("task-list-name");
			b.addStyleName("fake-button");
			taskListInfo.addComponent(b);

			Label taskListUpdateTime = new Label(AppContext.getMessage(
					DayI18nEnum.LAST_UPDATED_ON,
					AppContext.formatDateTime(taskList.getLastupdatedtime())));
			taskListUpdateTime.setWidthUndefined();
			taskListUpdateTime.setStyleName("last-updated-time");
			taskListInfo.addComponent(taskListUpdateTime);
			taskListLayout.addComponent(taskListInfo);

			if (taskList.getNumOpenTasks() > 0) {
				Label activeTasksNum = new Label(taskList.getNumOpenTasks()
						+ "");
				activeTasksNum.setWidthUndefined();
				activeTasksNum.setStyleName("active-task-num");
				taskListLayout.addComponent(activeTasksNum);
				taskListLayout.setComponentAlignment(activeTasksNum,
						Alignment.MIDDLE_LEFT);
			}
			taskListLayout.setExpandRatio(taskListInfo, 1.0f);

			return taskListLayout;
		}

	}

}
