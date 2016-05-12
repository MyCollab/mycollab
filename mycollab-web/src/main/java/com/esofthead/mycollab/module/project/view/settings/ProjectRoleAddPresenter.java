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

import com.esofthead.mycollab.core.SecureAccessException;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.ProjectRole;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.events.ProjectRoleEvent;
import com.esofthead.mycollab.module.project.service.ProjectRoleService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.IEditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectRoleAddPresenter extends AbstractPresenter<ProjectRoleAddView> {
    private static final long serialVersionUID = 1L;

    public ProjectRoleAddPresenter() {
        super(ProjectRoleAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<ProjectRole>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final ProjectRole item) {
                save(item);
                EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoList(this, null));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoList(this, null));
            }

            @Override
            public void onSaveAndNew(ProjectRole item) {
                save(item);
                EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoAdd(this, null));
            }
        });
    }

    public void save(ProjectRole item) {
        ProjectRoleService roleService = AppContextUtil.getSpringBean(ProjectRoleService.class);
        item.setSaccountid(AppContext.getAccountId());

        SimpleProject project = CurrentProjectVariables.getProject();
        item.setProjectid(project.getId());

        if (item.getId() == null) {
            roleService.saveWithSession(item, AppContext.getUsername());
        } else {
            roleService.updateWithSession(item, AppContext.getUsername());
        }

        roleService.savePermission(project.getId(), item.getId(), view.getPermissionMap(), AppContext.getAccountId());

    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.ROLES)) {
            ProjectRoleContainer roleContainer = (ProjectRoleContainer) container;
            roleContainer.removeAllComponents();
            roleContainer.addComponent(view);

            ProjectRole role = (ProjectRole) data.getParams();

            view.editItem(role);
            ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            if (role.getId() == null) {
                breadcrumb.gotoRoleAdd();
            } else {
                breadcrumb.gotoRoleEdit(role);
            }
        } else {
            throw new SecureAccessException();
        }
    }
}
