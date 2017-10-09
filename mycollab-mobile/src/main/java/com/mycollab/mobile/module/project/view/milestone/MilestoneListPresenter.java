package com.mycollab.mobile.module.project.view.milestone;

import com.mycollab.mobile.module.project.view.ProjectListPresenter;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class MilestoneListPresenter extends ProjectListPresenter<MilestoneListView, MilestoneSearchCriteria, SimpleMilestone> {
    private static final long serialVersionUID = 8282868336211950427L;

    public MilestoneListPresenter() {
        super(MilestoneListView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.MILESTONES)) {
            super.onGo(container, data);
            getView().displayStatus(MilestoneStatus.InProgress);
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
