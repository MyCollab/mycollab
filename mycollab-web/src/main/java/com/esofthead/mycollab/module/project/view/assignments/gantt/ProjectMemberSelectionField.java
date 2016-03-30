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
package com.esofthead.mycollab.module.project.view.assignments.gantt;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.ComboBox;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
class ProjectMemberSelectionField extends ComboBox {
    ProjectMemberSelectionField() {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        this.setNullSelectionAllowed(true);

        ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        criteria.addOrderField(new SearchCriteria.OrderField("memberFullName", SearchCriteria.ASC));
//        criteria.setStatus(StringSearchField.and(ProjectMemberStatusConstants.ACTIVE));

        ProjectMemberService userService = ApplicationContextUtil.getSpringBean(ProjectMemberService.class);
        List<SimpleProjectMember> memberList = userService.findPagableListByCriteria(new SearchRequest<>(criteria, 0, Integer.MAX_VALUE));
        loadUserList(memberList);
    }

    private void loadUserList(List<SimpleProjectMember> memberList) {
        for (SimpleProjectMember member : memberList) {
            this.addItem(member.getUsername());
            this.setItemCaption(member.getUsername(), StringUtils.trim(member.getDisplayName(), 25, true));
            this.setItemIcon(member.getUsername(), UserAvatarControlFactory.createAvatarResource(member.getMemberAvatarId(), 16));
        }
    }
}
