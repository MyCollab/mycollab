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

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.event.BugVersionEvent;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.module.tracker.service.VersionService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.IEditFormHandler;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class VersionAddPresenter extends AbstractPresenter<VersionAddView> {
    private static final long serialVersionUID = 1L;

    public VersionAddPresenter() {
        super(VersionAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<Version>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(Version item) {
                save(item);
                EventBusFactory.getInstance().post(new BugVersionEvent.GotoList(this, null));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new BugVersionEvent.GotoList(this, null));
            }

            @Override
            public void onSaveAndNew(final Version item) {
                save(item);
                EventBusFactory.getInstance().post(new BugVersionEvent.GotoAdd(this, null));
            }
        });
    }

    private void save(Version item) {
        VersionService versionService = AppContextUtil.getSpringBean(VersionService.class);
        item.setSaccountid(AppUI.getAccountId());
        item.setProjectid(CurrentProjectVariables.getProjectId());
        item.setStatus(StatusI18nEnum.Open.name());
        if (item.getId() == null) {
            versionService.saveWithSession(item, UserUIContext.getUsername());
        } else {
            versionService.updateWithSession(item, UserUIContext.getUsername());
        }
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.VERSIONS)) {
            VersionContainer versionContainer = (VersionContainer) container;
            versionContainer.addComponent(view);
            Version version = (Version) data.getParams();
            view.editItem(version);

            ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            if (version.getId() == null) {
                breadcrumb.gotoVersionAdd();
            } else {
                breadcrumb.gotoVersionEdit(version);
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
