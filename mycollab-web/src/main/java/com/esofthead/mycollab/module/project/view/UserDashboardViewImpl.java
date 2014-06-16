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

import com.esofthead.mycollab.common.domain.criteria.MonitorSearchCriteria;
import com.esofthead.mycollab.common.service.MonitorItemService;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.project.events.FollowingTicketEvent;
import com.esofthead.mycollab.module.project.events.TimeTrackingEvent;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.view.user.ActivityStreamComponent;
import com.esofthead.mycollab.module.project.view.user.MyProjectListComponent;
import com.esofthead.mycollab.module.project.view.user.TaskStatusComponent;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractLazyPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class UserDashboardViewImpl extends AbstractLazyPageView implements
		UserDashboardView {
	private static final long serialVersionUID = 1L;

	private ButtonLink followingTicketsLink;

	private ButtonLink timeTrackingLink;

	private MyProjectListComponent myProjectListComponent;

	private ActivityStreamComponent activityStreamComponent;

	private TaskStatusComponent taskStatusComponent;

	private List<Integer> prjKeys;

	@Override
	protected void displayView() {
		this.removeAllComponents();

		this.setSpacing(true);
		this.setWidth("100%");

		final CssLayout headerWrapper = new CssLayout();
		headerWrapper.setWidth("100%");
		headerWrapper.setStyleName("projectfeed-hdr-wrapper");

		final HorizontalLayout header = new HorizontalLayout();
		header.setWidth("100%");
		header.setMargin(false);
		header.setSpacing(true);
		header.addStyleName("projectfeed-hdr");

		header.addComponent(UserAvatarControlFactory
				.createUserAvatarEmbeddedComponent(
						AppContext.getUserAvatarId(), 64));

		final VerticalLayout headerContent = new VerticalLayout();
		headerContent.addStyleName("projectfeed-hdr-content");
		headerContent.setSpacing(true);
		headerContent.setMargin(new MarginInfo(false, false, false, true));

		final Label headerLabel = new Label(AppContext.getSession()
				.getDisplayName());
		headerLabel.setStyleName(Reindeer.LABEL_H1);

		final HorizontalLayout headerContentTop = new HorizontalLayout();
		headerContentTop.setSpacing(true);
		headerContentTop.setMargin(new MarginInfo(false, false, true, false));
		headerContentTop.addComponent(headerLabel);
		headerContentTop.setComponentAlignment(headerLabel, Alignment.TOP_LEFT);

		if (AppContext.canBeYes(RolePermissionCollections.CREATE_NEW_PROJECT)) {
			final Button createProjectBtn = new Button(
					AppContext
							.getMessage(ProjectCommonI18nEnum.NEW_PROJECT_ACTION),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final Button.ClickEvent event) {
							final ProjectAddWindow projectNewWindow = new ProjectAddWindow();
							UI.getCurrent().addWindow(projectNewWindow);
						}
					});
			createProjectBtn.setIcon(MyCollabResource
					.newResource("icons/16/addRecord.png"));
			createProjectBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			headerContentTop.addComponent(createProjectBtn);
			headerContentTop.setComponentAlignment(createProjectBtn,
					Alignment.MIDDLE_LEFT);
		}

		final HorizontalLayout headerContentBottom = new HorizontalLayout();
		headerContentBottom.setSpacing(true);
		followingTicketsLink = new ButtonLink("My Following Tickets (" + "0"
				+ ")");

		followingTicketsLink.setIcon(MyCollabResource
				.newResource("icons/16/follow.png"));
		followingTicketsLink.removeStyleName("wordWrap");
		followingTicketsLink.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (prjKeys != null) {
					EventBus.getInstance().fireEvent(
							new FollowingTicketEvent.GotoMyFollowingItems(
									UserDashboardViewImpl.this, prjKeys));
				}
			}
		});

		timeTrackingLink = new ButtonLink("Time Tracking");
		timeTrackingLink.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(
						new TimeTrackingEvent.GotoTimeTrackingView(
								UserDashboardViewImpl.this, null));
			}
		});
		timeTrackingLink.setIcon(MyCollabResource
				.newResource("icons/16/project/time_white.png"));
		timeTrackingLink.removeStyleName("wordWrap");
		headerContentBottom.addComponent(followingTicketsLink);
		headerContentBottom.addComponent(timeTrackingLink);

		headerContent.addComponent(headerContentTop);
		headerContent.addComponent(headerContentBottom);

		header.addComponent(headerContent);
		header.setExpandRatio(headerContent, 1.0f);
		headerWrapper.addComponent(header);

		this.addComponent(headerWrapper);

		final HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setMargin(new MarginInfo(false, false, true, false));
		layout.setSpacing(true);

		final VerticalLayout leftPanel = new VerticalLayout();
		leftPanel.setMargin(new MarginInfo(false, true, false, false));
		this.activityStreamComponent = new ActivityStreamComponent();
		leftPanel.addComponent(this.activityStreamComponent);
		leftPanel.setWidth("100%");

		final VerticalLayout rightPanel = new VerticalLayout();
		this.myProjectListComponent = new MyProjectListComponent();
		this.taskStatusComponent = new TaskStatusComponent();
		rightPanel.setWidth("565px");
		rightPanel.addComponent(this.myProjectListComponent);
		rightPanel.addComponent(this.taskStatusComponent);

		layout.addComponent(leftPanel);
		layout.addComponent(rightPanel);
		layout.setExpandRatio(leftPanel, 1.0f);

		final CssLayout contentWrapper = new CssLayout();
		contentWrapper.setWidth("100%");
		contentWrapper.addStyleName("content-wrapper");
		this.addComponent(contentWrapper);
		contentWrapper.addComponent(layout);

		final ProjectService prjService = ApplicationContextUtil
				.getSpringBean(ProjectService.class);
		prjKeys = prjService.getUserProjectKeys(AppContext.getUsername(),
				AppContext.getAccountId());
		if (prjKeys != null && !prjKeys.isEmpty()) {
			this.activityStreamComponent.showFeeds(prjKeys);
			this.myProjectListComponent.showProjects(prjKeys);
			displayFollowingTicketsCount();
		}

		this.taskStatusComponent.showProjectTasksByStatus();

	}

	private void displayFollowingTicketsCount() {
		// show following ticket numbers
		MonitorSearchCriteria searchCriteria = new MonitorSearchCriteria();
		searchCriteria.setUser(new StringSearchField(SearchField.AND,
				AppContext.getUsername()));
		searchCriteria.setExtraTypeIds(new SetSearchField<Integer>(prjKeys
				.toArray(new Integer[0])));
		MonitorItemService monitorService = ApplicationContextUtil
				.getSpringBean(MonitorItemService.class);
		int followingItemsCount = monitorService.getTotalCount(searchCriteria);
		followingTicketsLink.setCaption("My Following Tickets ("
				+ followingItemsCount + ")");
	}
}
