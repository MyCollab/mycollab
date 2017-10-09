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
