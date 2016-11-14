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

package com.mycollab.module.project.view.ticket;

import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.ClassUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.module.project.view.parameters.TaskScreenData;
import com.mycollab.module.project.view.parameters.TicketScreenData;
import com.mycollab.module.project.view.task.TaskAddPresenter;
import com.mycollab.module.project.view.task.TaskKanbanBoardPresenter;
import com.mycollab.module.project.view.task.TaskReadPresenter;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TicketPresenter extends AbstractPresenter<TicketContainer> {
    private static final long serialVersionUID = 1L;

    public TicketPresenter() {
        super(TicketContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ProjectView projectViewContainer = (ProjectView) container;
        projectViewContainer.gotoSubView(ProjectTypeConstants.TICKET);

        AbstractPresenter<?> presenter;

        if (data instanceof TaskScreenData.Read) {
            presenter = PresenterResolver.getPresenter(TaskReadPresenter.class);
        } else if (ClassUtils.instanceOf(data, TaskScreenData.Edit.class, TaskScreenData.Add.class)) {
            presenter = PresenterResolver.getPresenter(TaskAddPresenter.class);
        } else if (data instanceof TaskScreenData.GotoKanbanView) {
            presenter = PresenterResolver.getPresenter(TaskKanbanBoardPresenter.class);
        } else if (data == null || data instanceof TicketScreenData.GotoDashboard) {
            presenter = PresenterResolver.getPresenter(TicketDashboardPresenter.class);
        } else {
            throw new MyCollabException("No support data: " + data);
        }

        presenter.go(view, data);
    }
}
