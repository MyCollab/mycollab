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
package com.mycollab.module.project.view.page;

import com.mycollab.core.SecureAccessException;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.page.domain.Page;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.PageEvent;
import com.mycollab.module.project.service.ProjectPageService;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.ProjectGenericPresenter;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.IEditFormHandler;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class PageAddPresenter extends ProjectGenericPresenter<PageAddView> {
    private static final long serialVersionUID = 1L;

    public PageAddPresenter() {
        super(PageAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<Page>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(Page page) {
                savePage(page);
                EventBusFactory.getInstance().post(new PageEvent.GotoRead(this, page));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new PageEvent.GotoList(this, null));
            }

            @Override
            public void onSaveAndNew(Page page) {
                savePage(page);
                EventBusFactory.getInstance().post(new PageEvent.GotoAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PAGES)) {
            PageContainer pageContainer = (PageContainer) container;
            pageContainer.navigateToContainer(ProjectTypeConstants.PAGE);
            pageContainer.setContent(view);

            Page page = (Page) data.getParams();
            view.editItem(page);

            ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            if (page.getPath().equals("")) {
                breadcrumb.gotoPageAdd();
            } else {
                breadcrumb.gotoPageEdit(page);
            }

        } else {
            throw new SecureAccessException();
        }
    }

    private void savePage(Page page) {
        ProjectPageService pageService = AppContextUtil.getSpringBean(ProjectPageService.class);

        pageService.savePage(page, UserUIContext.getUsername(), CurrentProjectVariables.getProjectId(),
                AppUI.getAccountId());
    }
}
