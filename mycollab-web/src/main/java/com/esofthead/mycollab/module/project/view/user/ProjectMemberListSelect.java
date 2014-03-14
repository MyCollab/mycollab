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
package com.esofthead.mycollab.module.project.view.user;

import java.util.List;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.ValueComboBox;
import com.vaadin.data.util.BeanContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public @SuppressWarnings("serial")
class ProjectMemberListSelect extends ValueComboBox {

	public ProjectMemberListSelect() {
		//super("username");
		//this.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		//this.setMultiSelect(true);

		ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
		criteria.setProjectId(new NumberSearchField(CurrentProjectVariables
				.getProjectId()));

		ProjectMemberService userService = ApplicationContextUtil
				.getSpringBean(ProjectMemberService.class);
		List<SimpleProjectMember> userList = userService
				.findPagableListByCriteria(new SearchRequest<ProjectMemberSearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));

		BeanContainer<String, SimpleProjectMember> beanItem = new BeanContainer<String, SimpleProjectMember>(
				SimpleProjectMember.class);
		beanItem.setBeanIdProperty("username");

		for (SimpleProjectMember user : userList) {
			beanItem.addBean(user);
		}

		this.setContainerDataSource(beanItem);
		this.setItemCaptionPropertyId("displayName");
		//this.setRows(4);
	}
}
