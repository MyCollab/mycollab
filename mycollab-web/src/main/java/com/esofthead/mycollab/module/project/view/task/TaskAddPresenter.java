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

import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.events.TaskListEvent;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.HistoryViewManager;
import com.esofthead.mycollab.vaadin.mvp.NullViewState;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.ViewState;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.AttachmentUploadField;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskAddPresenter extends AbstractPresenter<TaskAddView> {
	private static final long serialVersionUID = 1L;

	public TaskAddPresenter() {
		super(TaskAddView.class);
	}

	@Override
	protected void postInitView() {
		view.getEditFormHandlers().addFormHandler(new EditFormHandler<Task>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSave(final Task item) {
				save(item);
				ViewState viewState = HistoryViewManager.back();
				if (viewState instanceof NullViewState) {
					EventBusFactory.getInstance().post(
							new TaskListEvent.GotoTaskListScreen(this, null));
				}
			}

			@Override
			public void onCancel() {
				ViewState viewState = HistoryViewManager.back();
				if (viewState instanceof NullViewState) {
					EventBusFactory.getInstance().post(
							new TaskListEvent.GotoTaskListScreen(this, null));
				}
			}

			@Override
			public void onSaveAndNew(final Task item) {
				save(item);
				EventBusFactory.getInstance().post(
						new TaskEvent.GotoAdd(this, null));
			}
		});
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.TASKS)) {
			TaskContainer taskContainer = (TaskContainer) container;
			taskContainer.removeAllComponents();

			taskContainer.addComponent(view.getWidget());

			SimpleTask task = (SimpleTask) data.getParams();
			view.editItem(task);

			ProjectBreadcrumb breadCrumb = ViewManager
					.getCacheComponent(ProjectBreadcrumb.class);
			if (task.getId() == null) {
				breadCrumb.gotoTaskAdd();
			} else {
				breadCrumb.gotoTaskEdit(task);
			}
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

	private void save(Task item) {
		ProjectTaskService taskService = ApplicationContextUtil
				.getSpringBean(ProjectTaskService.class);

		item.setSaccountid(AppContext.getAccountId());
		item.setProjectid(CurrentProjectVariables.getProjectId());
		if (item.getPercentagecomplete() == null) {
			item.setPercentagecomplete(new Double(0));
			item.setStatus(StatusI18nEnum.Open.name());
		} else if (item.getPercentagecomplete().doubleValue() == 100d) {
			item.setStatus(StatusI18nEnum.Closed.name());
		} else {
			item.setStatus(StatusI18nEnum.Open.name());
		}

		if (item.getId() == null) {
			item.setLogby(AppContext.getUsername());
			int taskId = taskService.saveWithSession(item,
					AppContext.getUsername());
			AttachmentUploadField uploadField = view.getAttachUploadField();
			String attachPath = AttachmentUtils.getProjectTaskAttachmentPath(
					AppContext.getAccountId(),
					CurrentProjectVariables.getProjectId(), taskId);
			uploadField.saveContentsToRepo(attachPath);
		} else {
			taskService.updateWithSession(item, AppContext.getUsername());
			AttachmentUploadField uploadField = view.getAttachUploadField();
			String attachPath = AttachmentUtils.getProjectTaskAttachmentPath(
					AppContext.getAccountId(),
					CurrentProjectVariables.getProjectId(), item.getId());
			uploadField.saveContentsToRepo(attachPath);
		}
	}
}
