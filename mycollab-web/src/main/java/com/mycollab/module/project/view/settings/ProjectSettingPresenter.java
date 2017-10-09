package com.mycollab.module.project.view.settings;

import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.ProjectNotificationSetting;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.service.ProjectNotificationSettingService;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ProjectSettingPresenter extends AbstractPresenter<ProjectSettingView> {
    private static final long serialVersionUID = 1L;

    public ProjectSettingPresenter() {
        super(ProjectSettingView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        UserSettingView userSettingView = (UserSettingView) container;
        userSettingView.gotoSubView(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_SETTINGS));

        ProjectNotificationSettingService projectNotificationSettingService = AppContextUtil
                .getSpringBean(ProjectNotificationSettingService.class);
        ProjectNotificationSetting notification = projectNotificationSettingService
                .findNotification(UserUIContext.getUsername(), CurrentProjectVariables.getProjectId(),
                        AppUI.getAccountId());

        ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
        breadCrumb.gotoProjectSetting();
        view.showNotificationSettings(notification);
    }
}
