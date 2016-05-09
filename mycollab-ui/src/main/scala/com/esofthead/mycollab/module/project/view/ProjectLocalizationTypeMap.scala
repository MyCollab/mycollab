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
package com.esofthead.mycollab.module.project.view

import com.esofthead.mycollab.module.project.ProjectTypeConstants
import com.esofthead.mycollab.module.project.i18n._
import com.esofthead.mycollab.vaadin.AppContext

/**
  * @author MyCollab Ltd
  * @since 5.3.1
  */
object ProjectLocalizationTypeMap {
  def getType(key: String): String = {
    key match {
      case ProjectTypeConstants.PROJECT => AppContext.getMessage(ProjectI18nEnum.SINGLE)
      case ProjectTypeConstants.MESSAGE => AppContext.getMessage(MessageI18nEnum.SINGLE)
      case ProjectTypeConstants.MILESTONE => AppContext.getMessage(MilestoneI18nEnum.SINGLE)
      case ProjectTypeConstants.TASK => AppContext.getMessage(TaskI18nEnum.SINGLE)
      case ProjectTypeConstants.BUG => AppContext.getMessage(BugI18nEnum.SINGLE)
      case ProjectTypeConstants.BUG_COMPONENT => AppContext.getMessage(ComponentI18nEnum.SINGLE)
      case ProjectTypeConstants.BUG_VERSION => AppContext.getMessage(VersionI18nEnum.SINGLE)
      case ProjectTypeConstants.PAGE => AppContext.getMessage(PageI18nEnum.SINGLE)
      case ProjectTypeConstants.STANDUP => AppContext.getMessage(StandupI18nEnum.SINGLE)
      case ProjectTypeConstants.RISK => AppContext.getMessage(RiskI18nEnum.SINGLE)
      case _ => ""
    }
  }
}
