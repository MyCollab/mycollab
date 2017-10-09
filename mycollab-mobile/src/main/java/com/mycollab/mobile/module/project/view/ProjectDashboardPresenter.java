package com.mycollab.mobile.module.project.view;

import com.mycollab.mobile.module.project.view.parameters.ProjectScreenData;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class ProjectDashboardPresenter extends AbstractProjectPresenter<ProjectDashboardView> {
    private static final long serialVersionUID = -2645763046888609751L;

    public ProjectDashboardPresenter() {
        super(ProjectDashboardView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (data instanceof ProjectScreenData.Edit) {
            // TODO: Handle edit project
        } else {
            if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.PROJECT)) {
                super.onGo(container, data);
                getView().displayDashboard();
            } else {
                NotificationUtil.showMessagePermissionAlert();
            }
        }
    }
}
