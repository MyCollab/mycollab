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

import com.esofthead.mycollab.mobile.ui.AbstractSelectionCustomField;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.data.Property;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.0
 */
public class TaskListSelectionField extends
		AbstractSelectionCustomField<Integer, SimpleTaskList> {

	private static final long serialVersionUID = -221268327412224730L;

	public TaskListSelectionField() {
		super(TaskListSelectionView.class);
	}

	@Override
	public void setPropertyDataSource(Property newDataSource) {
		Object value = newDataSource.getValue();
		if (value instanceof Integer) {
			setTaskListByVal((Integer) value);
		}
		super.setPropertyDataSource(newDataSource);
	}

	private void setTaskListByVal(Integer value) {
		ProjectTaskListService service = ApplicationContextUtil
				.getSpringBean(ProjectTaskListService.class);
		SimpleTaskList taskList = service.findById(value,
				AppContext.getAccountId());
		if (taskList != null) {
			setInternalTaskList(taskList);
		}
	}

	private void setInternalTaskList(SimpleTaskList taskList) {
		this.beanItem = taskList;
		this.navButton.setCaption(taskList.getName());
	}

	@Override
	public void fireValueChange(SimpleTaskList data) {
		setInternalTaskList(data);
		setInternalValue(data.getId());
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}

}
