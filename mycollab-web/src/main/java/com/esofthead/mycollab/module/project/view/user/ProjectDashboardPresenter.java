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

package com.esofthead.mycollab.module.project.view.user;

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.project.view.ProjectView;
import com.esofthead.mycollab.module.project.view.TagListPresenter;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectDashboardPresenter extends
        AbstractPresenter<ProjectDashboardContainer> {
    private static final long serialVersionUID = 1L;

    public ProjectDashboardPresenter() {
        super(ProjectDashboardContainer.class);
    }

    @Override
    public void go(ComponentContainer container, ScreenData<?> data) {
        super.go(container, data, false);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        ProjectView projectViewContainer = (ProjectView) container;
        projectViewContainer.gotoSubView(ProjectTypeConstants.DASHBOARD);

        view.removeAllComponents();

        ProjectBreadcrumb breadcrumb = ViewManager
                .getCacheComponent(ProjectBreadcrumb.class);

        if (data instanceof ProjectScreenData.Edit) {
            if (CurrentProjectVariables
                    .canWrite(ProjectRolePermissionCollections.PROJECT)) {
                ProjectAddPresenter presenter = PresenterResolver
                        .getPresenter(ProjectAddPresenter.class);
                presenter.go(view, data);
                breadcrumb.gotoProjectEdit();
            } else {
                NotificationUtil.showMessagePermissionAlert();
            }
        } else if (data instanceof ProjectScreenData.GotoTagList) {
            TagListPresenter presenter = PresenterResolver.getPresenter(TagListPresenter.class);
            presenter.go(view, data);
        } else {
            if (CurrentProjectVariables
                    .canRead(ProjectRolePermissionCollections.PROJECT)) {
                ProjectSummaryPresenter presenter = PresenterResolver
                        .getPresenter(ProjectSummaryPresenter.class);
                presenter.go(view, data);
                breadcrumb.gotoProjectDashboard();
            } else {
                NotificationUtil.showMessagePermissionAlert();
            }
        }
    }
}
