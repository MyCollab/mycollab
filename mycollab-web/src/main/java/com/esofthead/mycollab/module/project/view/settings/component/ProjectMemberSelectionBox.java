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

import java.util.Collection;
import java.util.List;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.ComboBox;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class ProjectMemberSelectionBox extends ComboBox {
	private static final long serialVersionUID = 1L;

	public ProjectMemberSelectionBox(boolean isNullAllowable) {
		this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
		this.setNullSelectionAllowed(isNullAllowable);

		ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
		criteria.setProjectId(new NumberSearchField(CurrentProjectVariables
				.getProjectId()));
		criteria.setStatus(new StringSearchField(
				ProjectMemberStatusConstants.ACTIVE));

		ProjectMemberService userService = ApplicationContextUtil
				.getSpringBean(ProjectMemberService.class);
		List<SimpleProjectMember> memberList = userService
				.findPagableListByCriteria(new SearchRequest<ProjectMemberSearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));
		loadUserList(memberList);
	}

	private void loadUserList(List<SimpleProjectMember> memberList) {
		for (SimpleProjectMember member : memberList) {
			this.addItem(member);
			this.setItemCaption(member, member.getDisplayName());
			this.setItemIcon(
					member,
					UserAvatarControlFactory.createAvatarResource(
							member.getMemberAvatarId(), 16));
		}
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof String) {
			Collection<?> containerPropertyIds = this.getItemIds();
			for (Object id : containerPropertyIds) {
				if (id instanceof SimpleProjectMember) {
					if (value.equals(((SimpleProjectMember) id).getUsername())) {
						super.setValue(id);
					}
				}
			}
		} else {
			super.setValue(value);
		}
	}
}
