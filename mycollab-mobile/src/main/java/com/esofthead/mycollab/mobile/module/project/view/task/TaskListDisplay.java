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

import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.0
 *
 */
public class TaskListDisplay
		extends
		DefaultPagedBeanList<ProjectTaskService, TaskSearchCriteria, SimpleTask> {

	private static final long serialVersionUID = 1469872908434812706L;

	public TaskListDisplay() {
		super(ApplicationContextUtil.getSpringBean(ProjectTaskService.class),
				new TaskRowDisplayHandler());
	}

	private static class TaskRowDisplayHandler implements
			RowDisplayHandler<SimpleTask> {

		@Override
		public Component generateRow(final SimpleTask task, int rowIndex) {
			Button b = new Button(task.getTaskname());
			b.setWidth("100%");
			b.addStyleName("list-item");
			return b;
		}

	}

}
