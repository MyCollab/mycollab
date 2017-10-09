package com.mycollab.module.project.view.settings;

import com.mycollab.module.project.view.parameters.ProjectMemberScreenData;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectUserPresenter extends AbstractPresenter<ProjectUserContainer> {
    private static final long serialVersionUID = 1L;

    public ProjectUserPresenter() {
        super(ProjectUserContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        AbstractPresenter<?> presenter;

        if (data instanceof ProjectMemberScreenData.Add) {
            presenter = PresenterResolver.getPresenter(ProjectMemberEditPresenter.class);
        } else if (data instanceof ProjectMemberScreenData.InviteProjectMembers) {
            presenter = PresenterResolver.getPresenter(ProjectMemberInvitePresenter.class);
        } else if (data instanceof ProjectMemberScreenData.InviteProjectMembers) {
            presenter = PresenterResolver.getPresenter(ProjectMemberInvitePresenter.class);
        } else if (data instanceof ProjectMemberScreenData.Read) {
            presenter = PresenterResolver.getPresenter(ProjectMemberReadPresenter.class);
        } else {
            presenter = PresenterResolver.getPresenter(ProjectMemberListPresenter.class);
        }

        presenter.go(view, data);
    }
}
