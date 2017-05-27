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
package com.mycollab.module.crm.view

import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.i18n._
import com.mycollab.vaadin.UserUIContext

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
object CrmLocalizationTypeMap {

  def getType(key: String): String = {
    key match {
      case CrmTypeConstants.ACCOUNT => UserUIContext.getMessage(AccountI18nEnum.SINGLE)
      case CrmTypeConstants.CALL => UserUIContext.getMessage(CallI18nEnum.SINGLE)
      case CrmTypeConstants.CAMPAIGN => UserUIContext.getMessage(CampaignI18nEnum.SINGLE)
      case CrmTypeConstants.CASE => UserUIContext.getMessage(CaseI18nEnum.SINGLE)
      case CrmTypeConstants.CONTACT => UserUIContext.getMessage(ContactI18nEnum.SINGLE)
      case CrmTypeConstants.LEAD => UserUIContext.getMessage(LeadI18nEnum.SINGLE)
      case CrmTypeConstants.MEETING => UserUIContext.getMessage(MeetingI18nEnum.SINGLE)
      case CrmTypeConstants.OPPORTUNITY => UserUIContext.getMessage(OpportunityI18nEnum.SINGLE)
      case CrmTypeConstants.TASK => UserUIContext.getMessage(TaskI18nEnum.SINGLE)
      case _ => ""
    }
  }
}
