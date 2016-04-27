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

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.IEditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.*;
import com.esofthead.mycollab.vaadin.web.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectMemberEditPresenter extends AbstractPresenter<ProjectMemberEditView> {
    private static final long serialVersionUID = 1L;

    public ProjectMemberEditPresenter() {
        super(ProjectMemberEditView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<SimpleProjectMember>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final SimpleProjectMember projectMember) {
                saveProjectMember(projectMember);
                ViewState viewState = HistoryViewManager.back();
                if (viewState instanceof NullViewState) {
                    EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null));
                }
            }

            @Override
            public void onCancel() {
                ViewState viewState = HistoryViewManager.back();
                if (viewState instanceof NullViewState) {
                    EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null));
                }
            }

            @Override
            public void onSaveAndNew(final SimpleProjectMember projectMember) {

            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.USERS)) {
            ProjectUserContainer userGroupContainer = (ProjectUserContainer) container;
            userGroupContainer.removeAllComponents();
            userGroupContainer.addComponent(view);

            SimpleProjectMember member = (SimpleProjectMember) data.getParams();
            view.editItem(member);

            ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            if (member.getId() == null) {
                breadcrumb.gotoUserAdd();
            } else {
                breadcrumb.gotoUserEdit(member);
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }

    }

    public void saveProjectMember(ProjectMember projectMember) {
        ProjectMemberService projectMemberService = ApplicationContextUtil.getSpringBean(ProjectMemberService.class);

        if (projectMember.getId() == null) {
            throw new MyCollabException("User not exist in projectMember table, something goes wrong in DB");
        } else {
            projectMemberService.updateWithSession(projectMember, AppContext.getUsername());
        }
    }

}
