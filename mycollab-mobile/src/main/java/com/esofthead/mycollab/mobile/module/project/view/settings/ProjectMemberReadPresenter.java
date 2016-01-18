/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.settings;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.mobile.module.project.view.AbstractProjectPresenter;
import com.esofthead.mycollab.mobile.ui.ConfirmDialog;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class ProjectMemberReadPresenter extends AbstractProjectPresenter<ProjectMemberReadView> {
    private static final long serialVersionUID = 1287812620895443711L;

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
                ConfirmDialog.show(UI.getCurrent(),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        new ConfirmDialog.CloseListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    ProjectMemberService projectMemberService = ApplicationContextUtil.
                                            getSpringBean(ProjectMemberService.class);
                                    projectMemberService.removeWithSession(data,
                                            AppContext.getUsername(), AppContext.getAccountId());
                                    EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null));
                                }
                            }
                        });
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
                prjMember = prjMemberService.findMemberByUsername(username, CurrentProjectVariables.getProjectId(),
                        AppContext.getAccountId());
            }
            if (prjMember != null) {
                this.view.previewItem(prjMember);
                super.onGo(container, data);

                AppContext.addFragment("project/user/preview/" + UrlEncodeDecoder.encode(CurrentProjectVariables
                        .getProjectId() + "/" + prjMember.getUsername()), prjMember.getDisplayName());
            } else {
                NotificationUtil.showRecordNotExistNotification();
                return;
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

}
