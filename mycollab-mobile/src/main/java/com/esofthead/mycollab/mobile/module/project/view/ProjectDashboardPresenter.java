package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectGenericPresenter;
import com.esofthead.mycollab.mobile.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.mobile.mvp.AbstractPresenter;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class ProjectDashboardPresenter extends ProjectGenericPresenter<ProjectDashboardView> {

    public ProjectDashboardPresenter() {
        super(ProjectDashboardView.class);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (data instanceof ProjectScreenData.Edit) {
            // TODO: Handle edit project
        } else {
            if (CurrentProjectVariables
                    .canRead(ProjectRolePermissionCollections.PROJECT)) {
                super.onGo(container, data);
                view.displayDashboard();
            } else {
                NotificationUtil.showMessagePermissionAlert();
            }
        }
    }
}
