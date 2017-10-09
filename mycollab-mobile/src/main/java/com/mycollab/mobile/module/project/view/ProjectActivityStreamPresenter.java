package com.mycollab.mobile.module.project.view;

import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.ProjectActivityStream;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class ProjectActivityStreamPresenter extends ProjectListPresenter<ProjectActivitiesView, ActivityStreamSearchCriteria, ProjectActivityStream> {
    private static final long serialVersionUID = -2089284900326846089L;

    public ProjectActivityStreamPresenter() {
        super(ProjectActivitiesView.class);
    }

    @Override
    protected void onGo(HasComponents navigator, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.PROJECT)) {
            super.onGo(navigator, data);
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

}
