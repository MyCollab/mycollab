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
package com.esofthead.mycollab.shell.view;

import java.util.Calendar;
import java.util.Date;

import org.vaadin.hene.popupbutton.PopupButton;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.common.ui.components.TimezoneNotification;
import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.events.SessionEvent;
import com.esofthead.mycollab.events.SessionEvent.UserProfileChangeEvent;
import com.esofthead.mycollab.module.billing.AccountStatusConstants;
import com.esofthead.mycollab.module.billing.service.BillingService;
import com.esofthead.mycollab.module.user.domain.BillingPlan;
import com.esofthead.mycollab.module.user.domain.SimpleBillingAccount;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.desktop.ui.ModuleHelper;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ControllerRegistry;
import com.esofthead.mycollab.vaadin.mvp.IModule;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.FeedbackWindow;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.NotificationButton;
import com.esofthead.mycollab.vaadin.ui.ServiceMenu;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.esofthead.mycollab.web.CustomLayoutLoader;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@ViewComponent
public final class MainView extends AbstractPageView {
	private static final long serialVersionUID = 1L;

	private CssLayout bodyLayout;

	private ServiceMenu serviceMenu;

	public MainView() {
		this.setSizeFull();
		ControllerRegistry.addController(new MainViewController(this));
	}

	public void initialize() {
		this.removeAllComponents();
		this.addComponent(this.createTopMenu());
		this.bodyLayout = new CssLayout();
		this.bodyLayout.addStyleName("main-body");
		this.bodyLayout.setWidth("100%");
		this.bodyLayout.setHeight("100%");
		this.addComponent(this.bodyLayout);
		this.setExpandRatio(this.bodyLayout, 1.0f);
		this.addComponent(this.createFooter());
	}

	public void addModule(final IModule module) {
		ModuleHelper.setCurrentModule(module);
		this.bodyLayout.removeAllComponents();
		this.bodyLayout.addComponent(module.getWidget());

		if (ModuleHelper.isCurrentCrmModule()) {
			serviceMenu.selectService(0);
		} else if (ModuleHelper.isCurrentProjectModule()) {
			serviceMenu.selectService(1);
		} else if (ModuleHelper.isCurrentFileModule()) {
			serviceMenu.selectService(2);
		}
	}

