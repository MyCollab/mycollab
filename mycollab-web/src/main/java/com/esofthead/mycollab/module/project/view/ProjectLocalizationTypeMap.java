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

import java.util.HashMap;
import java.util.Map;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.ProjectTypeI18nEnum;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectLocalizationTypeMap {
	private static Map<String, ProjectTypeI18nEnum> typeMap;

	static {
		typeMap = new HashMap<>();
		typeMap.put(ProjectTypeConstants.PROJECT, ProjectTypeI18nEnum.PROJECT_ITEM);
		typeMap.put(ProjectTypeConstants.MESSAGE, ProjectTypeI18nEnum.MESSAGE_ITEM);
		typeMap.put(ProjectTypeConstants.MILESTONE, ProjectTypeI18nEnum.PHASE_ITEM);
		typeMap.put(ProjectTypeConstants.TASK, ProjectTypeI18nEnum.TASK_ITEM);
		typeMap.put(ProjectTypeConstants.TASK_LIST, ProjectTypeI18nEnum.TASKGROUP_ITEM);
		typeMap.put(ProjectTypeConstants.BUG, ProjectTypeI18nEnum.BUG_ITEM);
		typeMap.put(ProjectTypeConstants.BUG_COMPONENT, ProjectTypeI18nEnum.COMPONENT_ITEM);
		typeMap.put(ProjectTypeConstants.BUG_VERSION, ProjectTypeI18nEnum.VERSION_ITEM);
		typeMap.put(ProjectTypeConstants.PAGE, ProjectTypeI18nEnum.PAGE_ITEM);
		typeMap.put(ProjectTypeConstants.STANDUP, ProjectTypeI18nEnum.STANDUP_ITEM);
		typeMap.put(ProjectTypeConstants.PROBLEM, ProjectTypeI18nEnum.PROBLEM_ITEM);
		typeMap.put(ProjectTypeConstants.RISK, ProjectTypeI18nEnum.RISK_ITEM);
	}

	public static ProjectTypeI18nEnum getType(String key) {
		ProjectTypeI18nEnum result = typeMap.get(key);
		if (result == null) {
			throw new MyCollabException("Can not get key: " + key);
		}

		return result;
	}
}
