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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.core.SecureAccessException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.db.query.NumberParam;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.project.view.ProjectGenericPresenter;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.reporting.FormReportLayout;
import com.esofthead.mycollab.reporting.PrintButton;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.LoadPolicy;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import org.vaadin.dialogs.ConfirmDialog;

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
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        new ConfirmDialog.Listener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void onClose(final ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                                    bugService.removeWithSession(data, AppContext.getUsername(), AppContext.getAccountId());
                                    EventBusFactory.getInstance().post(new BugEvent.GotoList(this, null));
                                }
                            }
                        });
            }

            @Override
            public void onPrint(Object source, SimpleBug data) {
                PrintButton btn = (PrintButton) source;
                btn.doPrint(data, new FormReportLayout(ProjectTypeConstants.BUG, BugWithBLOBs.Field.summary.name(),
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
                searchCriteria.setProjectId(NumberSearchField.and(data.getProjectid()));
                searchCriteria.addExtraField(BugSearchCriteria.p_bugkey.buildSearchField(SearchField.AND, NumberParam.GREATER_THAN, data.getBugkey()));
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
                searchCriteria.setProjectId(NumberSearchField.and(data.getProjectid()));
                searchCriteria.addExtraField(BugSearchCriteria.p_bugkey.buildSearchField(SearchField.AND, NumberParam.LESS_THAN,
                        data.getBugkey()));
                Integer previousId = bugService.getNextItemKey(searchCriteria);
                if (previousId != null) {
                    EventBusFactory.getInstance().post(new BugEvent.GotoRead(this, previousId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new BugEvent.GotoList(this, null));
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.BUGS)) {
            if (data.getParams() instanceof Integer) {
                BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                SimpleBug bug = bugService.findById((Integer) data.getParams(), AppContext.getAccountId());
                if (bug != null) {
                    BugContainer bugContainer = (BugContainer) container;
                    bugContainer.removeAllComponents();
                    bugContainer.addComponent(view);
                    view.previewItem(bug);

                    ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
                    breadcrumb.gotoBugRead(bug);
                } else {
                    throw new ResourceNotFoundException();
                }
            }
        } else {
            throw new SecureAccessException();
        }
    }
}
