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

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.events.TaskEvent;
import com.esofthead.mycollab.mobile.module.project.ui.AbstractListViewComp;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskListSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.0
 *
 */

@ViewComponent
public class TaskGroupListViewImpl extends
		AbstractListViewComp<TaskListSearchCriteria, SimpleTaskList> implements
		TaskGroupListView {

	private static final long serialVersionUID = 1604670567813647489L;

	public TaskGroupListViewImpl() {
		this.setCaption(AppContext
				.getMessage(TaskGroupI18nEnum.M_VIEW_LIST_TITLE));
		this.addStyleName("task-group-list-view");
	}

	@Override
	protected AbstractPagedBeanList<TaskListSearchCriteria, SimpleTaskList> createBeanTable() {
		return new TaskGroupListDisplay();
	}

	@Override
	protected Component createRightComponent() {
		Button addTaskList = new Button("", new Button.ClickListener() {

			private static final long serialVersionUID = 2067641610209145531L;

			@Override
			public void buttonClick(Button.ClickEvent event) {
				EventBusFactory.getInstance().post(
						new TaskEvent.GotoListAdd(this, null));
			}
		});
		addTaskList.setStyleName("add-btn");
		return addTaskList;
	}

}
