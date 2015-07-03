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

package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.i18n.VersionI18nEnum;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.project.view.ProjectGenericListPresenter;
import com.esofthead.mycollab.module.tracker.domain.SimpleVersion;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.VersionService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.desktop.ui.DefaultMassEditActionHandler;
import com.esofthead.mycollab.vaadin.events.ViewItemAction;
import com.esofthead.mycollab.vaadin.mvp.LoadPolicy;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.MailFormWindow;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
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
        super(VersionListView.class, VersionListNoItemView.class);
        versionService = ApplicationContextUtil.getSpringBean(VersionService.class);
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
                return AppContext.getMessage(VersionI18nEnum.VIEW_LIST_TITLE);
            }

            @Override
            protected Class<?> getReportModelClassType() {
                return SimpleVersion.class;
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.VERSIONS)) {
            VersionContainer versionContainer = (VersionContainer) container;
            versionContainer.removeAllComponents();
            versionContainer.addComponent(view.getWidget());

            searchCriteria = (VersionSearchCriteria) data.getParams();
            int totalCount = versionService.getTotalCount(searchCriteria);

            if (totalCount > 0) {
                displayListView(container, data);
                doSearch(searchCriteria);
            } else {
                displayNoExistItems(container, data);
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
                versionService.massRemoveWithSession(keyList, AppContext.getUsername(), AppContext.getAccountId());
            }
        } else {
            versionService.removeByCriteria(searchCriteria, AppContext.getAccountId());
        }

        int totalCount = versionService.getTotalCount(searchCriteria);

        if (totalCount > 0) {
            displayListView((ComponentContainer) view.getParent(), null);
            doSearch(searchCriteria);
        } else {
            displayNoExistItems((ComponentContainer) view.getParent(), null);
        }

    }

    @Override
    public ISearchableService<VersionSearchCriteria> getSearchService() {
        return versionService;
    }
}
