package com.mycollab.mobile.module.project.view.settings;

import com.mycollab.mobile.module.project.view.AbstractProjectPresenter;
import com.mycollab.mobile.module.project.view.parameters.ProjectMemberScreenData;
import com.mycollab.mobile.mvp.view.PresenterOptionUtil;
import com.mycollab.vaadin.mvp.IPresenter;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class ProjectUserPresenter extends AbstractProjectPresenter<ProjectUserContainer> {
    private static final long serialVersionUID = 1L;

    public ProjectUserPresenter() {
        super(ProjectUserContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        IPresenter<?> presenter;

        if (data instanceof ProjectMemberScreenData.InviteProjectMembers) {
            presenter = PresenterOptionUtil.getPresenter(IProjectMemberInvitePresenter.class);
        } else if (data instanceof ProjectMemberScreenData.Read) {
            presenter = PresenterResolver.getPresenter(ProjectMemberReadPresenter.class);
        } else if (data instanceof ProjectMemberScreenData.Edit) {
            presenter = PresenterOptionUtil.getPresenter(IProjectMemberEditPresenter.class);
        } else {
            presenter = PresenterResolver.getPresenter(ProjectMemberListPresenter.class);
        }

        presenter.go(container, data);
    }
}
