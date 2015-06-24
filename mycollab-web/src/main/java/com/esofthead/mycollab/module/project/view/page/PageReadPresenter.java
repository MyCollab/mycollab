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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.page.domain.Page;
import com.esofthead.mycollab.module.page.service.PageService;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.PageEvent;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.LoadPolicy;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import org.vaadin.dialogs.ConfirmDialog;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class PageReadPresenter extends AbstractPresenter<PageReadView> {
    private static final long serialVersionUID = 1L;

    public PageReadPresenter() {
        super(PageReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(
                new DefaultPreviewFormHandler<Page>() {
                    @Override
                    public void onEdit(Page data) {
                        EventBusFactory.getInstance().post(new PageEvent.GotoEdit(this, data));
                    }

                    @Override
                    public void onAdd(Page data) {
                        EventBusFactory.getInstance().post(new PageEvent.GotoAdd(this, null));
                    }

                    @Override
                    public void onDelete(final Page data) {
                        ConfirmDialogExt.show(UI.getCurrent(),
                                AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                                AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                                AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                                AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                                new ConfirmDialog.Listener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void onClose(ConfirmDialog dialog) {
                                        if (dialog.isConfirmed()) {
                                            PageService pageService = ApplicationContextUtil.getSpringBean(PageService.class);
                                            pageService.removeResource(data.getPath());
                                            EventBusFactory.getInstance().post(new PageEvent.GotoList(this, null));
                                        }
                                    }
                                });
                    }
                });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.PAGES)) {
            PageContainer pageContainer = (PageContainer) container;
            pageContainer.navigateToContainer(ProjectTypeConstants.PAGE);
            pageContainer.removeAllComponents();
            pageContainer.addComponent(view.getWidget());

            Page page = (Page) data.getParams();
            view.previewItem(page);

            ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            breadcrumb.gotoPageRead(page);
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }

    }

}
