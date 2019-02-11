package com.mycollab.module.project.ui.components;

import com.mycollab.module.project.domain.Project;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.ListSelect;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
public class UserProjectListSelect extends ListSelect<SimpleProject> implements Converter<Collection<SimpleProject>, Collection<Integer>> {
    private List<SimpleProject> projects;

    public UserProjectListSelect() {
        setRows(4);
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

    @Override
    public Result<Collection<Integer>> convertToModel(Collection<SimpleProject> value, ValueContext context) {
        if (value == null) {
            return Result.ok(null);
        } else {
            return Result.ok(value.stream().map(project -> project.getId()).collect(Collectors.toSet()));
        }
    }

    @Override
    public Collection<SimpleProject> convertToPresentation(Collection<Integer> value, ValueContext context) {
        return null;
    }
}
