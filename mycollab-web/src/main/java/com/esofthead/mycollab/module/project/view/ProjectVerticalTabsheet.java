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

import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.VerticalTabsheet;
import com.vaadin.ui.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class ProjectVerticalTabsheet extends VerticalTabsheet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void setDefaulButtonIcon(Component btn, Boolean selected) {
		ButtonTabImpl btnTabImpl = (ButtonTabImpl) btn;
		String tabId = btnTabImpl.getTabId();
		String suffix = (selected) ? "_selected" : "";

		switch (tabId) {
		case "dashboard":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/project/dashboard" + suffix + ".png"));
			break;

		case "message":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/message"
					+ suffix + ".png"));
			break;
		case "milestone":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/phase"
					+ suffix + ".png"));
			break;

		case "task":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/task"
					+ suffix + ".png"));
			break;

		case "bug":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/bug"
					+ suffix + ".png"));
			break;

		case "file":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/file"
					+ suffix + ".png"));
			break;
		case "risk":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/risk"
					+ suffix + ".png"));
			break;
		case "problem":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/problem"
					+ suffix + ".png"));
			break;
		case "time":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/time"
					+ suffix + ".png"));
			break;
		case "standup":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/standup"
					+ suffix + ".png"));
			break;
		case "member":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/user"
					+ suffix + ".png"));
			break;
		default:

		}

	}

}
