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
package com.esofthead.mycollab.module.user.ui.components;

import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.VerticalTabsheet;
import com.vaadin.ui.Button;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class UserVerticalTabsheet extends VerticalTabsheet {
	private static final long serialVersionUID = -9095044309853738791L;

	@Override
	protected void setDefaulButtonIcon(Button btn, Boolean selected) {
		ButtonTabImpl btnTabImpl = (ButtonTabImpl) btn;
		String tabId = btnTabImpl.getTabId();
		String suffix;
		if (selected == true)
			suffix = "_selected";
		else
			suffix = "";

		switch (tabId) {
		case "profile":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/user/menu_profile.png"));
			break;
		case "users":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/user/menu_team.png"));
			break;
		case "billing":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/user/menu_account.png"));
			break;
		case "theme":
			btn.setIcon(MyCollabResource.newResource("icons/22/user/menu_theme"
					+ suffix + ".png"));
			break;
		default:
		}
	}
}
