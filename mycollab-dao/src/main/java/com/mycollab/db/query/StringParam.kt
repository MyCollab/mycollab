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

import com.mycollab.common.i18n.QueryI18nEnum.StringI18nEnum.*
import com.mycollab.core.MyCollabException
import com.mycollab.db.arguments.NoValueSearchField
import com.mycollab.db.arguments.OneValueSearchField
import com.mycollab.db.arguments.SearchField

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class StringParam(id: String, table: String, column: String) : ColumnParam(id, table, column) {

    fun buildSearchField(prefixOper: String, compareOper: String, value: String): SearchField {
        val compareVal = valueOf(compareOper)
        return when (compareVal) {
            IS_EMPTY -> this.buildStringParamIsNull(prefixOper)
            IS_NOT_EMPTY -> this.buildStringParamIsNotNull(prefixOper)
            IS -> this.buildStringParamIsEqual(prefixOper, value)
            IS_NOT -> this.buildStringParamIsNotEqual(prefixOper, value)
            CONTAINS -> this.buildStringParamIsLike(prefixOper, value)
            NOT_CONTAINS -> this.buildStringParamIsNotLike(prefixOper, value)
            else -> throw MyCollabException("Not support yet")
        }
    }

    fun buildStringParamIsNull(oper: String): NoValueSearchField = NoValueSearchField(oper, "$table.$column is null")

    fun andStringParamIsNull(): NoValueSearchField = buildStringParamIsNull(SearchField.AND)

    fun orStringParamIsNull(): NoValueSearchField = buildStringParamIsNull(SearchField.OR)

    fun buildStringParamIsNotNull(oper: String): NoValueSearchField =
            NoValueSearchField(oper, "$table.$column is not null")

    fun andStringParamIsNotNull(): NoValueSearchField = buildStringParamIsNotNull(SearchField.AND)

    fun orStringParamIsNotNull(): NoValueSearchField = buildStringParamIsNull(SearchField.OR)

    fun buildStringParamIsEqual(oper: String, value: Any): OneValueSearchField =
            OneValueSearchField(oper, "$table.$column = ", value)

    fun andStringParamIsEqual(value: Any): OneValueSearchField = buildStringParamIsEqual(SearchField.AND, value)

    fun orStringParamIsEqual(value: Any): OneValueSearchField = buildStringParamIsEqual(SearchField.OR, value)

    fun buildStringParamIsNotEqual(oper: String, value: Any): OneValueSearchField =
            OneValueSearchField(oper, "$table.$column <> ", value)

    fun andStringParamIsNotEqual(value: Any): OneValueSearchField = buildStringParamIsNotEqual(SearchField.AND, value)

    fun orStringParamIsNotEqual(param: StringParam, value: Any): OneValueSearchField =
            buildStringParamIsNotEqual(SearchField.OR, value)

    fun buildStringParamIsLike(oper: String, value: Any): OneValueSearchField =
            OneValueSearchField(oper, "$table.$column like ", "%$value%")

    fun andStringParamIsLike(value: Any): OneValueSearchField = buildStringParamIsLike(SearchField.AND, value)

    fun orStringParamIsLike(value: Any): OneValueSearchField = buildStringParamIsLike(SearchField.OR, value)

    fun buildStringParamIsNotLike(oper: String, value: Any): OneValueSearchField =
            OneValueSearchField(oper, "$table.$column not like ", "%$value%")

    fun andStringParamIsNotLike(value: Any): OneValueSearchField = buildStringParamIsNotLike(SearchField.AND, value)

    fun orStringParamIsNotLike(value: Any): OneValueSearchField = buildStringParamIsNotLike(SearchField.OR, value)

    companion object {

        @JvmField val OPTIONS = arrayOf(IS, IS_NOT, CONTAINS, NOT_CONTAINS, IS_EMPTY, IS_NOT_EMPTY)
    }
}
