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

package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import org.vaadin.dialogs.ConfirmDialog;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectMemberReadPresenter extends AbstractPresenter<ProjectMemberReadView> {
    private static final long serialVersionUID = 1L;

    public ProjectMemberReadPresenter() {
        super(ProjectMemberReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleProjectMember>() {
            @Override
            public void onEdit(SimpleProjectMember data) {
                EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoEdit(this, data));
            }

            @Override
            public void onDelete(final SimpleProjectMember data) {
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
                                    ProjectMemberService projectMemberService = ApplicationContextUtil.getSpringBean(ProjectMemberService.class);
                                    projectMemberService.removeWithSession(data, AppContext.getUsername(), AppContext.getAccountId());
                                    EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null));
                                }
                            }
                        });
            }

            @Override
            public void onClone(SimpleProjectMember data) {
                SimpleProjectMember cloneData = (SimpleProjectMember) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoEdit(this, cloneData));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null));
            }

            @Override
            public void gotoNext(SimpleProjectMember data) {
                ProjectMemberService projectMemberService = ApplicationContextUtil.getSpringBean(ProjectMemberService.class);
                ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER));
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                criteria.setStatus(new StringSearchField(ProjectMemberStatusConstants.ACTIVE));

                Integer nextId = projectMemberService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }

            }

            @Override
            public void gotoPrevious(SimpleProjectMember data) {
                ProjectMemberService projectMemberService = ApplicationContextUtil.getSpringBean(ProjectMemberService.class);
                ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESSTHAN));
                criteria.setStatus(new StringSearchField(ProjectMemberStatusConstants.ACTIVE));

                Integer nextId = projectMemberService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        boolean isCurrentUserAccess = false;

        if (data.getParams() instanceof String) {
            if (AppContext.getUsername().equals(data.getParams())) {
                isCurrentUserAccess = true;
            }
        }
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.USERS) || isCurrentUserAccess) {
            ProjectMemberService prjMemberService = ApplicationContextUtil.getSpringBean(ProjectMemberService.class);
            SimpleProjectMember prjMember = null;
            if (data.getParams() instanceof Integer) {
                prjMember = prjMemberService.findById((Integer) data.getParams(), AppContext.getAccountId());

            } else if (data.getParams() instanceof String) {
                String username = (String) data.getParams();
                prjMember = prjMemberService.findMemberByUsername(username, CurrentProjectVariables.getProjectId(), AppContext.getAccountId());
            }

            if (prjMember != null) {
                ProjectUserContainer userGroupContainer = (ProjectUserContainer) container;
                userGroupContainer.removeAllComponents();
                userGroupContainer.addComponent(view.getWidget());
                view.previewItem(prjMember);
                ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
                breadCrumb.gotoUserRead(prjMember);
            } else {
                NotificationUtil.showRecordNotExistNotification();
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
