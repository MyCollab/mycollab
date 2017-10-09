package com.mycollab.module.project.view.milestone;

import com.mycollab.core.SecureAccessException;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.ProjectGenericPresenter;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class MilestoneListPresenter extends ProjectGenericPresenter<MilestoneListView> {
    private static final long serialVersionUID = 1L;

    public MilestoneListPresenter() {
        super(MilestoneListView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.MILESTONES)) {
            MilestoneContainer milestoneContainer = (MilestoneContainer) container;
            milestoneContainer.navigateToContainer(ProjectTypeConstants.MILESTONE);
            milestoneContainer.setContent(view);

            view.lazyLoadView();

            ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            breadcrumb.gotoMilestoneList();
        } else {
            throw new SecureAccessException();
        }
    }
}
