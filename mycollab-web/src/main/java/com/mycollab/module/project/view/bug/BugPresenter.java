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
package com.mycollab.module.project.view.bug;

import com.mycollab.core.MyCollabException;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.module.project.view.parameters.BugScreenData;
import com.mycollab.module.project.view.ticket.TicketContainer;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugPresenter extends AbstractPresenter<BugContainer> {
    private static final long serialVersionUID = 1L;

    public BugPresenter() {
        super(BugContainer.class);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        ProjectView projectViewContainer = (ProjectView) container;
        TicketContainer ticketContainer = (TicketContainer) projectViewContainer.gotoSubView(ProjectTypeConstants.TICKET);
        ticketContainer.setContent(view);

        view.removeAllComponents();

        AbstractPresenter<?> presenter;

        if (data instanceof BugScreenData.Add || data instanceof BugScreenData.Edit) {
            presenter = PresenterResolver.getPresenter(BugAddPresenter.class);
        } else if (data instanceof BugScreenData.Read) {
            presenter = PresenterResolver.getPresenter(BugReadPresenter.class);
        } else if (data instanceof BugScreenData.GotoKanbanView) {
            presenter = PresenterResolver.getPresenter(BugKanbanPresenter.class);
        } else if (data == null) {
            presenter = PresenterResolver.getPresenter(BugListPresenter.class);
        } else if (data instanceof BugScreenData.GotoList) {
            presenter = PresenterResolver.getPresenter(BugListPresenter.class);
        } else {
            throw new MyCollabException("Do not support screen data");
        }

        presenter.go(view, data);
    }

}
