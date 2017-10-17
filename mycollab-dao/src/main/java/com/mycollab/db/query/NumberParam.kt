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

import com.mycollab.common.i18n.QueryI18nEnum.NumberI18nEnum.*
import com.mycollab.core.MyCollabException
import com.mycollab.db.arguments.NoValueSearchField
import com.mycollab.db.arguments.OneValueSearchField
import com.mycollab.db.arguments.SearchField

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class NumberParam(id: String, table: String, column: String) : ColumnParam(id, table, column) {

    fun buildSearchField(prefixOper: String, compareOper: String, value: Number): SearchField {
        val compareValue = valueOf(compareOper)
        return when (compareValue) {
            EQUAL -> this.buildParamIsEqual(prefixOper, value)
            NOT_EQUAL -> this.buildParamIsNotEqual(prefixOper, value)
            IS_EMPTY -> this.buildParamIsNull(prefixOper)
            IS_NOT_EMPTY -> this.buildParamIsNotNull(prefixOper)
            GREATER_THAN -> this.buildParamIsGreaterThan(prefixOper, value)
            GREATER_THAN_EQUAL -> this.buildParamIsGreaterThanEqual(prefixOper, value)
            LESS_THAN -> this.buildParamIsLessThan(prefixOper, value)
            LESS_THAN_EQUAL -> this.buildParamIsLessThanEqual(prefixOper, value)
            else -> throw MyCollabException("Not support yet")
        }
    }

    fun buildParamIsEqual(oper: String, value: Any): OneValueSearchField =
            OneValueSearchField(oper, String.format(EQUAL_EXPR, this.table, this.column), value)

    private fun buildParamIsNotEqual(oper: String, value: Any): OneValueSearchField =
            OneValueSearchField(oper, String.format(NOT_EQUAL_EXPR, this.table, this.column), value)

    private fun buildParamIsNull(oper: String): NoValueSearchField =
            NoValueSearchField(oper, String.format(NULL_EXPR, this.table, this.column))

    private fun buildParamIsNotNull(oper: String): NoValueSearchField =
            NoValueSearchField(oper, String.format(NOT_NULL_EXPR, this.table, this.column))

    private fun buildParamIsGreaterThan(oper: String, value: Any): OneValueSearchField =
            OneValueSearchField(oper, String.format(GREATER_THAN_EXPR,
                    this.table, this.column), value)

    private fun buildParamIsGreaterThanEqual(oper: String, value: Any): OneValueSearchField =
            OneValueSearchField(oper, String.format(GREATER_THAN_EQUAL_EXPR, this.table, this.column), value)

    private fun buildParamIsLessThan(oper: String, value: Any): OneValueSearchField =
            OneValueSearchField(oper, String.format(LESS_THAN_EXPR, this.table, this.column), value)

    private fun buildParamIsLessThanEqual(oper: String, value: Any): OneValueSearchField =
            OneValueSearchField(oper, String.format(
                    LESS_THAN_EQUAL_EXPR, this.table, this.column), value)

    companion object {

        @JvmField val OPTIONS = arrayOf(EQUAL, NOT_EQUAL, LESS_THAN, LESS_THAN_EQUAL, GREATER_THAN, GREATER_THAN_EQUAL, IS_EMPTY, IS_NOT_EMPTY)

        private val EQUAL_EXPR = "%s.%s = "
        private val NOT_EQUAL_EXPR = "%s.%s <> "
        private val NULL_EXPR = "%s.%s is null"
        private val NOT_NULL_EXPR = "%s.%s is not null"
        private val GREATER_THAN_EXPR = "%s.%s > "
        private val GREATER_THAN_EQUAL_EXPR = "%s.%s >= "
        private val LESS_THAN_EXPR = "%s.%s < "
        private val LESS_THAN_EQUAL_EXPR = "%s.%s <= "
    }
}
