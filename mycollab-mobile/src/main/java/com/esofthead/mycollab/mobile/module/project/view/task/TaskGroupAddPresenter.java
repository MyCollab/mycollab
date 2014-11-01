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

package com.esofthead.mycollab.mobile.module.project.view.task;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePresenter;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.TaskList;
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskGroupAddPresenter extends
		AbstractMobilePresenter<TaskGroupAddView> {
	private static final long serialVersionUID = 1L;

	public TaskGroupAddPresenter() {
		super(TaskGroupAddView.class);
	}

	@Override
	protected void postInitView() {
		view.getEditFormHandlers().addFormHandler(
				new EditFormHandler<SimpleTaskList>() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onSave(final SimpleTaskList item) {
						save(item);
						EventBusFactory.getInstance().post(
								new ShellEvent.NavigateBack(this, null));
					}

					@Override
					public void onSaveAndNew(SimpleTaskList bean) {
					}

					@Override
					public void onCancel() {
					}
				});
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.TASKS)) {

			SimpleTaskList taskList = (SimpleTaskList) data.getParams();
			view.editItem(taskList);
			super.onGo(container, data);

			if (taskList.getId() == null) {
				AppContext
						.addFragment(
								"project/task/taskgroup/add/"
										+ GenericLinkUtils
												.encodeParam(new Object[] { CurrentProjectVariables
														.getProjectId() }),
								AppContext
										.getMessage(TaskGroupI18nEnum.M_VIEW_NEW_TITLE));
			} else {
				AppContext
						.addFragment(
								"project/task/taskgroup/edit/"
										+ GenericLinkUtils.encodeParam(new Object[] {
												CurrentProjectVariables
														.getProjectId(),
												taskList.getId() }), taskList
										.getName());
			}
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

	public void save(TaskList item) {
		ProjectTaskListService taskService = ApplicationContextUtil
				.getSpringBean(ProjectTaskListService.class);

		item.setSaccountid(AppContext.getAccountId());
		item.setProjectid(CurrentProjectVariables.getProjectId());

		if (item.getId() == null) {
			item.setCreateduser(AppContext.getUsername());
			taskService.saveWithSession(item, AppContext.getUsername());
		} else {
			taskService.updateWithSession(item, AppContext.getUsername());
		}

	}
}
