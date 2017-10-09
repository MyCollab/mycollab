package com.mycollab.mobile.module.project.view.settings;

import com.mycollab.common.GenericLinkUtils;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.module.project.event.ProjectMemberEvent;
import com.mycollab.mobile.module.project.ui.AbstractListPageView;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
@ViewComponent
public class ProjectMemberListViewImpl extends AbstractListPageView<ProjectMemberSearchCriteria, SimpleProjectMember>
        implements ProjectMemberListView {
    private static final long serialVersionUID = 3008732621100597514L;

    public ProjectMemberListViewImpl() {
        this.setCaption(UserUIContext.getMessage(ProjectMemberI18nEnum.LIST));
    }

    @Override
    protected AbstractPagedBeanList<ProjectMemberSearchCriteria, SimpleProjectMember> createBeanList() {
        return new ProjectMemberListDisplay();
    }

    @Override
    protected SearchInputField<ProjectMemberSearchCriteria> createSearchField() {
        return null;
    }

    @Override
    protected Component buildRightComponent() {
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.USERS)) {
            return new MButton("", clickEvent -> EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoInviteMembers(this, null)))
                    .withIcon(FontAwesome.PLUS).withStyleName(UIConstants.CIRCLE_BOX);
        }
        return null;
    }

    @Override
    public void onBecomingVisible() {
        super.onBecomingVisible();
        AppUI.addFragment("project/user/list/" + GenericLinkUtils.encodeParam(CurrentProjectVariables.getProjectId()),
                UserUIContext.getMessage(ProjectMemberI18nEnum.LIST));
    }
}
