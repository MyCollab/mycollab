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

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.project.view.parameters.TaskFilterParameter;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.mvp.ListCommand;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0.0
 *
 */
public class TaskSearchPresenter extends AbstractPresenter<TaskSearchView>
		implements ListCommand<TaskSearchCriteria> {
	private static final long serialVersionUID = 1L;

	public TaskSearchPresenter() {
		super(TaskSearchView.class);
	}

	@Override
	protected void postInitView() {
		view.getSearchHandlers().addSearchHandler(
				new SearchHandler<TaskSearchCriteria>() {
					@Override
					public void onSearch(TaskSearchCriteria criteria) {
						doSearch(criteria);
					}
				});
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (CurrentProjectVariables
				.canRead(ProjectRolePermissionCollections.TASKS)) {
            ProjectBreadcrumb breadCrumb = ViewManager
                    .getCacheComponent(ProjectBreadcrumb.class);
            breadCrumb.gotoTaskFilter();

			TaskContainer trackerContainer = (TaskContainer) container;
			trackerContainer.removeAllComponents();
			trackerContainer.addComponent(view.getWidget());

			TaskFilterParameter param = (TaskFilterParameter) data.getParams();

			view.setTitle(param.getScreenTitle());
			doSearch(param.getSearchCriteria());
			if (param.getAdvanceSearch()) {
				view.moveToAdvanceSearch();
			} else {
				view.moveToBasicSearch();
			}
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

	@Override
	public void doSearch(TaskSearchCriteria searchCriteria) {
		if (searchCriteria.getTaskName() != null)
			view.setSearchInputValue(searchCriteria.getTaskName().getValue());
		view.getPagedBeanTable().setSearchCriteria(searchCriteria);
	}
}