	private CustomLayout createFooter() {
		final CustomLayout footer = CustomLayoutLoader.createLayout("footer");

		Link companyLink = new Link("eSoftHead", new ExternalResource(
				"http://www.esofthead.com"));
		companyLink.setTargetName("_blank");

		footer.addComponent(companyLink, "company-url");

		Calendar currentCal = Calendar.getInstance();

		Label currentYear = new Label(String.valueOf(currentCal
				.get(Calendar.YEAR)));
		currentYear.setSizeUndefined();
		footer.addComponent(currentYear, "current-year");

		HorizontalLayout footerRight = new HorizontalLayout();
		footerRight.setSpacing(true);

		final Button sendFeedback = new Button("Feedback");
		sendFeedback.setStyleName("link");
		sendFeedback.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				UI.getCurrent().addWindow(new FeedbackWindow());
			}
		});
		Link blogLink = new Link("Blog", new ExternalResource(
				"http://blog.mycollab.com"));
		blogLink.setTargetName("_blank");

		Link forumLink = new Link("Forum", new ExternalResource(
				"http://forum.mycollab.com"));
		forumLink.setTargetName("_blank");

		Link wikiLink = new Link("Knowledge Base", new ExternalResource(
				"http://wiki.mycollab.com"));
		wikiLink.setTargetName("_blank");

		footerRight.addComponent(blogLink);
		footerRight.addComponent(forumLink);
		footerRight.addComponent(wikiLink);
		footerRight.addComponent(sendFeedback);

		footer.addComponent(footerRight, "footer-right");
		return footer;
	}

	private CustomLayout createTopMenu() {
		final CustomLayout layout = CustomLayoutLoader
				.createLayout("topNavigation");
		layout.setStyleName("topNavigation");
		layout.setHeight("40px");
		layout.setWidth("100%");
		serviceMenu = new ServiceMenu();
		serviceMenu.addStyleName("topNavPopup");

		serviceMenu.addService(
				AppContext.getMessage(GenericI18Enum.CRM_MODULE),
				MyCollabResource.newResource("icons/16/customer.png"),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						EventBus.getInstance().fireEvent(
								new ShellEvent.GotoCrmModule(this, null));
					}
				});

		serviceMenu.addService(
				AppContext.getMessage(GenericI18Enum.PROJECT_MODULE),
				MyCollabResource.newResource("icons/16/project.png"),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						EventBus.getInstance().fireEvent(
								new ShellEvent.GotoProjectModule(this, null));
					}
				});

		serviceMenu.addService(
				AppContext.getMessage(GenericI18Enum.DOCUMENT_MODULE),
				MyCollabResource.newResource("icons/16/document.png"),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						EventBus.getInstance().fireEvent(
								new ShellEvent.GotoFileModule(this, null));
					}
				});

		layout.addComponent(serviceMenu, "serviceMenu");

		final HorizontalLayout accountLayout = new HorizontalLayout();
		accountLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		accountLayout.setMargin(new MarginInfo(false, true, false, false));
		accountLayout.setSpacing(true);

		// display trial box if user in trial mode
		SimpleBillingAccount billingAccount = AppContext.getBillingAccount();
		if (AccountStatusConstants.TRIAL.equals(billingAccount.getStatus())) {
			Label informLbl = new Label("", ContentMode.HTML);
			informLbl.addStyleName("trialEndingNotification");
			informLbl.setHeight("100%");
			HorizontalLayout informBox = new HorizontalLayout();
			informBox.addStyleName("trialInformBox");
			informBox.setSizeFull();
			informBox.addComponent(informLbl);
			informBox.setMargin(new MarginInfo(false, true, false, false));
			informBox
					.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void layoutClick(LayoutClickEvent event) {
							EventBus.getInstance().fireEvent(
									new ShellEvent.GotoUserAccountModule(this,
											new String[] { "billing" }));
						}
					});
			accountLayout.addComponent(informBox);
			accountLayout.setSpacing(true);
			accountLayout.setComponentAlignment(informBox,
					Alignment.MIDDLE_LEFT);

			Date createdtime = billingAccount.getCreatedtime();
			long timeDeviation = System.currentTimeMillis()
					- createdtime.getTime();
			int daysLeft = (int) Math.floor(timeDeviation
					/ (1000 * 60 * 60 * 24));
			if (daysLeft > 30) {
				BillingService billingService = ApplicationContextUtil
						.getSpringBean(BillingService.class);
				BillingPlan freeBillingPlan = billingService
						.getFreeBillingPlan();
				billingAccount.setBillingPlan(freeBillingPlan);

				informLbl
						.setValue("<div class='informBlock'>TRIAL ENDING<br>"
								+ " 0 DAYS LEFT</div><div class='informBlock'>&gt;&gt;</div>");
			} else {
				if (AppContext.isAdmin()) {
					informLbl
							.setValue("<div class='informBlock'>TRIAL ENDING<br>"
									+ (30 - daysLeft)
									+ " DAYS LEFT</div><div class='informBlock'>&gt;&gt;</div>");
				} else {
					informLbl
							.setValue("<div class='informBlock'>TRIAL ENDING<br>"
									+ (30 - daysLeft)
									+ " DAYS LEFT</div><div class='informBlock'>&gt;&gt;</div>");
				}
			}
		}

		NotificationButton notificationButton = new NotificationButton();
		accountLayout.addComponent(notificationButton);
		if (AppContext.getSession().getTimezone() == null) {
			EventBus.getInstance().fireEvent(
					new ShellEvent.NewNotification(this,
							new TimezoneNotification()));
		}

		UserAvatarComp userAvatar = new UserAvatarComp();
		accountLayout.addComponent(userAvatar);
		accountLayout.setComponentAlignment(userAvatar, Alignment.MIDDLE_LEFT);

		final PopupButton accountMenu = new PopupButton(AppContext.getSession()
				.getDisplayName());
		final VerticalLayout accLayout = new VerticalLayout();
		accLayout.setWidth("120px");

		final Button myProfileBtn = new Button("Profile",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						accountMenu.setPopupVisible(false);
						EventBus.getInstance().fireEvent(
								new ShellEvent.GotoUserAccountModule(this,
										new String[] { "preview" }));
					}
				});
		myProfileBtn.setStyleName("link");
		accLayout.addComponent(myProfileBtn);

		final Button myAccountBtn = new Button("Account",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						accountMenu.setPopupVisible(false);
						EventBus.getInstance().fireEvent(
								new ShellEvent.GotoUserAccountModule(this,
										new String[] { "billing" }));
					}
				});
		myAccountBtn.setStyleName("link");
		accLayout.addComponent(myAccountBtn);

		final Button userMgtBtn = new Button("User Management",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						accountMenu.setPopupVisible(false);
						EventBus.getInstance().fireEvent(
								new ShellEvent.GotoUserAccountModule(this,
										new String[] { "user", "list" }));
					}
				});
		userMgtBtn.setStyleName("link");
		accLayout.addComponent(userMgtBtn);

		final Button signoutBtn = new Button("Sign out",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						AppContext.getInstance().clearSession();
						EventBus.getInstance().fireEvent(
								new ShellEvent.LogOut(this, null));
					}
				});
		signoutBtn.setStyleName("link");
		accLayout.addComponent(signoutBtn);

		accountMenu.setContent(accLayout);
		accountMenu.setStyleName("accountMenu");
		accountMenu.addStyleName("topNavPopup");
		accountLayout.addComponent(accountMenu);

		layout.addComponent(accountLayout, "accountMenu");

		return layout;
	}

	private static class UserAvatarComp extends CssLayout {
		private static final long serialVersionUID = 1L;

		public UserAvatarComp() {
			addUserAvatar();

			// add listener to listen the change avatar or user information to
			// update top menu
			EventBus.getInstance()
					.addListener(
							new ApplicationEventListener<SessionEvent.UserProfileChangeEvent>() {
								private static final long serialVersionUID = 1L;

								@Override
								public Class<? extends ApplicationEvent> getEventType() {
									return SessionEvent.UserProfileChangeEvent.class;
								}

								@Override
								public void handle(UserProfileChangeEvent event) {
									if ("avatarid".equals(event
											.getFieldChange())) {
										UserAvatarComp.this
												.removeAllComponents();
										addUserAvatar();
									}
								}
							});

		}

		private void addUserAvatar() {
			Image userAvatar = UserAvatarControlFactory
					.createUserAvatarEmbeddedComponent(
							AppContext.getUserAvatarId(), 24);
			this.addComponent(userAvatar);
		}
	}
}
