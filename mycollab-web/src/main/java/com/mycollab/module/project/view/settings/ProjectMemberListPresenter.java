package com.mycollab.module.project.view.settings;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectMemberStatusConstants;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectMemberListPresenter extends AbstractPresenter<ProjectMemberListView> {
    private static final long serialVersionUID = 1L;

    public ProjectMemberListPresenter() {
        super(ProjectMemberListView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.USERS)) {
            ProjectUserContainer userGroupContainer = (ProjectUserContainer) container;
            userGroupContainer.setContent(view);

            ProjectMemberSearchCriteria criteria;
            if (data.getParams() == null) {
                criteria = new ProjectMemberSearchCriteria();
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                criteria.setStatuses(new SetSearchField<String>(ProjectMemberStatusConstants.ACTIVE, ProjectMemberStatusConstants.NOT_ACCESS_YET));
                criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
            } else {
                criteria = (ProjectMemberSearchCriteria) data.getParams();
            }
            view.setSearchCriteria(criteria);

            ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            breadCrumb.gotoUserList();
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
