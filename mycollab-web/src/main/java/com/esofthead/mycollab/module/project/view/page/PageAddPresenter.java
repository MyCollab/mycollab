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

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.page.domain.Page;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.PageEvent;
import com.esofthead.mycollab.module.project.service.ProjectPageService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.project.view.ProjectViewPresenter;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.*;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class PageAddPresenter extends AbstractPresenter<PageAddView> {
    private static final long serialVersionUID = 1L;

    public PageAddPresenter() {
        super(PageAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new EditFormHandler<Page>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final Page page) {
                savePage(page);
                EventBusFactory.getInstance().post(
                        new PageEvent.GotoRead(this, page));
            }

            @Override
            public void onCancel() {
                ViewState viewState = HistoryViewManager.back();
                if (viewState.hasPresenters(NullViewState.EmptyPresenter.class, ProjectViewPresenter.class)) {
                    EventBusFactory.getInstance().post(
                            new PageEvent.GotoList(this, null));
                }
            }

            @Override
            public void onSaveAndNew(final Page page) {
                savePage(page);
                EventBusFactory.getInstance().post(
                        new PageEvent.GotoAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.PAGES)) {
            PageContainer pageContainer = (PageContainer) container;
            pageContainer.navigateToContainer(ProjectTypeConstants.PAGE);
            pageContainer.removeAllComponents();
            pageContainer.addComponent(view.getWidget());

            Page page = (Page) data.getParams();
            view.editItem(page);

            ProjectBreadcrumb breadcrumb = ViewManager
                    .getCacheComponent(ProjectBreadcrumb.class);
            if (page.getPath().equals("")) {
                breadcrumb.gotoPageAdd();
            } else {
                breadcrumb.gotoPageEdit(page);
            }

        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    private void savePage(Page page) {
        ProjectPageService pageService = ApplicationContextUtil
                .getSpringBean(ProjectPageService.class);

        pageService.savePage(page, AppContext.getUsername(),
                CurrentProjectVariables.getProjectId(),
                AppContext.getAccountId());
    }
}
