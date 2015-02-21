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

import org.vaadin.dialogs.ConfirmDialog;

import com.esofthead.mycollab.vaadin.ui.MyCollabSession;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.events.TaskListEvent;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskReadPresenter extends AbstractPresenter<TaskReadView> {

	private static final long serialVersionUID = 1L;

	public TaskReadPresenter() {
		super(TaskReadView.class);
	}

	@Override
	protected void postInitView() {
		this.view.getPreviewFormHandlers().addFormHandler(
				new DefaultPreviewFormHandler<SimpleTask>() {

					@Override
					public void onAssign(final SimpleTask data) {
						UI.getCurrent().addWindow(new AssignTaskWindow(data));
					}

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
						ConfirmDialogExt.show(
								UI.getCurrent(),
								AppContext.getMessage(
										GenericI18Enum.DIALOG_DELETE_TITLE,
										SiteConfiguration.getSiteName()),
								AppContext
										.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
								AppContext
										.getMessage(GenericI18Enum.BUTTON_YES),
								AppContext
										.getMessage(GenericI18Enum.BUTTON_NO),
								new ConfirmDialog.Listener() {
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
													.post(new TaskListEvent.GotoTaskListScreen(
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

					@Override
					public void onCancel() {
						EventBusFactory.getInstance()
								.post(new TaskListEvent.GotoTaskListScreen(
										this, null));
					}

					@Override
					public void gotoNext(final SimpleTask data) {
						final ProjectTaskService taskService = ApplicationContextUtil
								.getSpringBean(ProjectTaskService.class);

						final TaskSearchCriteria criteria = new TaskSearchCriteria();
						final SimpleProject project = (SimpleProject) MyCollabSession
								.getVariable("project");
						criteria.setProjectid(new NumberSearchField(
								SearchField.AND, project.getId()));
						criteria.setId(new NumberSearchField(data.getId(),
								NumberSearchField.GREATER));
						final Integer nextId = taskService
								.getNextItemKey(criteria);
						if (nextId != null) {
							EventBusFactory.getInstance().post(
									new TaskEvent.GotoRead(this, nextId));
						} else {
							NotificationUtil.showGotoLastRecordNotification();
						}

					}

					@Override
					public void gotoPrevious(final SimpleTask data) {
						final ProjectTaskService taskService = ApplicationContextUtil
								.getSpringBean(ProjectTaskService.class);

						final TaskSearchCriteria criteria = new TaskSearchCriteria();
						final SimpleProject project = (SimpleProject) MyCollabSession
								.getVariable("project");
						criteria.setProjectid(new NumberSearchField(
								SearchField.AND, project.getId()));
						criteria.setId(new NumberSearchField(data.getId(),
								NumberSearchField.LESSTHAN));
						final Integer nextId = taskService
								.getNextItemKey(criteria);
						if (nextId != null) {
							EventBusFactory.getInstance().post(
									new TaskEvent.GotoRead(this, nextId));
						} else {
							NotificationUtil.showGotoFirstRecordNotification();
						}
					}
				});
	}

	@Override
	protected void onGo(final ComponentContainer container,
			final ScreenData<?> data) {
		if (CurrentProjectVariables
				.canRead(ProjectRolePermissionCollections.TASKS)) {
			final TaskContainer taskContainer = (TaskContainer) container;
			taskContainer.removeAllComponents();

			taskContainer.addComponent(this.view.getWidget());
			if (data.getParams() instanceof Integer) {
				final ProjectTaskService taskService = ApplicationContextUtil
						.getSpringBean(ProjectTaskService.class);
				final SimpleTask task = taskService.findById(
						(Integer) data.getParams(), AppContext.getAccountId());

				if (task != null) {
					this.view.previewItem(task);

					final ProjectBreadcrumb breadCrumb = ViewManager
							.getCacheComponent(ProjectBreadcrumb.class);
					breadCrumb.gotoTaskRead(task);
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
