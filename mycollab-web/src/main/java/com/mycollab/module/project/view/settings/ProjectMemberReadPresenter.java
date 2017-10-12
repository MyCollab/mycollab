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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectMemberStatusConstants;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.module.project.event.ProjectMemberEvent;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

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
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
                                projectMemberService.removeWithSession(data, UserUIContext.getUsername(), AppUI.getAccountId());
                                EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null));
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
                ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
                ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER));
                criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
                criteria.setStatuses(new SetSearchField<>(ProjectMemberStatusConstants.ACTIVE, ProjectMemberStatusConstants.NOT_ACCESS_YET));

                Integer nextId = projectMemberService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }

            }

            @Override
            public void gotoPrevious(SimpleProjectMember data) {
                ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
                ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESS_THAN));
                criteria.setStatuses(new SetSearchField<>(ProjectMemberStatusConstants.ACTIVE, ProjectMemberStatusConstants.NOT_ACCESS_YET));

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
    protected void onGo(HasComponents container, ScreenData<?> data) {
        boolean isCurrentUserAccess = false;

        if (data.getParams() instanceof String) {
            if (UserUIContext.getUsername().equals(data.getParams())) {
                isCurrentUserAccess = true;
            }
        }
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.USERS) || isCurrentUserAccess) {
            ProjectMemberService prjMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
            SimpleProjectMember prjMember = null;
            if (data.getParams() instanceof Integer) {
                prjMember = prjMemberService.findById((Integer) data.getParams(), AppUI.getAccountId());

            } else if (data.getParams() instanceof String) {
                String username = (String) data.getParams();
                prjMember = prjMemberService.findMemberByUsername(username, CurrentProjectVariables.getProjectId(), AppUI.getAccountId());
            }

            if (prjMember != null) {
                ProjectUserContainer userGroupContainer = (ProjectUserContainer) container;
                userGroupContainer.setContent(view);
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
