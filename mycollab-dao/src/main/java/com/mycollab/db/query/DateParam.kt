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

import com.mycollab.common.i18n.QueryI18nEnum.*
import com.mycollab.core.MyCollabException
import com.mycollab.db.arguments.BetweenValuesSearchField
import com.mycollab.db.arguments.OneValueSearchField
import com.mycollab.db.arguments.SearchField
import java.lang.reflect.Array
import java.time.LocalDate

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class DateParam(id: String, table: String, column: String) : ColumnParam(id, table, column) {

    fun buildSearchField(prefixOper: String, compareOper: String, dateValue1: LocalDate, dateValue2: LocalDate): SearchField {
        val compareValue = valueOf(compareOper)
        return when (compareValue) {
            BETWEEN -> buildDateValBetween(prefixOper, dateValue1, dateValue2)
            NOT_BETWEEN -> buildDateValNotBetween(prefixOper, dateValue1, dateValue2)
            else -> throw MyCollabException("Not support yet")
        }
    }

    fun buildSearchField(prefixOper: String, compareOper: String, dateValue: LocalDate): SearchField {
        val compareValue = valueOf(compareOper)
        return when (compareValue) {
            IS -> buildDateIsEqual(prefixOper, dateValue)
            IS_NOT -> buildDateIsNotEqual(prefixOper, dateValue)
            BEFORE -> buildDateIsLessThan(prefixOper, dateValue)
            AFTER -> buildDateIsGreaterThan(prefixOper, dateValue)
            else -> throw MyCollabException("Not support yet")
        }
    }

    private fun buildDateValBetween(oper: String, value1: LocalDate, value2: LocalDate): BetweenValuesSearchField =
            BetweenValuesSearchField(oper, "DATE($table.$column) BETWEEN", value1, value2)

    private fun buildDateValNotBetween(oper: String, value1: LocalDate, value2: LocalDate): BetweenValuesSearchField =
            BetweenValuesSearchField(oper, "DATE($table.$column) NOT BETWEEN", value1, value2)

    private fun buildDateIsEqual(oper: String, value: LocalDate): OneValueSearchField =
            OneValueSearchField(oper, "DATE($table.$column) = ", value)

    private fun buildDateIsNotEqual(oper: String, value: LocalDate): OneValueSearchField =
            OneValueSearchField(oper, "DATE($table.$column) <> ", value)

    private fun buildDateIsGreaterThan(oper: String, value: LocalDate): OneValueSearchField =
            OneValueSearchField(oper, "DATE($table.$column) >= ", value)

    private fun buildDateIsLessThan(oper: String, value: LocalDate): OneValueSearchField =
            OneValueSearchField(oper, "DATE($table.$column) <= ", value)

    companion object {

        @JvmField
        val OPTIONS = arrayOf(IS, IS_NOT, BEFORE, AFTER, BETWEEN, NOT_BETWEEN)

        @JvmStatic
        fun inRangeDate(dateParam: DateParam, variableInjector: VariableInjector<*>): SearchField? {
            val value = variableInjector.eval()
            return if (value != null) {
                if (value.javaClass.isArray) {
                    dateParam.buildSearchField(SearchField.AND, BETWEEN.name, Array.get(value, 0) as LocalDate, Array.get(value, 1) as LocalDate)
                } else {
                    dateParam.buildSearchField(SearchField.AND, BETWEEN.name, value as LocalDate)
                }
            } else null
        }
    }
}
