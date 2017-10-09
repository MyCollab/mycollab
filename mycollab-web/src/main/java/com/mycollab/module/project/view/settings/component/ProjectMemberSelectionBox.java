package com.mycollab.module.project.view.settings.component;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectMemberStatusConstants;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.ComboBox;

import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class ProjectMemberSelectionBox extends ComboBox {
    private static final long serialVersionUID = 1L;

    public ProjectMemberSelectionBox(boolean isNullAllowable) {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        this.setNullSelectionAllowed(isNullAllowable);

        ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        criteria.setStatuses(new SetSearchField<>(ProjectMemberStatusConstants.ACTIVE));
        criteria.addOrderField(new SearchCriteria.OrderField("memberFullName", SearchCriteria.ASC));

        ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
        List<SimpleProjectMember> memberList = (List<SimpleProjectMember>) projectMemberService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
        loadUserList(memberList);
    }

    private void loadUserList(List<SimpleProjectMember> memberList) {
        for (SimpleProjectMember member : memberList) {
            this.addItem(member);
            this.setItemCaption(member, StringUtils.trim(member.getDisplayName(), 25, true));
            this.setItemIcon(member, UserAvatarControlFactory.createAvatarResource(member.getMemberAvatarId(), 16));
        }
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof String) {
            Collection<?> containerPropertyIds = this.getItemIds();
            for (Object id : containerPropertyIds) {
                if (id instanceof SimpleProjectMember) {
                    if (value.equals(((SimpleProjectMember) id).getUsername())) {
                        super.setValue(id);
                    }
                }
            }
        } else {
            super.setValue(value);
        }
    }
}
