/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
