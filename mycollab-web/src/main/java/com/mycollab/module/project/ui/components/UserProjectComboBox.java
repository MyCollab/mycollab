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
package com.mycollab.module.project.ui.components;

import com.mycollab.core.SecureAccessException;
import com.mycollab.module.project.domain.Project;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ItemCaptionGenerator;

import java.util.List;
import java.util.Optional;

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
public class UserProjectComboBox extends ComboBox<SimpleProject> {
    private List<SimpleProject> projects;

    public UserProjectComboBox() {
        ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
        if (UserUIContext.isAdmin()) {
            projects = projectService.getProjectsUserInvolved(null, AppUI.getAccountId());
        } else {
            projects = projectService.getProjectsUserInvolved(UserUIContext.getUsername(), AppUI.getAccountId());
        }

        setItems(projects);
        setItemCaptionGenerator((ItemCaptionGenerator<SimpleProject>) Project::getName);
        this.setWidth(WebThemes.FORM_CONTROL_WIDTH);
    }

    public SimpleProject setSelectedProjectById(Integer projectId) {
        if (projects.size() == 0) {
            throw new SecureAccessException();
        }

        SimpleProject result;
        if (projectId == null) {
            result = projects.get(0);
        } else {
            Optional<SimpleProject> canPro = projects.stream().filter(project -> project.getId() == projectId).findFirst();
            result = canPro.orElse(projects.get(0));
        }
        setValue(result);
        return result;
    }
}
