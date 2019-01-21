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
import com.mycollab.vaadin.UserUIContext

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
class TrimHtmlHistoryFieldFormat : HistoryFieldFormat {
    override fun toString(value: String): String =
            toString(value, true, UserUIContext.getMessage(GenericI18Enum.FORM_EMPTY))

    override fun toString(value: String, displayAsHtml: Boolean, msgIfBlank: String): String =
            when {
                StringUtils.isNotBlank(value) -> {
                    val content = StringUtils.trimHtmlTags(value)
                    if (content.length > 150) "${content.substring(0, 150)} ..." else content
                }
                else -> msgIfBlank
            }
}
