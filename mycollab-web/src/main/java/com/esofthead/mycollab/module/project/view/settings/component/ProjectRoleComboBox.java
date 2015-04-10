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
package com.esofthead.mycollab.module.project.view.settings.component;

import java.util.List;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectRoleService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.ComboBox;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectRoleComboBox extends ComboBox {
	private static final long serialVersionUID = 1L;

	private List<SimpleProjectRole> roleList;

	@SuppressWarnings("unchecked")
	public ProjectRoleComboBox() {
		super();
		this.setImmediate(true);
		this.setItemCaptionMode(ItemCaptionMode.PROPERTY);

		ProjectRoleSearchCriteria criteria = new ProjectRoleSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		criteria.setProjectId(new NumberSearchField(CurrentProjectVariables
				.getProjectId()));

		ProjectRoleService roleService = ApplicationContextUtil
				.getSpringBean(ProjectRoleService.class);
		roleList = roleService
				.findPagableListByCriteria(new SearchRequest<ProjectRoleSearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));

		BeanContainer<String, SimpleProjectRole> beanItem = new BeanContainer<String, SimpleProjectRole>(
				SimpleProjectRole.class);
		beanItem.setBeanIdProperty("id");

		for (SimpleProjectRole role : roleList) {
			beanItem.addBean(role);
		}

		SimpleProjectRole ownerRole = new SimpleProjectRole();
		ownerRole.setId(-1);
		ownerRole.setRolename("Project Owner");
		beanItem.addBean(ownerRole);

		this.setNullSelectionAllowed(false);
		this.setContainerDataSource(beanItem);
		this.setItemCaptionPropertyId("rolename");
		if (roleList.size() > 0) {
			SimpleProjectRole role = roleList.get(0);
			this.setValue(role.getId());
		} else {
			this.setValue(-1);
		}
	}

}
