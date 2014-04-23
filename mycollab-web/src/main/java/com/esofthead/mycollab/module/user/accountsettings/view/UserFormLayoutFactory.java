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

package com.esofthead.mycollab.module.user.accountsettings.view;

import com.esofthead.mycollab.vaadin.ui.AddViewLayout;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
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
public abstract class UserFormLayoutFactory implements IFormLayoutFactory {

	private final String title;
	private UserInformationLayout userInformationLayout;
	private Resource userAvatarIcon;

	public UserFormLayoutFactory(final String title) {
		this.title = title;
	}

	public void setAvatarLink(String avatarId) {
		userAvatarIcon = UserAvatarControlFactory.createAvatarResource(
				avatarId, 48);
	}

	@Override
	public Layout getLayout() {
		if (userAvatarIcon == null) {
			userAvatarIcon = MyCollabResource
					.newResource("icons/48/user/user.png");
		}
		final AddViewLayout userAddLayout = new AddViewLayout(this.title,
				userAvatarIcon);

		final Layout topPanel = this.createTopPanel();
		if (topPanel != null) {
			userAddLayout.addHeaderRight(topPanel);
		}

		this.userInformationLayout = new UserInformationLayout();
		this.userInformationLayout.getLayout().setWidth("100%");
		userAddLayout.addBody(this.userInformationLayout.getLayout());

		final Layout bottomPanel = this.createBottomPanel();
		if (bottomPanel != null) {
			userAddLayout.addBottomControls(bottomPanel);
		}

		return userAddLayout;
	}

	protected abstract Layout createTopPanel();

	protected abstract Layout createBottomPanel();

	@Override
	public boolean attachField(final Object propertyId, final Field<?> field) {
		return this.userInformationLayout.attachField(propertyId, field);
	}

	public static class UserInformationLayout implements IFormLayoutFactory {

		private GridFormLayoutHelper informationLayout;

		@Override
		public Layout getLayout() {
			final VerticalLayout layout = new VerticalLayout();
			final Label organizationHeader = new Label("User Information");
			organizationHeader.setStyleName("h2");
			layout.addComponent(organizationHeader);

			this.informationLayout = new GridFormLayoutHelper(2, 6, "100%",
					"167px", Alignment.TOP_LEFT);
			this.informationLayout.getLayout().setWidth("100%");
			this.informationLayout.getLayout().setMargin(false);
			this.informationLayout.getLayout().addStyleName(
					UIConstants.COLORED_GRIDLAYOUT);

			layout.addComponent(this.informationLayout.getLayout());
			return layout;
		}

		@Override
		public boolean attachField(final Object propertyId, final Field<?> field) {
			if (propertyId.equals("firstname")) {
				this.informationLayout.addComponent(field, "First Name", 0, 0);
			} else if (propertyId.equals("lastname")) {
				this.informationLayout.addComponent(field, "Last Name", 0, 1);
			} else if (propertyId.equals("nickname")) {
				this.informationLayout.addComponent(field, "Nick Name", 1, 0);
			} else if (propertyId.equals("dateofbirth")) {
				this.informationLayout.addComponent(field, "Birthday", 1, 1);
			} else if (propertyId.equals("email")) {
				this.informationLayout.addComponent(field, "Email", 0, 2);
			} else if (propertyId.equals("isadmin")) {
				this.informationLayout.addComponent(field, "Role", 1, 2);
			} else if (propertyId.equals("company")) {
				this.informationLayout.addComponent(field, "Company", 0, 3);
			} else if (propertyId.equals("website")) {
				this.informationLayout.addComponent(field, "Website", 1, 3);
			} else {
				return false;
			}

			return true;
		}
	}
}
