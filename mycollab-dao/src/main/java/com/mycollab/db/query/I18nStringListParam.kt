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
package com.mycollab.db.query

import com.mycollab.common.i18n.QueryI18nEnum.IN
import com.mycollab.common.i18n.QueryI18nEnum.NOT_IN
import com.mycollab.db.arguments.SetValueSearchField
import com.mycollab.db.arguments.SearchField

/**
 * @author MyCollab Ltd.
 * @since 4.3.0
 */
class I18nStringListParam(id: String, table: String, column: String, var values: Set<Enum<*>>?) : ColumnParam(id, table, column) {

    fun buildStringParamInList(oper: String, value: Set<*>): SetValueSearchField =
            SetValueSearchField(oper, "$table.$column in ", value)

    fun andStringParamInList(value: Set<*>): SetValueSearchField =
            buildStringParamInList(SearchField.AND, value)

    fun orStringParamInList(value: Set<*>): SetValueSearchField =
            buildStringParamInList(SearchField.OR, value)

    fun buildStringParamNotInList(oper: String, value: Set<*>): SetValueSearchField =
            SetValueSearchField(oper, "$table.$column not in ", value)

    fun andStringParamNotInList(value: Set<*>): SetValueSearchField =
            buildStringParamNotInList(SearchField.AND, value)

    fun orStringParamNotInList(value: Set<*>): SetValueSearchField =
            buildStringParamNotInList(SearchField.OR, value)

    companion object {
        @JvmField
        val OPTIONS = arrayOf(IN, NOT_IN)
    }
}
