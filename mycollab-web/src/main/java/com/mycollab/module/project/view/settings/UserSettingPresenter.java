package com.mycollab.module.project.view.settings;

import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.ClassUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.*;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.module.project.view.parameters.*;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class UserSettingPresenter extends AbstractPresenter<UserSettingView> {
    private static final long serialVersionUID = 1L;

    public UserSettingPresenter() {
        super(UserSettingView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ProjectView projectViewContainer = (ProjectView) container;
        projectViewContainer.gotoSubView(ProjectTypeConstants.MEMBER);

        AbstractPresenter<?> presenter;
        if (ClassUtils.instanceOf(data, ProjectRoleScreenData.Search.class, ProjectRoleScreenData.Add.class,
                ProjectRoleScreenData.Read.class)) {
            view.gotoSubView(UserUIContext.getMessage(ProjectRoleI18nEnum.LIST));
            presenter = PresenterResolver.getPresenter(ProjectRolePresenter.class);
        } else if (ClassUtils.instanceOf(data, ProjectMemberScreenData.Read.class,
                ProjectMemberScreenData.Search.class, ProjectMemberScreenData.Add.class,
                ProjectMemberScreenData.InviteProjectMembers.class)) {
            view.gotoSubView(UserUIContext.getMessage(ProjectMemberI18nEnum.LIST));
            presenter = PresenterResolver.getPresenter(ProjectUserPresenter.class);
        } else if (ClassUtils.instanceOf(data, ProjectSettingScreenData.ViewSettings.class)) {
            view.gotoSubView(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_SETTINGS));
            presenter = PresenterResolver.getPresenter(ProjectSettingPresenter.class);
        } else if (ClassUtils.instanceOf(data, ComponentScreenData.Add.class, ComponentScreenData.Edit.class,
                ComponentScreenData.Read.class, ComponentScreenData.Search.class)) {
            view.gotoSubView(UserUIContext.getMessage(ComponentI18nEnum.LIST));
            presenter = PresenterResolver.getPresenter(ComponentPresenter.class);
        } else if (ClassUtils.instanceOf(data, VersionScreenData.Add.class, VersionScreenData.Edit.class,
                VersionScreenData.Read.class, VersionScreenData.Search.class)) {
            view.gotoSubView(UserUIContext.getMessage(VersionI18nEnum.LIST));
            presenter = PresenterResolver.getPresenter(VersionPresenter.class);
        } else {
            throw new MyCollabException("No support screen data: " + data);
        }

        presenter.go(view, data);
    }
}
