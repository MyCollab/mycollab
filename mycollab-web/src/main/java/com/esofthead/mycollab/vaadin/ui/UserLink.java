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

package com.esofthead.mycollab.vaadin.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.module.project.LabelLink;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class UserLink extends Button {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(UserLink.class);

	public UserLink(final String username, String userAvatarId,
			final String displayName, boolean useWordWrap) {
		super(displayName);
		this.addStyleName("link");

		if (username != null && !username.equals("")) {
			this.setIcon(UserAvatarControlFactory.createAvatarResource(
					userAvatarId, 16));
		}

		if (useWordWrap) {
			this.addStyleName(UIConstants.WORD_WRAP);
		}
		this.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				UserService userService = ApplicationContextUtil
						.getSpringBean(UserService.class);
				SimpleUser user = userService.findUserByUserNameInAccount(
						username, AppContext.getAccountId());
				try {
					UI.getCurrent().addWindow(new UserQuickPreviewWindow(user));
				} catch (Exception e) {
					log.error(
							"Error while try to show user information window",
							e);
				}
			}
		});
	}

	public UserLink(final String username, String userAvatarId,
			final String displayName) {
		this(username, userAvatarId, displayName, true);
	}

	public class UserQuickPreviewWindow extends Window {
		private static final long serialVersionUID = 1L;
		private SimpleUser user;

		public UserQuickPreviewWindow(SimpleUser user) {
			super("User preview");
			this.center();
			this.setWidth("500px");
			this.setResizable(false);
			this.addStyleName("user-preview-window");
			this.user = user;
			constructBody();
		}

		private void constructBody() {
			VerticalLayout layout = new VerticalLayout();
			layout.setSpacing(true);
			layout.setMargin(true);

			HorizontalLayout topLayout = new HorizontalLayout();
			layout.addComponent(topLayout);

			// ---------define top layout
			topLayout.setSpacing(true);
			topLayout.addComponent(new Label("View full profile at: "));

			String userFullLinkStr = AccountLinkGenerator
					.generatePreviewFullUserLink(AppContext.getSiteUrl(),
							user.getUsername());
			LabelLink userFullLinkBtn = new LabelLink(userFullLinkStr,
					userFullLinkStr);

			userFullLinkBtn.setWidth("100%");
			topLayout.addComponent(userFullLinkBtn);
			topLayout.setExpandRatio(userFullLinkBtn, 1.0f);
			// -----------------------------------
			CssLayout mainBodyWapper = new CssLayout();
			mainBodyWapper.addStyleName("border-box2-color");
			mainBodyWapper.setSizeFull();

			layout.addComponent(mainBodyWapper);
			VerticalLayout bodyLayout = new VerticalLayout();
			mainBodyWapper.addComponent(bodyLayout);
			HorizontalLayout infoHorizontalWapper = new HorizontalLayout();
			VerticalLayout mainUserInfoLayout = new VerticalLayout();
			VerticalLayout userImageLayout = new VerticalLayout();
			infoHorizontalWapper.addComponent(mainUserInfoLayout);
			infoHorizontalWapper.addComponent(userImageLayout);
			bodyLayout.addComponent(infoHorizontalWapper);

			// Construct mainUserInfoLayout ------------------
			mainUserInfoLayout.setWidth("360px");
			mainUserInfoLayout.setSpacing(true);
			mainUserInfoLayout.setMargin(true);

			Label userNameLbl = new Label(user.getDisplayName());
			userNameLbl.addStyleName("h2");
			mainUserInfoLayout.addComponent(userNameLbl);

			HorizontalLayout emailLayout = new HorizontalLayout();
			Label emailTitle = new Label("Email");
			emailTitle.setWidth("120px");
			emailLayout.addComponent(emailTitle);

			Label emailLink = new Label("<a href=\"mailto:" + user.getEmail()
					+ "\">" + user.getEmail() + "</a>", ContentMode.HTML);
			emailLayout.addComponent(emailLink);
			mainUserInfoLayout.addComponent(emailLayout);

			HorizontalLayout timeZoneLayout = new HorizontalLayout();
			Label timeLabel = new Label("Time");
			timeLabel.setWidth("120px");
			timeZoneLayout.addComponent(timeLabel);
			timeZoneLayout.addComponent(new Label(TimezoneMapper.getTimezone(
					user.getTimezone()).getDisplayName()));
			mainUserInfoLayout.addComponent(timeZoneLayout);

			HorizontalLayout countryLayout = new HorizontalLayout();
			Label countryTitle = new Label("Country");
			countryTitle.setWidth("120px");
			countryLayout.addComponent(countryTitle);
			countryLayout.addComponent(new Label(user.getCountry()));
			mainUserInfoLayout.addComponent(countryLayout);

			HorizontalLayout phoneLayout = new HorizontalLayout();
			Label phoneLbl = new Label("Phone");
			phoneLbl.setWidth("120px");
			phoneLayout.addComponent(phoneLbl);
			phoneLayout.addComponent(new Label(user.getWorkphone()));
			mainUserInfoLayout.addComponent(phoneLayout);

			HorizontalLayout lastAccessTimeLayout = new HorizontalLayout();
			Label lastAccessTimeTitle = new Label("Last access time");
			lastAccessTimeTitle.setWidth("120px");
			lastAccessTimeLayout.addComponent(lastAccessTimeTitle);
			Label lastAccessTimeLabel = new Label(
					DateTimeUtils.getStringDateFromNow(
							user.getLastaccessedtime(),
							AppContext.getUserLocale()));
			lastAccessTimeLayout.addComponent(lastAccessTimeLabel);
			mainUserInfoLayout.addComponent(lastAccessTimeLayout);

			// Construct userImageLayout ---------------------
			Embedded embeedIcon = new Embedded("",
					UserAvatarControlFactory.createAvatarResource(
							user.getAvatarid(), 100));
			userImageLayout.addComponent(embeedIcon);
			this.setContent(layout);
		}
	}
}
