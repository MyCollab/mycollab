package com.mycollab.mobile.module.project.view.settings;

import com.mycollab.mobile.module.project.view.ProjectListPresenter;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class ProjectMemberListPresenter extends ProjectListPresenter<ProjectMemberListView, ProjectMemberSearchCriteria, SimpleProjectMember> {
    private static final long serialVersionUID = 1L;

    public ProjectMemberListPresenter() {
        super(ProjectMemberListView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.USERS)) {
            super.onGo(container, data);
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
