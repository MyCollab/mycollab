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

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.utils.StringUtils
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.vaadin.UserUIContext

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
class LocalizationHistoryFieldFormat(private val enumCls: Class<out Enum<*>>) : HistoryFieldFormat {

    override fun toString(value: String) =
            toString(UserUIContext.getUser(), value, true, UserUIContext.getMessage(GenericI18Enum.FORM_EMPTY))

    override fun toString(currentViewUser: SimpleUser, value: String, displayAsHtml: Boolean, msgIfBlank: String) =
            when {
                StringUtils.isNotBlank(value) -> {
                    val content = LocalizationHelper.getMessage(currentViewUser.locale, enumCls, value)
                    if (content.length > 150) content.substring(0, 150) + "..." else content
                }
                else -> msgIfBlank
            }
}
