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
package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.common.i18n.OptionI18nEnum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultEditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class ProjectAddPresenter extends AbstractProjectPresenter<ProjectAddView> {
    public ProjectAddPresenter() {
        super(ProjectAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new DefaultEditFormHandler<SimpleProject>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final SimpleProject project) {
                Integer prjId = saveProject(project);
                EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, prjId));
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (AppContext.canAccess(RolePermissionCollections.CREATE_NEW_PROJECT)) {
            super.onGo(container, data);
            SimpleProject project = (SimpleProject) data.getParams();
            view.editItem(project);

            if (project.getId() == null) {
                AppContext.addFragment("project/add", "New Project");
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    private Integer saveProject(SimpleProject project) {
        ProjectService projectService = ApplicationContextUtil.getSpringBean(ProjectService.class);
        if (project.getProjectstatus() == null) {
            project.setProjectstatus(OptionI18nEnum.StatusI18nEnum.Open.name());
        }
        return projectService.saveWithSession(project, AppContext.getUsername());
    }
}
