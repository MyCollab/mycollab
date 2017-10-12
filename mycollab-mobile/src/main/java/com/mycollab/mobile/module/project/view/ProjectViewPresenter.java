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
package com.mycollab.mobile.module.project.view;

import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.core.utils.ClassUtils;
import com.mycollab.mobile.module.project.view.message.MessagePresenter;
import com.mycollab.mobile.module.project.view.milestone.MilestonePresenter;
import com.mycollab.mobile.module.project.view.parameters.*;
import com.mycollab.mobile.module.project.view.settings.ProjectUserPresenter;
import com.mycollab.mobile.module.project.view.ticket.TicketPresenter;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.mvp.IPresenter;
import com.mycollab.vaadin.mvp.PageActionChain;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class ProjectViewPresenter extends ProjectGenericPresenter<ProjectView> {
    private static final long serialVersionUID = -2509768926569804614L;

    public ProjectViewPresenter() {
        super(ProjectView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (data == null) {
            // do nothing
        }
        if (data.getParams() instanceof Integer) {
            ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
            SimpleProject project = projectService.findById((Integer) data.getParams(), AppUI.getAccountId());

            if (project == null) {
                throw new ResourceNotFoundException();
            } else {
                CurrentProjectVariables.setProject(project);
            }
        }
    }

    @Override
    protected void onHandleChain(HasComponents container, PageActionChain pageActionChain) {
        ScreenData<?> pageAction = pageActionChain.peek();

        IPresenter<?> presenter;
        if (ClassUtils.instanceOf(pageAction, ProjectScreenData.GotoDashboard.class)) {
            presenter = PresenterResolver.getPresenter(ProjectDashboardPresenter.class);
        } else if (ClassUtils.instanceOf(pageAction, ProjectScreenData.ProjectActivities.class)) {
            presenter = PresenterResolver.getPresenter(ProjectActivityStreamPresenter.class);
        } else if (ClassUtils.instanceOf(pageAction, MessageScreenData.Read.class, MessageScreenData.Search.class,
                MessageScreenData.Add.class)) {
            presenter = PresenterResolver.getPresenter(MessagePresenter.class);
        } else if (ClassUtils.instanceOf(pageAction, TaskScreenData.Read.class,
                TaskScreenData.Add.class, TaskScreenData.Edit.class, BugScreenData.Add.class,
                BugScreenData.Read.class, BugScreenData.Edit.class, TicketScreenData.GotoDashboard.class,
                RiskScreenData.Add.class, RiskScreenData.Edit.class, RiskScreenData.Read.class)) {
            presenter = PresenterResolver.getPresenter(TicketPresenter.class);
        } else if (ClassUtils.instanceOf(pageAction, MilestoneScreenData.Search.class,
                MilestoneScreenData.Read.class, MilestoneScreenData.Add.class, MilestoneScreenData.Edit.class)) {
            presenter = PresenterResolver.getPresenter(MilestonePresenter.class);
        } else if (ClassUtils.instanceOf(pageAction, ProjectMemberScreenData.Search.class,
                ProjectMemberScreenData.InviteProjectMembers.class, ProjectMemberScreenData.Read.class, ProjectMemberScreenData.Edit.class)) {
            presenter = PresenterResolver.getPresenter(ProjectUserPresenter.class);
        } else {
            throw new UnsupportedOperationException("Not support page action chain " + pageAction);
        }

        presenter.handleChain(container, pageActionChain);
    }
}
