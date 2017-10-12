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

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectMemberStatusConstants;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.parameters.ProjectRoleScreenData;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.TabSheetDecorator;
import com.mycollab.module.project.i18n.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class UserSettingViewImpl extends AbstractVerticalPageView implements UserSettingView {
    private static final long serialVersionUID = 1L;

    private ProjectUserPresenter userPresenter;
    private ProjectRolePresenter rolePresenter;
    private ProjectSettingPresenter settingPresenter;
    private ComponentPresenter componentPresenter;
    private VersionPresenter versionPresenter;

    private final TabSheetDecorator myProjectTab;

    public UserSettingViewImpl() {
        this.myProjectTab = new TabSheetDecorator();
        this.addComponent(myProjectTab);
        this.buildComponents();
    }

    private void buildComponents() {
        userPresenter = PresenterResolver.getPresenter(ProjectUserPresenter.class);
        myProjectTab.addTab(userPresenter.getView(), UserUIContext.getMessage(ProjectMemberI18nEnum.LIST), FontAwesome.USERS);

        rolePresenter = PresenterResolver.getPresenter(ProjectRolePresenter.class);
        myProjectTab.addTab(rolePresenter.getView(), UserUIContext.getMessage(ProjectRoleI18nEnum.LIST), FontAwesome.USER_MD);

        componentPresenter = PresenterResolver.getPresenter(ComponentPresenter.class);
        myProjectTab.addTab(componentPresenter.getView(), UserUIContext.getMessage(ComponentI18nEnum.LIST),
                ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG_COMPONENT));

        versionPresenter = PresenterResolver.getPresenter(VersionPresenter.class);
        myProjectTab.addTab(versionPresenter.getView(), UserUIContext.getMessage(VersionI18nEnum.LIST),
                ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG_VERSION));

        settingPresenter = PresenterResolver.getPresenter(ProjectSettingPresenter.class);
        myProjectTab.addTab(settingPresenter.getView(), UserUIContext.getMessage(ProjectCommonI18nEnum
                .VIEW_SETTINGS), FontAwesome.COG);

        myProjectTab.addTab(settingPresenter.getView(), UserUIContext.getMessage(ProjectCommonI18nEnum
                .VIEW_SETTINGS), FontAwesome.COG);

        myProjectTab.addTab(settingPresenter.getView(), UserUIContext.getMessage(ProjectCommonI18nEnum
                .VIEW_SETTINGS), FontAwesome.COG);

        myProjectTab.addTab(settingPresenter.getView(), UserUIContext.getMessage(ProjectCommonI18nEnum
                .VIEW_SETTINGS), FontAwesome.COG);

        myProjectTab.addSelectedTabChangeListener(new SelectedTabChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectedTabChange(SelectedTabChangeEvent event) {
                Tab tab = ((TabSheetDecorator) event.getTabSheet()).getSelectedTabInfo();
                String caption = tab.getCaption();
                SimpleProject project = CurrentProjectVariables.getProject();

                if (UserUIContext.getMessage(ProjectMemberI18nEnum.LIST).equals(caption)) {
                    ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
                    criteria.setProjectId(new NumberSearchField(project.getId()));
                    criteria.setStatuses(new SetSearchField<>(ProjectMemberStatusConstants.ACTIVE,
                            ProjectMemberStatusConstants.NOT_ACCESS_YET));
                    userPresenter.go(UserSettingViewImpl.this, new ScreenData.Search<>(criteria));
                } else if (UserUIContext.getMessage(ProjectRoleI18nEnum.LIST).equals(caption)) {
                    ProjectRoleSearchCriteria criteria = new ProjectRoleSearchCriteria();
                    criteria.setProjectId(new NumberSearchField(project.getId()));
                    rolePresenter.go(UserSettingViewImpl.this,
                            new ProjectRoleScreenData.Search(criteria));
                } else if (UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_SETTINGS).equals(caption)) {
                    settingPresenter.go(UserSettingViewImpl.this, null);
                } else if (UserUIContext.getMessage(ComponentI18nEnum.LIST).equals(caption)) {
                    componentPresenter.go(UserSettingViewImpl.this, null);
                } else if (UserUIContext.getMessage(VersionI18nEnum.LIST).equals(caption)) {
                    versionPresenter.go(UserSettingViewImpl.this, null);
                }
            }
        });

    }

    @Override
    public Component gotoSubView(final String name) {
        return myProjectTab.selectTab(name).getComponent();
    }
}
