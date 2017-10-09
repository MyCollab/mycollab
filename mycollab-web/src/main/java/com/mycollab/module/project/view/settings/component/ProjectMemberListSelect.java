package com.mycollab.module.project.view.settings.component;

import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectMemberStatusConstants;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.ListSelect;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class ProjectMemberListSelect extends ListSelect {
    private static final long serialVersionUID = 1L;

    public ProjectMemberListSelect() {
        this(true);
    }

    public ProjectMemberListSelect(boolean listActiveMembersOnly) {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        this.setNullSelectionAllowed(false);
        this.setMultiSelect(true);

        ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));

        if (listActiveMembersOnly) {
            criteria.setStatuses(new SetSearchField<>(ProjectMemberStatusConstants.ACTIVE));
        }

        ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
        List<SimpleProjectMember> memberList = (List<SimpleProjectMember>) projectMemberService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
        for (SimpleProjectMember member : memberList) {
            this.addItem(member.getUsername());
            this.setItemCaption(member.getUsername(), member.getDisplayName());
            this.setItemIcon(member.getUsername(), UserAvatarControlFactory.createAvatarResource(member.getMemberAvatarId(), 16));
        }
        this.setRows(4);
    }
}
