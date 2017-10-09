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
