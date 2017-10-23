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
package com.mycollab.module.project.view.bug;

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.QueryI18nEnum.NumberI18nEnum;
import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.core.SecureAccessException;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.module.project.event.UpdateNotificationItemReadStatusEvent;
import com.mycollab.spring.AppEventBus;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.BugEvent;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.ProjectGenericPresenter;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.vaadin.reporting.FormReportLayout;
import com.mycollab.vaadin.reporting.PrintButton;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class BugReadPresenter extends ProjectGenericPresenter<BugReadView> {
    private static final long serialVersionUID = 1L;

    public BugReadPresenter() {
        super(BugReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleBug>() {
            @Override
            public void onEdit(SimpleBug data) {
                EventBusFactory.getInstance().post(new BugEvent.GotoEdit(this, data));
            }

            @Override
            public void onAdd(SimpleBug data) {
                EventBusFactory.getInstance().post(new BugEvent.GotoAdd(this, null));
            }

            @Override
            public void onDelete(final SimpleBug data) {
                ConfirmDialogExt.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.ACTION_YES),
                        UserUIContext.getMessage(GenericI18Enum.ACTION_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                                bugService.removeWithSession(data, UserUIContext.getUsername(), AppUI.getAccountId());
                                EventBusFactory.getInstance().post(new TicketEvent.GotoDashboard(this, null));
                            }
                        });
            }

            @Override
            public void onPrint(Object source, SimpleBug data) {
                PrintButton btn = (PrintButton) source;
                btn.doPrint(data, new FormReportLayout(ProjectTypeConstants.BUG, BugWithBLOBs.Field.name.name(),
                        BugDefaultFormLayoutFactory.getForm(), SimpleBug.Field.components.name(), SimpleBug.Field
                        .affectedVersions.name(), SimpleBug.Field.fixedVersions.name(), BugWithBLOBs.Field.id.name(),
                        SimpleBug.Field.selected.name()));
            }

            @Override
            public void onClone(SimpleBug data) {
                SimpleBug cloneData = (SimpleBug) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new BugEvent.GotoEdit(this, cloneData));
            }

            @Override
            public void gotoNext(SimpleBug data) {
                BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                BugSearchCriteria searchCriteria = new BugSearchCriteria();
                searchCriteria.setProjectId(NumberSearchField.equal(data.getProjectid()));
                searchCriteria.addExtraField(BugSearchCriteria.p_bugkey.buildSearchField(SearchField.AND, NumberI18nEnum.GREATER_THAN.name(),
                        data.getBugkey()));
                Integer nextId = bugService.getNextItemKey(searchCriteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new BugEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }
            }

            @Override
            public void gotoPrevious(SimpleBug data) {
                BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                BugSearchCriteria searchCriteria = new BugSearchCriteria();
                searchCriteria.setProjectId(NumberSearchField.equal(data.getProjectid()));
                searchCriteria.addExtraField(BugSearchCriteria.p_bugkey.buildSearchField(SearchField.AND, NumberI18nEnum.LESS_THAN.name(),
                        data.getBugkey()));
                Integer previousId = bugService.getPreviousItemKey(searchCriteria);
                if (previousId != null) {
                    EventBusFactory.getInstance().post(new BugEvent.GotoRead(this, previousId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new TicketEvent.GotoDashboard(this, null));
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.BUGS)) {
            if (data.getParams() instanceof Integer) {
                BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                SimpleBug bug = bugService.findById((Integer) data.getParams(), AppUI.getAccountId());
                if (bug != null) {
                    BugContainer bugContainer = (BugContainer) container;
                    bugContainer.removeAllComponents();
                    bugContainer.addComponent(view);
                    view.previewItem(bug);

                    ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
                    breadcrumb.gotoBugRead(bug);

                    AppEventBus.getInstance().post(new UpdateNotificationItemReadStatusEvent(UserUIContext.getUsername(),
                            ModuleNameConstants.PRJ, ProjectTypeConstants.BUG, bug.getId().toString()));
                } else {
                    throw new ResourceNotFoundException();
                }
            }
        } else {
            throw new SecureAccessException();
        }
    }
}
