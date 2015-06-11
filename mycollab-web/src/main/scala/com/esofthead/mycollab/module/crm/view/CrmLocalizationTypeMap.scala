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

import com.esofthead.mycollab.core.MyCollabException
import com.esofthead.mycollab.module.crm.CrmTypeConstants
import com.esofthead.mycollab.module.crm.i18n.CrmTypeI18nEnum

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
object CrmLocalizationTypeMap {
    private val typeMap: Map[String, CrmTypeI18nEnum] = Map(
        CrmTypeConstants.ACCOUNT -> CrmTypeI18nEnum.ACCOUNT,
        CrmTypeConstants.CALL -> CrmTypeI18nEnum.CALL,
        CrmTypeConstants.CAMPAIGN -> CrmTypeI18nEnum.CAMPAIGN,
        CrmTypeConstants.CASE -> CrmTypeI18nEnum.CASES,
        CrmTypeConstants.CONTACT -> CrmTypeI18nEnum.CONTACT,
        CrmTypeConstants.LEAD -> CrmTypeI18nEnum.LEAD,
        CrmTypeConstants.MEETING -> CrmTypeI18nEnum.MEETING,
        CrmTypeConstants.OPPORTUNITY -> CrmTypeI18nEnum.OPPORTUNITY,
        CrmTypeConstants.TASK -> CrmTypeI18nEnum.TASK)
    typeMap.withDefaultValue(null)

    def getType(key: String): CrmTypeI18nEnum = {
        val result: CrmTypeI18nEnum = typeMap(key)
        if (result == null) {
            throw new MyCollabException("CAN NOT GET VALUE FOR KEY: " + key)
        }
        result
    }
}
