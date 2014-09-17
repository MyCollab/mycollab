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
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.events.TaskEvent;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePresenter;
import com.esofthead.mycollab.mobile.ui.ConfirmDialog;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class TaskReadPresenter extends AbstractMobilePresenter<TaskReadView> {

	private static final long serialVersionUID = 1L;

	public TaskReadPresenter() {
		super(TaskReadView.class);
	}

	@Override
	protected void postInitView() {
		this.view.getPreviewFormHandlers().addFormHandler(
				new DefaultPreviewFormHandler<SimpleTask>() {

					@Override
					public void onEdit(final SimpleTask data) {
						EventBusFactory.getInstance().post(
								new TaskEvent.GotoEdit(this, data));
					}

					@Override
					public void onAdd(SimpleTask data) {
						EventBusFactory.getInstance().post(
								new TaskEvent.GotoAdd(this, null));
					}

					@Override
					public void onDelete(final SimpleTask data) {
						ConfirmDialog.show(
								UI.getCurrent(),
								AppContext
										.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
								AppContext
										.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
								AppContext
										.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
								new ConfirmDialog.CloseListener() {
									private static final long serialVersionUID = 1L;

									@Override
									public void onClose(
											final ConfirmDialog dialog) {
										if (dialog.isConfirmed()) {
											final ProjectTaskService taskService = ApplicationContextUtil
													.getSpringBean(ProjectTaskService.class);
											taskService.removeWithSession(
													data.getId(),
													AppContext.getUsername(),
													AppContext.getAccountId());
											EventBusFactory
													.getInstance()
													.post(new ShellEvent.NavigateBack(
															this, null));
										}
									}
								});
					}

					@Override
					public void onClone(final SimpleTask data) {
						final Task cloneData = (Task) data.copy();
						cloneData.setId(null);
						EventBusFactory.getInstance().post(
								new TaskEvent.GotoEdit(this, cloneData));
					}
				});
	}

	@Override
	protected void onGo(final ComponentContainer container,
			final ScreenData<?> data) {
		if (CurrentProjectVariables
				.canRead(ProjectRolePermissionCollections.TASKS)) {

			if (data.getParams() instanceof Integer) {
				final ProjectTaskService taskService = ApplicationContextUtil
						.getSpringBean(ProjectTaskService.class);
				final SimpleTask task = taskService.findById(
						(Integer) data.getParams(), AppContext.getAccountId());

				if (task != null) {
					super.onGo(container, data);
					this.view.previewItem(task);

					AppContext.addFragment(
							"project/task/preview/"
									+ GenericLinkUtils
											.encodeParam(new Object[] {
													CurrentProjectVariables
															.getProjectId(),
													task.getId() }), task
									.getTaskname());
				} else {
					NotificationUtil.showRecordNotExistNotification();
					return;
				}
			}
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}
}
