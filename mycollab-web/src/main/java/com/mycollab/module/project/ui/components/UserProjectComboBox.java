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
package com.mycollab.module.project.ui.components;

import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.vaadin.ui.ComboBox;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
public class UserProjectComboBox extends ComboBox {
    public UserProjectComboBox(String username) {
        setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
        List<SimpleProject> projects = projectService.getProjectsUserInvolved(username, AppUI.getAccountId());
        projects.forEach(project -> {
            addItem(project.getId());
            setItemCaption(project.getId(), project.getName());
        });
    }
}
