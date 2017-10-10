/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.user;

import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.view.*;
import com.mycollab.module.project.view.assignments.ICalendarPresenter;
import com.mycollab.module.project.view.parameters.ProjectScreenData;
import com.mycollab.module.project.view.parameters.ReportScreenData;
import com.mycollab.module.project.view.parameters.StandupScreenData;
import com.mycollab.module.project.view.reports.IReportPresenter;
import com.mycollab.vaadin.mvp.*;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectDashboardPresenter extends AbstractPresenter<ProjectDashboardContainer> {
    private static final long serialVersionUID = 1L;

    public ProjectDashboardPresenter() {
        super(ProjectDashboardContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ProjectView projectViewContainer = (ProjectView) container;
        projectViewContainer.gotoSubView(ProjectTypeConstants.DASHBOARD);

        ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);

        if (data instanceof ProjectScreenData.Edit) {
            if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PROJECT)) {
                ProjectAddPresenter presenter = PresenterResolver.getPresenter(ProjectAddPresenter.class);
                presenter.go(view, data);
                breadcrumb.gotoProjectEdit();
            } else {
                NotificationUtil.showMessagePermissionAlert();
            }
        } else if (data instanceof ProjectScreenData.GotoTagList) {
            ITagListPresenter presenter = PresenterResolver.getPresenter(ITagListPresenter.class);
            presenter.go(view, data);
        } else if (data instanceof ProjectScreenData.GotoFavorite) {
            IFavoritePresenter presenter = PresenterResolver.getPresenter(IFavoritePresenter.class);
            presenter.go(view, data);
        } else if (data instanceof ProjectScreenData.SearchItem) {
            ProjectSearchItemPresenter presenter = PresenterResolver.getPresenter(ProjectSearchItemPresenter.class);
            presenter.go(view, data);
        } else if (data instanceof ProjectScreenData.GotoGanttChart) {
            IGanttChartPresenter presenter = PresenterResolver.getPresenter(IGanttChartPresenter.class);
            presenter.go(view, data);
        } else if (data instanceof ProjectScreenData.GotoCalendarView) {
            ICalendarPresenter presenter = PresenterResolver.getPresenter(ICalendarPresenter.class);
            presenter.go(view, data);
        } else if (data instanceof ProjectScreenData.GotoReportConsole || data instanceof StandupScreenData.Search
                || data instanceof ReportScreenData.GotoWeeklyTiming) {
            IReportPresenter presenter = PresenterResolver.getPresenter(IReportPresenter.class);
            presenter.go(view, data);
        } else {
            if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.PROJECT)) {
                ProjectSummaryPresenter presenter = PresenterResolver.getPresenter(ProjectSummaryPresenter.class);
                presenter.go(view, data);
                breadcrumb.gotoProjectDashboard();
            } else {
                NotificationUtil.showMessagePermissionAlert();
            }
        }
    }

    @Override
    protected void onHandleChain(HasComponents container, PageActionChain pageActionChain) {
        ScreenData<?> pageAction = pageActionChain.peek();

        Class<? extends IPresenter> presenterCls = ProjectPresenterDataMapper.presenter(pageAction);
        if (presenterCls != null) {
            IPresenter<?> presenter = PresenterResolver.getPresenter(presenterCls);
            presenter.handleChain(view, pageActionChain);
        } else {
            throw new UnsupportedOperationException("Not support page action chain " + pageAction);
        }
    }
}
