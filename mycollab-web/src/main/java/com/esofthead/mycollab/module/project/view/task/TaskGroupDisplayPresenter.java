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

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskListSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.project.view.ProjectGenericListPresenter;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskGroupDisplayPresenter
		extends
		ProjectGenericListPresenter<TaskGroupDisplayView, TaskListSearchCriteria, SimpleTaskList> {

	private static final long serialVersionUID = 1L;

	private final ProjectTaskListService taskListService;

	public TaskGroupDisplayPresenter() {
		super(TaskGroupDisplayView.class, TaskGroupNoItemView.class);

		taskListService = ApplicationContextUtil
				.getSpringBean(ProjectTaskListService.class);
	}

	@Override
	protected void postInitView() {
		// Override to avoid presenter setting up search handlers
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (CurrentProjectVariables
				.canRead(ProjectRolePermissionCollections.TASKS)) {

			final TaskListSearchCriteria criteria = new TaskListSearchCriteria();
			criteria.setProjectId(new NumberSearchField(CurrentProjectVariables
					.getProjectId()));

			int totalCount = taskListService.getTotalCount(criteria);

			if (totalCount > 0) {
				view.displayTaskList();
				displayListView(container, data);
			} else {
				displayNoExistItems(container, data);
			}

			ProjectBreadcrumb breadCrumb = ViewManager
					.getView(ProjectBreadcrumb.class);
			breadCrumb.gotoTaskDashboard();
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

	@Override
	public ISearchableService<TaskListSearchCriteria> getSearchService() {
		return taskListService;
	}

	@Override
	protected void deleteSelectedItems() {
		throw new UnsupportedOperationException(
				"This presenter doesn't support this operation");
	}
}
