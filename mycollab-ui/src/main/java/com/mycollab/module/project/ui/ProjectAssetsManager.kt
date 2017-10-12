/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.ui

import com.mycollab.core.utils.StringUtils
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority
import com.vaadin.server.FontAwesome

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
object ProjectAssetsManager {
    private val resources = mutableMapOf(ProjectTypeConstants.DASHBOARD to FontAwesome.DASHBOARD,
            ProjectTypeConstants.MESSAGE to FontAwesome.COMMENT,
            ProjectTypeConstants.MILESTONE to FontAwesome.FLAG_CHECKERED,
            ProjectTypeConstants.TASK to FontAwesome.TASKS,
            ProjectTypeConstants.TICKET to FontAwesome.TICKET,
            ProjectTypeConstants.PAGE to FontAwesome.FILE,
            ProjectTypeConstants.BUG to FontAwesome.BUG,
            ProjectTypeConstants.BUG_COMPONENT to FontAwesome.CUBE,
            ProjectTypeConstants.BUG_VERSION to FontAwesome.LEAF,
            ProjectTypeConstants.FILE to FontAwesome.BRIEFCASE,
            ProjectTypeConstants.RISK to FontAwesome.SHIELD,
            ProjectTypeConstants.FINANCE to FontAwesome.MONEY,
            ProjectTypeConstants.TIME to FontAwesome.CLOCK_O,
            ProjectTypeConstants.INVOICE to FontAwesome.CREDIT_CARD,
            ProjectTypeConstants.STANDUP to FontAwesome.CUBES,
            ProjectTypeConstants.MEMBER to FontAwesome.USERS,
            ProjectTypeConstants.PROJECT to FontAwesome.CALENDAR_O
    )

    @JvmStatic fun getAsset(resId: String): FontAwesome = resources[resId] ?: FontAwesome.DASHBOARD

    @JvmStatic fun toHexString(resId: String): String ="&#x" + Integer.toHexString(resources[resId]!!.getCodepoint())

    @JvmStatic fun getPriority(priority: String?): FontAwesome {
        return if (Priority.Urgent.name == priority || Priority.High.name == priority || Priority.Medium.name == priority || priority == null) {
            FontAwesome.ARROW_UP
        } else {
            FontAwesome.ARROW_DOWN
        }
    }

    @JvmStatic fun getMilestoneStatus(status: String): FontAwesome {
        return when (status) {
            MilestoneStatus.Closed.name -> FontAwesome.MINUS_CIRCLE
            MilestoneStatus.InProgress.name -> FontAwesome.CLOCK_O
            else -> FontAwesome.SPINNER
        }
    }

    @JvmStatic fun getPriorityHtml(priority: String): String {
        var temp = priority
        if (StringUtils.isBlank(temp)) {
            temp = Priority.Medium.name
        }
        val fontAwesome = getPriority(temp)
        return String.format("<span class=\"priority-%s v-icon\" style=\"font-family: FontAwesome;\">&#x%s;</span>",
                temp.toLowerCase(), Integer.toHexString(fontAwesome.codepoint))
    }
}
