/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view.settings;

import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.vaadin.ui.ListSelect;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class ProjectMemberListSelect extends ListSelect {

    public ProjectMemberListSelect() {
        super();
        this.setImmediate(true);
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
        this.setRows(1);
        ProjectMemberSearchCriteria searchCriteria = new ProjectMemberSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));

        ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
        List<SimpleProjectMember> projectMembers = (List<SimpleProjectMember>) projectMemberService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));
        for (SimpleProjectMember projectMember : projectMembers) {
            addItem(projectMember.getUsername());
            setItemCaption(projectMember.getUsername(), projectMember.getDisplayName());
        }
    }
}
