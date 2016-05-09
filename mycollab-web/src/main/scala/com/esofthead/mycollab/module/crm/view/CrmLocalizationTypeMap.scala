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
package com.esofthead.mycollab.module.crm.view

import com.esofthead.mycollab.module.crm.CrmTypeConstants
import com.esofthead.mycollab.module.crm.i18n._
import com.esofthead.mycollab.vaadin.AppContext

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
object CrmLocalizationTypeMap {

  def getType(key: String): String = {
    key match {
      case CrmTypeConstants.ACCOUNT => AppContext.getMessage(AccountI18nEnum.SINGLE)
      case CrmTypeConstants.CALL => AppContext.getMessage(CallI18nEnum.SINGLE)
      case CrmTypeConstants.CAMPAIGN => AppContext.getMessage(CampaignI18nEnum.SINGLE)
      case CrmTypeConstants.CASE => AppContext.getMessage(CaseI18nEnum.SINGLE)
      case CrmTypeConstants.CONTACT => AppContext.getMessage(ContactI18nEnum.SINGLE)
      case CrmTypeConstants.LEAD => AppContext.getMessage(LeadI18nEnum.SINGLE)
      case CrmTypeConstants.MEETING => AppContext.getMessage(MeetingI18nEnum.SINGLE)
      case CrmTypeConstants.OPPORTUNITY => AppContext.getMessage(OpportunityI18nEnum.SINGLE)
      case CrmTypeConstants.TASK => AppContext.getMessage(TaskI18nEnum.SINGLE)
      case _ => ""
    }
  }
}
