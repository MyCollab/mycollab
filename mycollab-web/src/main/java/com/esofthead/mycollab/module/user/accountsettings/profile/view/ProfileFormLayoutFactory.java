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

package com.esofthead.mycollab.module.user.accountsettings.profile.view;

import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.ReadViewLayout;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public abstract class ProfileFormLayoutFactory implements IFormLayoutFactory {

	private final String title;
	protected UserInformationLayout userInformationLayout;
	private Resource userAvatarIcon;

	public ProfileFormLayoutFactory(final String title) {
		this.title = title;
	}

	public void setAvatarLink(String avatarId) {
		userAvatarIcon = UserAvatarControlFactory.createAvatarResource(
				avatarId, 16);
	}

	@Override
	public Layout getLayout() {
		if (userAvatarIcon == null) {
			userAvatarIcon = MyCollabResource
					.newResource("icons/24/project/user.png");
		}
		final ReadViewLayout userAddLayout = new ReadViewLayout(this.title);

		this.userInformationLayout = new UserInformationLayout();
		this.userInformationLayout.getLayout().setWidth("100%");
		userAddLayout.addBody(this.userInformationLayout.getLayout());

		final Layout bottomPanel = this.createBottomPanel();
		if (bottomPanel != null) {
			userAddLayout.addBottomControls(bottomPanel);
		}

		return userAddLayout;
	}

	protected abstract Layout createBottomPanel();

	@Override
	public void attachField(final Object propertyId, final Field<?> field) {
		this.userInformationLayout.attachField(propertyId, field);
	}

	public static class UserInformationLayout implements IFormLayoutFactory {

		private GridFormLayoutHelper basicInformationLayout;
		private GridFormLayoutHelper advancedInformationLayout;
		private GridFormLayoutHelper contactInformationLayout;

		@Override
		public Layout getLayout() {
			final VerticalLayout layout = new VerticalLayout();
			final Label organizationHeader = new Label(
					AppContext
							.getMessage(UserI18nEnum.SECTION_BASIC_INFORMATION));
			organizationHeader.setStyleName("h2");
			layout.addComponent(organizationHeader);

			this.basicInformationLayout = new GridFormLayoutHelper(2, 7,
					"100%", "167px", Alignment.TOP_LEFT);
			this.basicInformationLayout.getLayout().setWidth("100%");
			this.basicInformationLayout.getLayout().setMargin(false);
			this.basicInformationLayout.getLayout().addStyleName(
					UIConstants.COLORED_GRIDLAYOUT);

			layout.addComponent(this.basicInformationLayout.getLayout());

			final Label contactHeader = new Label(
					AppContext
							.getMessage(UserI18nEnum.SECTION_CONTACT_INFORMATION));
			contactHeader.setStyleName("h2");
			layout.addComponent(contactHeader);

			this.contactInformationLayout = new GridFormLayoutHelper(2, 3,
					"100%", "167px", Alignment.TOP_LEFT);
			this.contactInformationLayout.getLayout().setWidth("100%");
			this.contactInformationLayout.getLayout().setMargin(false);
			this.contactInformationLayout.getLayout().addStyleName(
					UIConstants.COLORED_GRIDLAYOUT);

			layout.addComponent(this.contactInformationLayout.getLayout());

			final Label advancedHeader = new Label(
					AppContext
							.getMessage(UserI18nEnum.SECTION_ADVANCED_INFORMATION));
			advancedHeader.setStyleName("h2");
			layout.addComponent(advancedHeader);

			this.advancedInformationLayout = new GridFormLayoutHelper(2, 2,
					"100%", "167px", Alignment.TOP_LEFT);
			this.advancedInformationLayout.getLayout().setWidth("100%");
			this.advancedInformationLayout.getLayout().setMargin(false);
			this.advancedInformationLayout.getLayout().addStyleName(
					UIConstants.COLORED_GRIDLAYOUT);

			layout.addComponent(this.advancedInformationLayout.getLayout());
			return layout;
		}

		@Override
		public void attachField(final Object propertyId, final Field<?> field) {
			if (propertyId.equals("firstname")) {
				this.basicInformationLayout.addComponent(field,
						AppContext.getMessage(UserI18nEnum.FORM_FIRST_NAME), 0,
						0);
			} else if (propertyId.equals("lastname")) {
				this.basicInformationLayout.addComponent(field,
						AppContext.getMessage(UserI18nEnum.FORM_LAST_NAME), 0,
						1);
			} else if (propertyId.equals("nickname")) {
				this.basicInformationLayout.addComponent(field,
						AppContext.getMessage(UserI18nEnum.FORM_NICK_NAME), 1,
						0);
			} else if (propertyId.equals("dateofbirth")) {
				this.basicInformationLayout
						.addComponent(field, AppContext
								.getMessage(UserI18nEnum.FORM_BIRTHDAY), 1, 1);
			} else if (propertyId.equals("email")) {
				this.basicInformationLayout.addComponent(field,
						AppContext.getMessage(UserI18nEnum.FORM_EMAIL), 0, 2);
			} else if (propertyId.equals("timezone")) {
				this.basicInformationLayout.addComponent(field,
						AppContext.getMessage(UserI18nEnum.FORM_TIMEZONE), 0,
						3, 2, "262px", Alignment.MIDDLE_LEFT);
			} else if (propertyId.equals("roleid")) {
				this.basicInformationLayout.addComponent(field,
						AppContext.getMessage(UserI18nEnum.FORM_ROLE), 1, 2);
			} else if (propertyId.equals("company")) {
				this.advancedInformationLayout.addComponent(field,
						AppContext.getMessage(UserI18nEnum.FORM_COMPANY), 0, 0);
			} else if (propertyId.equals("country")) {
				this.advancedInformationLayout.addComponent(field,
						AppContext.getMessage(UserI18nEnum.FORM_COUNTRY), 0, 1,
						2, "262px", Alignment.MIDDLE_LEFT);
			} else if (propertyId.equals("website")) {
				this.advancedInformationLayout.addComponent(field,
						AppContext.getMessage(UserI18nEnum.FORM_WEBSITE), 1, 0);
			} else if (propertyId.equals("workphone")) {
				this.contactInformationLayout.addComponent(field,
						AppContext.getMessage(UserI18nEnum.FORM_WORK_PHONE), 0,
						0);
			} else if (propertyId.equals("homephone")) {
				this.contactInformationLayout.addComponent(field,
						AppContext.getMessage(UserI18nEnum.FORM_HOME_PHONE), 0,
						1);
			} else if (propertyId.equals("facebookaccount")) {
				this.contactInformationLayout.addComponent(field, "Facebook",
						1, 0);
			} else if (propertyId.equals("twitteraccount")) {
				this.contactInformationLayout.addComponent(field, "Twitter", 1,
						1);
			} else if (propertyId.equals("skypecontact")) {
				this.contactInformationLayout.addComponent(field, "Skype", 0,
						2, 2, "262px", Alignment.MIDDLE_LEFT);
			}
		}
	}
}
