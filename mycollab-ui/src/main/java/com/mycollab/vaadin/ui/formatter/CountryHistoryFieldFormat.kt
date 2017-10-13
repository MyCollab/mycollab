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
package com.mycollab.vaadin.ui.formatter

import com.mycollab.core.utils.StringUtils
import com.mycollab.vaadin.UserUIContext
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 5.4.2
 */
class CountryHistoryFieldFormat : HistoryFieldFormat {
    override fun toString(countryCode: String): String =
            when {
                StringUtils.isNotBlank(countryCode) -> {
                    val obj = Locale("", countryCode)
                    obj.getDisplayCountry(UserUIContext.getUserLocale())
                }
                else -> ""
            }

    override fun toString(value: String, displayAsHtml: Boolean, msgIfBlank: String): String = toString(value)
}
