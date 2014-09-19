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

import java.util.HashSet;
import java.util.Set;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.events.TaskEvent;
import com.esofthead.mycollab.mobile.module.project.ui.AbstractListViewComp;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.PreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.0
 *
 */

@ViewComponent
public class TaskListViewImpl extends
		AbstractListViewComp<TaskSearchCriteria, SimpleTask> implements
		TaskListView {

	private static final long serialVersionUID = -3705209608075399509L;

	private SimpleTaskList currentTaskList;

	private Set<PreviewFormHandler<SimpleTaskList>> handlers;

	public TaskListViewImpl() {
		this.addStyleName("task-list-view");
	}

	@Override
	protected AbstractPagedBeanList<TaskSearchCriteria, SimpleTask> createBeanTable() {
		return new TaskListDisplay();
	}

	@Override
	protected Component createRightComponent() {
		NavigationBarQuickMenu editBtn = new NavigationBarQuickMenu();
		editBtn.setButtonCaption("...");
		editBtn.setStyleName("edit-btn");

		ProjectPreviewFormControlsGenerator<SimpleTaskList> controlsGenerator = new ProjectPreviewFormControlsGenerator<SimpleTaskList>(
				this);
		VerticalLayout menuContent = controlsGenerator
				.createButtonControls(ProjectRolePermissionCollections.TASKS);
		Button viewTaskList = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_VIEW),
				new Button.ClickListener() {

					private static final long serialVersionUID = 150675475815367481L;

					@Override
					public void buttonClick(Button.ClickEvent event) {
						EventBusFactory.getInstance().post(
								new TaskEvent.GotoListView(this,
										currentTaskList.getId()));
					}
				});
		viewTaskList.setWidth("100%");

		viewTaskList.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.TASKS));

		controlsGenerator.insertToControlBlock(viewTaskList);
		editBtn.setContent(menuContent);

		return editBtn;
	}

	@Override
	public void addFormHandler(PreviewFormHandler<SimpleTaskList> handler) {
		if (handlers == null) {
			handlers = new HashSet<PreviewFormHandler<SimpleTaskList>>();
		}

		handlers.add(handler);
	}

	@Override
	public SimpleTaskList getBean() {
		return this.currentTaskList;
	}

	@Override
	public void setBean(SimpleTaskList bean) {
		this.currentTaskList = bean;
	}

	@Override
	public void fireAssignForm(SimpleTaskList bean) {
		if (handlers != null) {
			for (PreviewFormHandler<SimpleTaskList> handler : handlers) {
				handler.onAssign(bean);
			}
		}
	}

	@Override
	public void fireEditForm(SimpleTaskList bean) {
		if (handlers != null) {
			for (PreviewFormHandler<SimpleTaskList> handler : handlers) {
				handler.onEdit(bean);
			}
		}
	}

	@Override
	public void showHistory() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireCancelForm(SimpleTaskList bean) {
		if (handlers != null) {
			for (PreviewFormHandler<SimpleTaskList> handler : handlers) {
				handler.onCancel();
			}
		}
	}

	@Override
	public void fireDeleteForm(SimpleTaskList bean) {
		if (handlers != null) {
			for (PreviewFormHandler<SimpleTaskList> handler : handlers) {
				handler.onDelete(bean);
			}
		}
	}

	@Override
	public void fireCloneForm(SimpleTaskList bean) {
		if (handlers != null) {
			for (PreviewFormHandler<SimpleTaskList> handler : handlers) {
				handler.onClone(bean);
			}
		}
	}

	@Override
	public void fireExtraAction(String action, SimpleTaskList bean) {
		if (handlers != null) {
			for (PreviewFormHandler<SimpleTaskList> handler : handlers) {
				handler.onExtraAction(action, bean);
			}
		}
	}

}
