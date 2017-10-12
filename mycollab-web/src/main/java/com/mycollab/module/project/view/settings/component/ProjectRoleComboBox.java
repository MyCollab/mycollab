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
package com.mycollab.module.project.view.settings.component;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.SimpleProjectRole;
import com.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.mycollab.module.project.service.ProjectRoleService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.ComboBox;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectRoleComboBox extends ComboBox {
    private static final long serialVersionUID = 1L;

    public ProjectRoleComboBox() {
        super();
        this.setImmediate(true);
        this.setItemCaptionMode(ItemCaptionMode.PROPERTY);

        ProjectRoleSearchCriteria criteria = new ProjectRoleSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));

        ProjectRoleService roleService = AppContextUtil.getSpringBean(ProjectRoleService.class);
        List<SimpleProjectRole> roleList = (List<SimpleProjectRole>) roleService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));

        BeanContainer<String, SimpleProjectRole> beanItem = new BeanContainer<>(SimpleProjectRole.class);
        beanItem.setBeanIdProperty("id");

        SimpleProjectRole ownerRole = new SimpleProjectRole();
        ownerRole.setId(-1);
        ownerRole.setRolename(UserUIContext.getMessage(ProjectRoleI18nEnum.OPT_ADMIN_ROLE_DISPLAY));
        beanItem.addBean(ownerRole);


        for (SimpleProjectRole role : roleList) {
            beanItem.addBean(role);
        }

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
