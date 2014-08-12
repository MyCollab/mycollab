/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.common;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public enum CommentType {
	PRJ_TASK_LIST("Project-TaskList"), PRJ_TASK("Project-Task"), PRJ_BUG(
			"Project-Bug"), PRJ_MESSAGE("Project-Message"), PRJ_MILESTONE(
			"Project-Milestone"), PRJ_RISK("Project-Risk"), PRJ_PROBLEM(
			"Project-Problem"), PRJ_PAGE("Project-Page"), CRM_NOTE("Crm-Note");

	private String type;

	CommentType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}
}
