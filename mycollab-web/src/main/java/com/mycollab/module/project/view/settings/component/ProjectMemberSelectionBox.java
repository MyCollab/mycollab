/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.settings.component;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectMemberStatusConstants;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.IconGenerator;
import com.vaadin.ui.ItemCaptionGenerator;

import java.util.List;
import java.util.Optional;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class ProjectMemberSelectionBox extends ComboBox<SimpleProjectMember> implements Converter<SimpleProjectMember, String> {
    private static final long serialVersionUID = 1L;

    private List<SimpleProjectMember> members;

    public ProjectMemberSelectionBox(boolean isNullAllowable) {
        this.setEmptySelectionAllowed(isNullAllowable);

        ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
        criteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        criteria.setStatuses(new SetSearchField<>(ProjectMemberStatusConstants.ACTIVE));
        criteria.addOrderField(new SearchCriteria.OrderField("memberFullName", SearchCriteria.ASC));

        ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
        members = (List<SimpleProjectMember>) projectMemberService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
        loadUserList();
    }

    private void loadUserList() {
        this.setItems(members);
        this.setItemCaptionGenerator((ItemCaptionGenerator<SimpleProjectMember>) member -> StringUtils.trim(member.getDisplayName(), 25, true));
        this.setItemIconGenerator((IconGenerator<SimpleProjectMember>) member -> UserAvatarControlFactory.createAvatarResource(member.getMemberAvatarId(), 16));
    }

    @Override
    public Result<String> convertToModel(SimpleProjectMember projectMember, ValueContext valueContext) {
        return Result.ok(projectMember.getUsername());
    }

    @Override
    public SimpleProjectMember convertToPresentation(String memberName, ValueContext valueContext) {
        return members.stream().filter(member -> member.getUsername().equals(memberName)).findFirst().get();
    }

    public void setSelectedUser(String username) {
        Optional<SimpleProjectMember> foundMember = members.stream().filter(member -> member.getUsername().equals(username)).findFirst();
        this.setValue(foundMember.orElse(null));
    }
}
