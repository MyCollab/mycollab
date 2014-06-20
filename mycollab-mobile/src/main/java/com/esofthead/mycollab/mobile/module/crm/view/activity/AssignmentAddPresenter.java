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
package com.esofthead.mycollab.mobile.module.crm.view.activity;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmGenericPresenter;
import com.esofthead.mycollab.module.crm.domain.Task;
import com.esofthead.mycollab.module.crm.service.TaskService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.HistoryViewManager;
import com.esofthead.mycollab.vaadin.mvp.NullViewState;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewState;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class AssignmentAddPresenter extends
		CrmGenericPresenter<AssignmentAddView> {
	private static final long serialVersionUID = -8546619959063314947L;

	public AssignmentAddPresenter() {
		super(AssignmentAddView.class);
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
					EventBus.getInstance().fireEvent(
							new ActivityEvent.GotoList(this, null));
				}
			}

			@Override
			public void onCancel() {
				ViewState viewState = HistoryViewManager.back();
				if (viewState instanceof NullViewState) {
					EventBus.getInstance().fireEvent(
							new ActivityEvent.GotoList(this, null));
				}
			}

			@Override
			public void onSaveAndNew(final Task item) {
				save(item);
				EventBus.getInstance().fireEvent(
						new ActivityEvent.TaskAdd(this, null));
			}
		});
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (AppContext.canWrite(RolePermissionCollections.CRM_TASK)) {

			Task task = null;
			if (data.getParams() instanceof Task) {
				task = (Task) data.getParams();
			} else if (data.getParams() instanceof Integer) {
				TaskService taskService = ApplicationContextUtil
						.getSpringBean(TaskService.class);
				task = taskService.findByPrimaryKey((Integer) data.getParams(),
						AppContext.getAccountId());
				if (task == null) {
					NotificationUtil.showRecordNotExistNotification();
					return;
				}
			} else {
				throw new MyCollabException("Do not support param data: "
						+ data);
			}

			super.onGo(container, data);
			view.editItem(task);

			if (task.getId() == null) {
				AppContext.addFragment("crm/activity/task/add/",
						"Add Activity Task");
			} else {
				AppContext.addFragment("crm/activity/task/edit/"
						+ UrlEncodeDecoder.encode(task.getId()),
						"Edit Activity Task: " + task.getSubject());
			}
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

	public void save(Task item) {
		TaskService taskService = ApplicationContextUtil
				.getSpringBean(TaskService.class);

		item.setSaccountid(AppContext.getAccountId());
		if (item.getId() == null) {
			taskService.saveWithSession(item, AppContext.getUsername());
		} else {
			taskService.updateWithSession(item, AppContext.getUsername());
		}

	}
}
