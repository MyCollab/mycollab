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

import com.esofthead.mycollab.module.project.ProjectTypeConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class FontAwesomeUtils {
    private static final Map<String, String> resources;

    static {
        resources = new HashMap<>();
        resources.put(ProjectTypeConstants.BUG, Integer.toHexString('\uf188'));
        resources.put(ProjectTypeConstants.TASK, Integer.toHexString('\uf0ae'));
        resources.put(ProjectTypeConstants.RISK, Integer.toHexString('\uf132'));
        resources.put(ProjectTypeConstants.MILESTONE, Integer.toHexString('\uf11e'));
        resources.put(ProjectTypeConstants.PROJECT, Integer.toHexString('\uf133'));
    }

    public static String toHtml(String resId) {
        return "<span class=\"v-icon\" style=\"font-family: FontAwesome;\">&#x" + resources.get(resId) + ";" + "</span>";
    }
}
