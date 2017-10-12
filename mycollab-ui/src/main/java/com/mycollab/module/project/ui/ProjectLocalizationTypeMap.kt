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

import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.i18n.*
import com.mycollab.vaadin.UserUIContext

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ProjectLocalizationTypeMap {
    @JvmStatic fun getType(key: String): String {
        return when (key) {
            ProjectTypeConstants.PROJECT -> UserUIContext.getMessage(ProjectI18nEnum.SINGLE)
            ProjectTypeConstants.MESSAGE -> UserUIContext.getMessage(MessageI18nEnum.SINGLE)
            ProjectTypeConstants.MILESTONE -> UserUIContext.getMessage(MilestoneI18nEnum.SINGLE)
            ProjectTypeConstants.TASK -> UserUIContext.getMessage(TaskI18nEnum.SINGLE)
            ProjectTypeConstants.BUG -> UserUIContext.getMessage(BugI18nEnum.SINGLE)
            ProjectTypeConstants.BUG_COMPONENT -> UserUIContext.getMessage(ComponentI18nEnum.SINGLE)
            ProjectTypeConstants.BUG_VERSION -> UserUIContext.getMessage(VersionI18nEnum.SINGLE)
            ProjectTypeConstants.PAGE -> UserUIContext.getMessage(PageI18nEnum.SINGLE)
            ProjectTypeConstants.STANDUP -> UserUIContext.getMessage(StandupI18nEnum.SINGLE)
            ProjectTypeConstants.RISK -> UserUIContext.getMessage(RiskI18nEnum.SINGLE)
            else -> ""
        }
    }
}