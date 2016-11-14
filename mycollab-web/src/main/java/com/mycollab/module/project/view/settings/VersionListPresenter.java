/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mycollab.module.project.view.settings;

import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.i18n.VersionI18nEnum;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.ProjectGenericListPresenter;
import com.mycollab.module.tracker.domain.SimpleVersion;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.mycollab.module.tracker.service.VersionService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.ViewItemAction;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.DefaultMassEditActionHandler;
import com.mycollab.vaadin.web.ui.MailFormWindow;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class VersionListPresenter extends ProjectGenericListPresenter<VersionListView, VersionSearchCriteria, SimpleVersion> {
    private static final long serialVersionUID = 1L;
    private final VersionService versionService;

    public VersionListPresenter() {
        super(VersionListView.class);
        versionService = AppContextUtil.getSpringBean(VersionService.class);
    }

    @Override
    protected void postInitView() {
        super.postInitView();

        view.getPopupActionHandlers().setMassActionHandler(new DefaultMassEditActionHandler(this) {
            @Override
            protected void onSelectExtra(String id) {
                if (ViewItemAction.MAIL_ACTION().equals(id)) {
                    UI.getCurrent().addWindow(new MailFormWindow());
                }
            }

            @Override
            protected String getReportTitle() {
                return UserUIContext.getMessage(VersionI18nEnum.LIST);
            }

            @Override
            protected Class<?> getReportModelClassType() {
                return SimpleVersion.class;
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.VERSIONS)) {
            VersionContainer versionContainer = (VersionContainer) container;
            versionContainer.removeAllComponents();
            versionContainer.addComponent(view);

            searchCriteria = (VersionSearchCriteria) data.getParams();
            int totalCount = versionService.getTotalCount(searchCriteria);

            if (totalCount > 0) {
                doSearch(searchCriteria);
            } else {
                view.showNoItemView();
            }

            ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            breadcrumb.gotoVersionList();
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    @Override
    protected void deleteSelectedItems() {
        if (!isSelectAll) {
            Collection<SimpleVersion> currentDataList = view.getPagedBeanTable().getCurrentDataList();
            List<Version> keyList = new ArrayList<>();
            for (Version item : currentDataList) {
                if (item.isSelected()) {
                    keyList.add(item);
                }
            }

            if (keyList.size() > 0) {
                versionService.massRemoveWithSession(keyList, UserUIContext.getUsername(), MyCollabUI.getAccountId());
            }
        } else {
            versionService.removeByCriteria(searchCriteria, MyCollabUI.getAccountId());
        }

        int totalCount = versionService.getTotalCount(searchCriteria);

        if (totalCount > 0) {
            doSearch(searchCriteria);
        } else {
            view.showNoItemView();
        }

    }

    @Override
    public ISearchableService<VersionSearchCriteria> getSearchService() {
        return versionService;
    }
}
