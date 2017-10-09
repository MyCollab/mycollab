package com.mycollab.module.project.view.settings;

import com.mycollab.core.MyCollabException;
import com.mycollab.module.project.view.parameters.ProjectRoleScreenData;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectRolePresenter extends AbstractPresenter<ProjectRoleContainer> {
    private static final long serialVersionUID = 1L;

    public ProjectRolePresenter() {
        super(ProjectRoleContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        AbstractPresenter<?> presenter;

        if (data instanceof ProjectRoleScreenData.Search) {
            presenter = PresenterResolver.getPresenter(ProjectRoleListPresenter.class);
        } else if (data instanceof ProjectRoleScreenData.Add) {
            presenter = PresenterResolver.getPresenter(ProjectRoleAddPresenter.class);
        } else if (data instanceof ProjectRoleScreenData.Read) {
            presenter = PresenterResolver.getPresenter(ProjectRoleReadPresenter.class);
        } else {
            throw new MyCollabException("Can not handle data " + data);
        }

        presenter.go(view, data);
    }
}
