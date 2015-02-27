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

package com.esofthead.mycollab.module.project.ui.components;

import java.util.List;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskListSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.ComboBox;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectTaskListComboBox extends ComboBox {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public ProjectTaskListComboBox() {
		super();
		this.setItemCaptionMode(ItemCaptionMode.PROPERTY);

		TaskListSearchCriteria criteria = new TaskListSearchCriteria();
		criteria.setProjectId(new NumberSearchField(SearchField.AND,
				CurrentProjectVariables.getProjectId()));

		ProjectTaskListService taskListService = ApplicationContextUtil
				.getSpringBean(ProjectTaskListService.class);
		List<SimpleTaskList> taskLists = taskListService
				.findPagableListByCriteria(new SearchRequest<>(
						criteria, 0, Integer.MAX_VALUE));

		BeanContainer<String, SimpleTaskList> beanItem = new BeanContainer<>(
				SimpleTaskList.class);
		beanItem.setBeanIdProperty("id");
		beanItem.addAll(taskLists);

		this.setContainerDataSource(beanItem);
		this.setItemCaptionPropertyId("name");
	}

}
