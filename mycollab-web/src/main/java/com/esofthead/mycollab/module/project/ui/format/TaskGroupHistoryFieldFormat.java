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
package com.esofthead.mycollab.module.project.ui.format;

import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.vaadin.ui.HistoryFieldFormat;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class TaskGroupHistoryFieldFormat implements HistoryFieldFormat {

	@Override
	public Component toVaadinComponent(String value) {
		try {
			int taskgroupId = Integer.parseInt(value);
			String html = ProjectLinkBuilder
					.generateTaskGroupHtmlLink(taskgroupId);
			return (value != null) ? new Label(html, ContentMode.HTML)
					: new Label("");
		} catch (NumberFormatException e) {
			return new Label("");
		}
	}

	@Override
	public String toString(String value) {
		return value;
	}

}
