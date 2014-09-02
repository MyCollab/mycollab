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
package com.esofthead.mycollab.module.project.view;

import java.util.List;

import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.module.project.events.FollowingTicketEvent;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.events.TimeTrackingEvent;
import com.esofthead.mycollab.module.project.view.parameters.FollowingTicketsScreenData;
import com.esofthead.mycollab.vaadin.mvp.AbstractController;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.google.common.eventbus.Subscribe;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 * 
 */
public class ProjectModuleController extends AbstractController {
	private static final long serialVersionUID = 1L;

	private ProjectModule container;

	public ProjectModuleController(ProjectModule container) {
		this.container = container;
		bindProjectEvents();
		bindFollowingTicketEvents();
		bindTimeTrackingEvents();
	}

	@SuppressWarnings("serial")
	private void bindProjectEvents() {
		this.register(new ApplicationEventListener<ProjectEvent.GotoMyProject>() {

			@Subscribe
			@Override
			public void handle(ProjectEvent.GotoMyProject event) {
				ProjectViewPresenter presenter = PresenterResolver
						.getPresenter(ProjectViewPresenter.class);
				presenter.handleChain(container,
						(PageActionChain) event.getData());
			}
		});
	}

	private void bindFollowingTicketEvents() {
		this.register(new ApplicationEventListener<FollowingTicketEvent.GotoMyFollowingItems>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(FollowingTicketEvent.GotoMyFollowingItems event) {
				FollowingTicketPresenter presenter = PresenterResolver
						.getPresenter(FollowingTicketPresenter.class);
				presenter.go(container,
						new FollowingTicketsScreenData.GotoMyFollowingItems(
								(List<Integer>) event.getData()));
			}
		});
	}

	private void bindTimeTrackingEvents() {
		this.register(new ApplicationEventListener<TimeTrackingEvent.GotoTimeTrackingView>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TimeTrackingEvent.GotoTimeTrackingView event) {
				TimeTrackingSummaryPresenter presenter = PresenterResolver
						.getPresenter(TimeTrackingSummaryPresenter.class);
				presenter.go(container, new ScreenData(event.getData()));
			}
		});
	}
}
