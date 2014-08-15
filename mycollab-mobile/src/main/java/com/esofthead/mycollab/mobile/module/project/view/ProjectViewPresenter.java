package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.mvp.AbstractPresenter;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class ProjectViewPresenter extends AbstractPresenter<ProjectView> {

    public ProjectViewPresenter() {
        super(ProjectView.class);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (data == null) {
            // do nothing
        }
        if (data.getParams() instanceof Integer) {
            ProjectService projectService = (ProjectService) ApplicationContextUtil
                    .getSpringBean(ProjectService.class);
            SimpleProject project = (SimpleProject) projectService.findById(
                    (Integer) data.getParams(), AppContext.getAccountId());

            if (project == null) {
                NotificationUtil.showRecordNotExistNotification();
            } else {
                CurrentProjectVariables.setProject(project);
            }
        }
    }

    @Override
    protected void onDefaultStopChain() {
        ProjectDashboardPresenter presenter = PresenterResolver.getPresenter(ProjectDashboardPresenter.class);
        presenter.go( (MobileNavigationManager) UI.getCurrent().getContent(), null);
    }

    @Override
    protected void onHandleChain(ComponentContainer container, PageActionChain pageActionChain) {
        // TODO: handle Project's submodules here
    }
}
