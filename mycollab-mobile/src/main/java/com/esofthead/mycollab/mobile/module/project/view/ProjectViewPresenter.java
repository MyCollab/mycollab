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

import com.esofthead.mycollab.core.utils.ClassUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.ui.InsideProjectNavigationMenu;
import com.esofthead.mycollab.mobile.module.project.view.bug.BugPresenter;
import com.esofthead.mycollab.mobile.module.project.view.message.MessagePresenter;
import com.esofthead.mycollab.mobile.module.project.view.milestone.MilestonePresenter;
import com.esofthead.mycollab.mobile.module.project.view.parameters.*;
import com.esofthead.mycollab.mobile.module.project.view.settings.ProjectUserPresenter;
import com.esofthead.mycollab.mobile.module.project.view.task.TaskPresenter;
import com.esofthead.mycollab.mobile.mvp.AbstractPresenter;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.IPresenter;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class ProjectViewPresenter extends AbstractPresenter<ProjectView> {

    private static final long serialVersionUID = -2509768926569804614L;

    public ProjectViewPresenter() {
        super(ProjectView.class);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (data == null) {
            // do nothing
        }
        if (data.getParams() instanceof Integer) {
            ProjectService projectService = ApplicationContextUtil
                    .getSpringBean(ProjectService.class);
            SimpleProject project = projectService.findById(
                    (Integer) data.getParams(), AppContext.getAccountId());

            if (project == null) {
                NotificationUtil.showRecordNotExistNotification();
            } else {
                CurrentProjectVariables.setProject(project);
                ((MobileNavigationManager) UI.getCurrent().getContent())
                        .setNavigationMenu(new InsideProjectNavigationMenu());
            }
        }
    }

    @Override
    protected void onDefaultStopChain() {
        ProjectDashboardPresenter presenter = PresenterResolver
                .getPresenter(ProjectDashboardPresenter.class);
        presenter.go((MobileNavigationManager) UI.getCurrent().getContent(),
                null);
    }

    @Override
    protected void onHandleChain(ComponentContainer container,
                                 PageActionChain pageActionChain) {
        ScreenData<?> pageAction = pageActionChain.peek();

        IPresenter<?> presenter;
        if (ClassUtils.instanceOf(pageAction,
                ProjectScreenData.GotoDashboard.class)) {
            presenter = PresenterResolver
                    .getPresenter(ProjectDashboardPresenter.class);
        } else if (ClassUtils.instanceOf(pageAction,
                ProjectScreenData.ViewActivities.class)) {
            presenter = PresenterResolver
                    .getPresenter(ProjectActivityStreamPresenter.class);
        } else if (ClassUtils.instanceOf(pageAction,
                MessageScreenData.Read.class, MessageScreenData.Search.class,
                MessageScreenData.Add.class)) {
            presenter = PresenterResolver.getPresenter(MessagePresenter.class);
        } else if (ClassUtils.instanceOf(pageAction, TaskScreenData.List.class,
                TaskScreenData.Read.class, TaskScreenData.Add.class,
                TaskScreenData.Edit.class)) {
            presenter = PresenterResolver.getPresenter(TaskPresenter.class);
        } else if (ClassUtils.instanceOf(pageAction,
                MilestoneScreenData.List.class,
                MilestoneScreenData.Search.class,
                MilestoneScreenData.Read.class, MilestoneScreenData.Add.class,
                MilestoneScreenData.Edit.class)) {
            presenter = PresenterResolver
                    .getPresenter(MilestonePresenter.class);
        } else if (ClassUtils.instanceOf(pageAction,
                BugScreenData.Search.class, BugScreenData.Add.class,
                BugScreenData.Read.class, BugScreenData.Edit.class)) {
            presenter = PresenterResolver.getPresenter(BugPresenter.class);
        } else if (ClassUtils.instanceOf(pageAction,
                ProjectMemberScreenData.Search.class,
                ProjectMemberScreenData.Add.class,
                ProjectMemberScreenData.InviteProjectMembers.class,
                ProjectMemberScreenData.Read.class,
                ProjectMemberScreenData.Edit.class)) {
            presenter = PresenterResolver
                    .getPresenter(ProjectUserPresenter.class);
        } else {
            throw new UnsupportedOperationException(
                    "Not support page action chain " + pageAction);
        }

        presenter.handleChain(container, pageActionChain);
    }
}
