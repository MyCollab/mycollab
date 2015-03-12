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
package com.esofthead.mycollab.module.project.view.page;

import com.esofthead.mycollab.module.page.domain.PageResource;
import com.esofthead.mycollab.module.page.service.PageService;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class PageListPresenter extends AbstractPresenter<PageListView> {
    private static final long serialVersionUID = 1L;

    private PageService pageService;

    public PageListPresenter() {
        super(PageListView.class);
        pageService = ApplicationContextUtil.getSpringBean(PageService.class);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables
                .canRead(ProjectRolePermissionCollections.PAGES)) {
            PageContainer pageContainer = (PageContainer) container;
            pageContainer.removeAllComponents();

            String path = (String) data.getParams();
            if (path == null) {
                path = CurrentProjectVariables.getCurrentPagePath();
            } else {
                CurrentProjectVariables.setCurrentPagePath(path);
            }
            List<PageResource> resources = pageService.getResources(path,
                    AppContext.getUsername());
            if (!CollectionUtils.isEmpty(resources)) {
                pageContainer.addComponent(view.getWidget());
                view.displayDefaultPages(resources);
            } else {
                PageListNoItemView alternativeView = ViewManager.getCacheComponent(PageListNoItemView.class);
                pageContainer.addComponent(alternativeView.getWidget());
            }

            ProjectBreadcrumb breadcrumb = ViewManager
                    .getCacheComponent(ProjectBreadcrumb.class);
            breadcrumb.gotoPageList();
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

}
