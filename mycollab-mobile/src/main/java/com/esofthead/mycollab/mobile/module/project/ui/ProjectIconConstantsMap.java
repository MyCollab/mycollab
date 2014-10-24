/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.ui;

import java.util.HashMap;
import java.util.Map;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.2
 */
public class ProjectIconConstantsMap {

	private static Map<String, String> constantsMap;

	static {
		constantsMap = new HashMap<String, String>();
		constantsMap.put(ProjectTypeConstants.BUG, IconConstants.PROJECT_BUG);
		constantsMap.put(ProjectTypeConstants.TASK, IconConstants.PROJECT_TASK);
		constantsMap.put(ProjectTypeConstants.TASK_LIST,
				IconConstants.PROJECT_TASK);
		constantsMap.put(ProjectTypeConstants.MILESTONE,
				IconConstants.PROJECT_MILESTONE);
		constantsMap.put(ProjectTypeConstants.MESSAGE,
				IconConstants.PROJECT_MESSAGE);
		constantsMap.put(ProjectTypeConstants.PROJECT,
				IconConstants.PROJECT_DASHBOARD);
	}

	public static String getIcon(String type) {
		String result = constantsMap.get(type);
		if (result == null) {
			throw new MyCollabException("Can not get key: " + type);
		}

		return result;
	}

}
