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
package com.esofthead.mycollab.module.file;

public enum AttachmentType {
	CRM_NOTE_TYPE("crm-note"),
	PROJECT_MESSAGE("project-message"),
	PROJECT_BUG_TYPE("project-bug"),
	PROJECT_COMPONENT("project-component"),
	PROJECT_VERSION("project-version"),
	PROJECT_MILESTONE("project-milestone"),
	PROJECT_RISK("project-risk"),
	PROJECT_PROBLEM("project-problem"),
	PROJECT_TASKLIST("project-tasklist"),
	PROJECT_PAGE("project-page"),
	PROJECT_TASK_TYPE("project-task"),
	COMMON_COMMENT("common-comment");

	private String type;

	AttachmentType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}

}
