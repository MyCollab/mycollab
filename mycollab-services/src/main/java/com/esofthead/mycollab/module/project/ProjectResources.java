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
package com.esofthead.mycollab.module.project;

import com.esofthead.mycollab.configuration.MyCollabAssets;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugPriority;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectResources {
    private static Logger LOG = LoggerFactory.getLogger(ProjectResources.class);
    private static Method getResMethod;

    public static final String T_PRIORITY_HIGHT_IMG = "icons/12/priority_high.png";
    public static final String T_PRIORITY_LOW_IMG = "icons/12/priority_low.png";
    public static final String T_PRIORITY_MEDIUM_IMG = "icons/12/priority_medium.png";
    public static final String T_PRIORITY_NONE_IMG = "icons/12/priority_none.png";
    public static final String T_PRIORITY_URGENT_IMG = "icons/12/priority_urgent.png";

    public static final String B_PRIORITY_BLOCKER_IMG_12 = "icons/12/priority_urgent.png";
    public static final String B_PRIORITY_CRITICAL_IMG_12 = "icons/12/priority_high.png";
    public static final String B_PRIORITY_MAJOR_IMG_12 = "icons/12/priority_medium.png";
    public static final String B_PRIORITY_MINOR_IMG_12 = "icons/12/priority_low.png";
    public static final String B_PRIORITY_TRIVIAL_IMG_12 = "icons/12/priority_none.png";

    public static final String B_SEVERITY_CRITICAL_IMG_12 = "icons/12/severity_critical.png";
    public static final String B_SEVERITY_MAJOR_IMG_12 = "icons/12/severity_major.png";
    public static final String B_SEVERITY_MINOR_IMG_12 = "icons/12/severity_minor.png";
    public static final String B_SEVERITY_TRIVIAL_IMG_12 = "icons/12/severity_trivial.png";

    static {
        try {
            Class<?> resourceCls = Class.forName("com.esofthead.mycollab.module.project.ui.ProjectAssetsManager");
            getResMethod = resourceCls.getMethod("toHexString", String.class);
        } catch (Exception e) {
            throw new MyCollabException("Can not reload resource", e);
        }
    }

    public static String getFontIconHtml(String type) {
        try {
            String codePoint = (String) getResMethod.invoke(null, type);
            return String.format("<span class=\"v-icon\" style=\"font-family: FontAwesome;\">%s;</span>", codePoint);
        } catch (Exception e) {
            LOG.error("Can not get resource type {}", type);
            return "";
        }
    }
}
