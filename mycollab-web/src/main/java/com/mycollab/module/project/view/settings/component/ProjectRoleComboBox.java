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

import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.ProjectRole;
import com.mycollab.module.project.domain.SimpleProjectRole;
import com.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.mycollab.module.project.service.ProjectRoleService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ItemCaptionGenerator;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectRoleComboBox extends ComboBox<SimpleProjectRole> implements Converter<SimpleProjectRole, Integer> {
    private static final long serialVersionUID = 1L;

    private List<SimpleProjectRole> roles;

    public ProjectRoleComboBox() {
        ProjectRoleSearchCriteria criteria = new ProjectRoleSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));

        ProjectRoleService roleService = AppContextUtil.getSpringBean(ProjectRoleService.class);
        roles = (List<SimpleProjectRole>) roleService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));

        SimpleProjectRole ownerRole = new SimpleProjectRole();
        ownerRole.setId(-1);
        ownerRole.setRolename(UserUIContext.getMessage(ProjectRoleI18nEnum.OPT_ADMIN_ROLE_DISPLAY));

        roles.add(ownerRole);
        this.setItems(roles);

        this.setEmptySelectionAllowed(false);
        this.setItemCaptionGenerator((ItemCaptionGenerator<SimpleProjectRole>) ProjectRole::getRolename);
    }

    @Override
    public Result<Integer> convertToModel(SimpleProjectRole simpleProjectRole, ValueContext valueContext) {
        return Result.ok(simpleProjectRole.getId());
    }

    @Override
    public SimpleProjectRole convertToPresentation(Integer roleId, ValueContext valueContext) {
        return roles.stream().filter(role -> role.getId() == roleId).findFirst().get();
    }
}
