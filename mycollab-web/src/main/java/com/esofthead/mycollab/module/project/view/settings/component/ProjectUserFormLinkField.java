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
package com.esofthead.mycollab.module.project.view.settings.component;

import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectUserFormLinkField extends CustomField {
	private static final long serialVersionUID = 1L;

	private String username;
	private String userAvatarId;
	private String displayName;

	public ProjectUserFormLinkField(String username, String userAvatarId,
			String displayName) {
		this.username = username;
		this.userAvatarId = userAvatarId;
		this.displayName = displayName;
	}

	@Override
	public Class<?> getType() {
		return Object.class;
	}

	@Override
	protected Component initContent() {
		ProjectUserLink projectLink = new ProjectUserLink(username,
				userAvatarId, displayName);
		projectLink.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
		return projectLink;
	}
}
