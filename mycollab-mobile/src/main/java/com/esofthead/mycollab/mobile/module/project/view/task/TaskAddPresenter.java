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

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.ui.InsideProjectNavigationMenu;
import com.esofthead.mycollab.mobile.module.project.ui.form.field.ProjectFormAttachmentUploadField;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePresenter;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.0
 */
public class TaskAddPresenter extends AbstractMobilePresenter<TaskAddView> {

	private static final long serialVersionUID = -1243069642966773053L;

	public TaskAddPresenter() {
		super(TaskAddView.class);
	}

	@Override
	protected void postInitView() {
		view.getEditFormHandlers().addFormHandler(
				new EditFormHandler<SimpleTask>() {

					private static final long serialVersionUID = 9034340428921755073L;

					@Override
					public void onSave(SimpleTask bean) {
						saveTask(bean);
						EventBusFactory.getInstance().post(
								new ShellEvent.NavigateBack(this, null));
					}

					@Override
					public void onSaveAndNew(SimpleTask bean) {
					}

					@Override
					public void onCancel() {
					}

				});
	}

	@Override
	protected void onGo(ComponentContainer navigator, ScreenData<?> data) {
		if (CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.TASKS)) {
			InsideProjectNavigationMenu projectModuleMenu = (InsideProjectNavigationMenu) ((MobileNavigationManager) UI
					.getCurrent().getContent()).getNavigationMenu();
			projectModuleMenu.selectButton(AppContext
					.getMessage(ProjectCommonI18nEnum.VIEW_TASK));

			SimpleTask task = (SimpleTask) data.getParams();
			view.editItem(task);
			super.onGo(navigator, data);
			if (task.getId() == null) {
				AppContext
						.addFragment(
								"project/task/task/add/"
										+ GenericLinkUtils.encodeParam(CurrentProjectVariables
                                                .getProjectId(),
										task.getTasklistid()),
								AppContext
										.getMessage(TaskI18nEnum.FORM_NEW_TASK_TITLE));
			} else {
				AppContext.addFragment(
						ProjectLinkGenerator.generateTaskEditLink(
								task.getTaskkey(), task.getProjectShortname()),
						task.getTaskname());
			}
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

	private void saveTask(SimpleTask task) {
		ProjectTaskService taskService = ApplicationContextUtil
				.getSpringBean(ProjectTaskService.class);

		task.setSaccountid(AppContext.getAccountId());
		task.setProjectid(CurrentProjectVariables.getProjectId());
		if (task.getPercentagecomplete() == null) {
			task.setPercentagecomplete(new Double(0));
			task.setStatus(StatusI18nEnum.Open.name());
		} else if (task.getPercentagecomplete().doubleValue() == 100d) {
			task.setStatus(StatusI18nEnum.Closed.name());
		} else {
			task.setStatus(StatusI18nEnum.Open.name());
		}

		if (task.getId() == null) {
			task.setLogby(AppContext.getUsername());
			int taskId = taskService.saveWithSession(task,
					AppContext.getUsername());
			ProjectFormAttachmentUploadField uploadField = view
					.getAttachUploadField();
			uploadField.saveContentsToRepo(
					CurrentProjectVariables.getProjectId(),
					AttachmentType.PROJECT_TASK_TYPE, taskId);
		} else {
			taskService.updateWithSession(task, AppContext.getUsername());
			ProjectFormAttachmentUploadField uploadField = view
					.getAttachUploadField();
			uploadField.saveContentsToRepo();
		}
	}

}
