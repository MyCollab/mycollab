package com.mycollab.module.project.view.milestone;

import com.mycollab.core.SecureAccessException;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.ProjectGenericPresenter;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
public class MilestoneRoadmapPresenter extends ProjectGenericPresenter<MilestoneRoadmapView> {
    public MilestoneRoadmapPresenter() {
        super(MilestoneRoadmapView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.MILESTONES)) {
            MilestoneContainer milestoneContainer = (MilestoneContainer) container;
            milestoneContainer.navigateToContainer(ProjectTypeConstants.MILESTONE);
            milestoneContainer.setContent(view);

            view.lazyLoadView();
            ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            breadcrumb.gotoRoadmap();
        } else {
            throw new SecureAccessException();
        }
    }
}
