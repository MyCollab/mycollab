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

import com.esofthead.mycollab.common.domain.criteria.MonitorSearchCriteria;
import com.esofthead.mycollab.common.i18n.FollowerI18nEnum;
import com.esofthead.mycollab.common.service.MonitorItemService;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.events.FollowingTicketEvent;
import com.esofthead.mycollab.module.project.events.TimeTrackingEvent;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.view.user.ActivityStreamComponent;
import com.esofthead.mycollab.module.project.view.user.MyProjectListComponent;
import com.esofthead.mycollab.module.project.view.user.TaskStatusComponent;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractLazyPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class UserDashboardViewImpl extends AbstractLazyPageView implements
		UserDashboardView {
	private static final long serialVersionUID = 1L;

	private ButtonLink followingTicketsLink;

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

		Button avatar = UserAvatarControlFactory
				.createUserAvatarEmbeddedButton(AppContext.getUserAvatarId(),
						64);
		avatar.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				String userFullLinkStr = AccountLinkGenerator
						.generatePreviewFullUserLink(AppContext.getSiteUrl(),
								AppContext.getUsername());
				getUI().getPage().open(userFullLinkStr, null);
			}
		});

		header.addComponent(avatar);

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
							.getMessage(ProjectCommonI18nEnum.BUTTON_NEW_PROJECT),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final Button.ClickEvent event) {
							final ProjectAddWindow projectNewWindow = new ProjectAddWindow();
							UI.getCurrent().addWindow(projectNewWindow);
						}
					});
			createProjectBtn.setIcon(MyCollabResource
					.newResource(WebResourceIds._16_addRecord));
			createProjectBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			headerContentTop.addComponent(createProjectBtn);
			headerContentTop.setComponentAlignment(createProjectBtn,
					Alignment.MIDDLE_LEFT);
		}

		final HorizontalLayout headerContentBottom = new HorizontalLayout();
		headerContentBottom.setSpacing(true);
		followingTicketsLink = new ButtonLink(AppContext.getMessage(
				FollowerI18nEnum.OPT_MY_FOLLOWING_TICKETS, 0));

		followingTicketsLink.setIcon(MyCollabResource
				.newResource("icons/16/follow.png"));
		followingTicketsLink.removeStyleName("wordWrap");
		followingTicketsLink.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (prjKeys != null) {
					EventBusFactory.getInstance().post(
							new FollowingTicketEvent.GotoMyFollowingItems(
									UserDashboardViewImpl.this, prjKeys));
				}
			}
		});

		ButtonLink timeTrackingLink = new ButtonLink(
				AppContext.getMessage(TimeTrackingI18nEnum.TIME_RECORD_HEADER));
		timeTrackingLink.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				EventBusFactory.getInstance().post(
						new TimeTrackingEvent.GotoTimeTrackingView(
								UserDashboardViewImpl.this, prjKeys));
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
		ActivityStreamComponent activityStreamComponent = new ActivityStreamComponent();
		leftPanel.addComponent(activityStreamComponent);
		leftPanel.setWidth("100%");

		final VerticalLayout rightPanel = new VerticalLayout();
		MyProjectListComponent myProjectListComponent = new MyProjectListComponent();
		TaskStatusComponent taskStatusComponent = new TaskStatusComponent();
		rightPanel.setWidth("565px");
		rightPanel.addComponent(myProjectListComponent);
		rightPanel.addComponent(taskStatusComponent);

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
		prjKeys = prjService.getProjectKeysUserInvolved(
				AppContext.getUsername(), AppContext.getAccountId());
		if (CollectionUtils.isNotEmpty(prjKeys)) {
			activityStreamComponent.showFeeds(prjKeys);
			myProjectListComponent.displayDefaultProjectsList();
			displayFollowingTicketsCount();
		}

		taskStatusComponent.showProjectTasksByStatus();

	}

	private void displayFollowingTicketsCount() {
		// show following ticket numbers
		MonitorSearchCriteria searchCriteria = new MonitorSearchCriteria();
		searchCriteria.setUser(new StringSearchField(SearchField.AND,
				AppContext.getUsername()));
		searchCriteria.setExtraTypeIds(new SetSearchField<>(prjKeys
				.toArray(new Integer[prjKeys.size()])));
		MonitorItemService monitorService = ApplicationContextUtil
				.getSpringBean(MonitorItemService.class);
		int followingItemsCount = monitorService.getTotalCount(searchCriteria);
		followingTicketsLink
				.setCaption(AppContext.getMessage(
						FollowerI18nEnum.OPT_MY_FOLLOWING_TICKETS,
						followingItemsCount));
	}
}
