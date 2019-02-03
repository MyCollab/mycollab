package com.mycollab.module.project.ui.components;

import com.mycollab.module.project.domain.Project;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.ListSelect;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
public class UserProjectListSelect extends ListSelect<SimpleProject> {
    private List<SimpleProject> projects;

    public UserProjectListSelect() {
        setRows(4);
        ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
        projects = projectService.getProjectsUserInvolved(UserUIContext.getUsername(), AppUI.getAccountId());
        setItems(projects);
        setItemCaptionGenerator((ItemCaptionGenerator<SimpleProject>) Project::getName);
        this.setWidth(WebThemes.FORM_CONTROL_WIDTH);
    }
}
