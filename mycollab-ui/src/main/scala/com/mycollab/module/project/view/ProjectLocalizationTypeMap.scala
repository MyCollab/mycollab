/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view

import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.i18n._
import com.mycollab.vaadin.UserUIContext

/**
  * @author MyCollab Ltd
  * @since 5.3.1
  */
object ProjectLocalizationTypeMap {
  def getType(key: String): String = {
    key match {
      case ProjectTypeConstants.PROJECT => UserUIContext.getMessage(ProjectI18nEnum.SINGLE)
      case ProjectTypeConstants.MESSAGE => UserUIContext.getMessage(MessageI18nEnum.SINGLE)
      case ProjectTypeConstants.MILESTONE => UserUIContext.getMessage(MilestoneI18nEnum.SINGLE)
      case ProjectTypeConstants.TASK => UserUIContext.getMessage(TaskI18nEnum.SINGLE)
      case ProjectTypeConstants.BUG => UserUIContext.getMessage(BugI18nEnum.SINGLE)
      case ProjectTypeConstants.BUG_COMPONENT => UserUIContext.getMessage(ComponentI18nEnum.SINGLE)
      case ProjectTypeConstants.BUG_VERSION => UserUIContext.getMessage(VersionI18nEnum.SINGLE)
      case ProjectTypeConstants.PAGE => UserUIContext.getMessage(PageI18nEnum.SINGLE)
      case ProjectTypeConstants.STANDUP => UserUIContext.getMessage(StandupI18nEnum.SINGLE)
      case ProjectTypeConstants.RISK => UserUIContext.getMessage(RiskI18nEnum.SINGLE)
      case _ => ""
    }
  }
}
