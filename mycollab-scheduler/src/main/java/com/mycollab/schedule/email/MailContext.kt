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
package com.mycollab.schedule.email

import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.core.utils.TimezoneVal
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.user.domain.SimpleUser

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class MailContext<B>(val emailNotification: SimpleRelayEmailNotification,
                     val user: SimpleUser, val siteUrl: String) {
    val locale = LocalizationHelper.getLocaleInstance(user.language)
    val timeZone = TimezoneVal.valueOf(user.timezone)
    var wrappedBean: B? = null

    val saccountid
        get() = emailNotification.saccountid

    val changeByUserFullName
        get() = emailNotification.changeByUserFullName

    val typeid
        get() = emailNotification.typeid

    val type
        get() = emailNotification.type

    fun getMessage(key: Enum<*>, vararg params: String?) = LocalizationHelper.getMessage(locale, key, *params)

    fun getFieldName(fieldMapper: ItemFieldMapper, fieldName: String): String {
        val fieldFormat = fieldMapper.getField(fieldName)
        return if (fieldFormat != null) getMessage(fieldFormat.displayName) else ""
    }
}