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
package com.mycollab.module.crm.ui

import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.i18n.*
import com.mycollab.vaadin.UserUIContext

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object CrmLocalizationTypeMap {
    @JvmStatic fun getType(key: String): String = when (key) {
        CrmTypeConstants.ACCOUNT -> UserUIContext.getMessage(AccountI18nEnum.SINGLE)
        CrmTypeConstants.CALL -> UserUIContext.getMessage(CallI18nEnum.SINGLE)
        CrmTypeConstants.CAMPAIGN -> UserUIContext.getMessage(CampaignI18nEnum.SINGLE)
        CrmTypeConstants.CASE -> UserUIContext.getMessage(CaseI18nEnum.SINGLE)
        CrmTypeConstants.CONTACT -> UserUIContext.getMessage(ContactI18nEnum.SINGLE)
        CrmTypeConstants.LEAD -> UserUIContext.getMessage(LeadI18nEnum.SINGLE)
        CrmTypeConstants.MEETING -> UserUIContext.getMessage(MeetingI18nEnum.SINGLE)
        CrmTypeConstants.OPPORTUNITY -> UserUIContext.getMessage(OpportunityI18nEnum.SINGLE)
        CrmTypeConstants.TASK -> UserUIContext.getMessage(TaskI18nEnum.SINGLE)
        else -> ""
    }
}