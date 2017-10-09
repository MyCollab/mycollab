package com.mycollab.db.query

import com.mycollab.core.MyCollabException
import com.mycollab.db.arguments.BetweenValuesSearchField
import com.mycollab.db.arguments.OneValueSearchField
import com.mycollab.db.arguments.SearchField
import java.lang.reflect.Array
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class DateParam(id: String, table: String, column: String) : ColumnParam(id, table, column) {

    fun buildSearchField(prefixOper: String, compareOper: String, dateValue1: Date, dateValue2: Date): SearchField =
            when (compareOper) {
                DateParam.BETWEEN -> buildDateValBetween(prefixOper, dateValue1, dateValue2)
                DateParam.NOT_BETWEEN -> buildDateValNotBetween(prefixOper, dateValue1, dateValue2)
                else -> throw MyCollabException("Not support yet")
            }

    fun buildSearchField(prefixOper: String, compareOper: String, dateValue: Date): SearchField =
            when (compareOper) {
                DateParam.IS -> buildDateIsEqual(prefixOper, dateValue)
                DateParam.IS_NOT -> buildDateIsNotEqual(prefixOper, dateValue)
                DateParam.BEFORE -> buildDateIsLessThan(prefixOper, dateValue)
                DateParam.AFTER -> buildDateIsGreaterThan(prefixOper, dateValue)
                else -> throw MyCollabException("Not support yet")
            }

    private fun buildDateValBetween(oper: String, value1: Date, value2: Date): BetweenValuesSearchField =
            BetweenValuesSearchField(oper, String.format("DATE(%s.%s) BETWEEN", table, column), value1, value2)

    private fun buildDateValNotBetween(oper: String, value1: Date, value2: Date): BetweenValuesSearchField =
            BetweenValuesSearchField(oper, String.format("DATE(%s.%s) NOT BETWEEN", table, column), value1, value2)

    private fun buildDateIsEqual(oper: String, value: Date): OneValueSearchField =
            OneValueSearchField(oper, String.format("DATE(%s.%s) = ", table, column), value)

    private fun buildDateIsNotEqual(oper: String, value: Date): OneValueSearchField =
            OneValueSearchField(oper, String.format("DATE(%s.%s) <> ", table, column), value)

    private fun buildDateIsGreaterThan(oper: String, value: Date): OneValueSearchField =
            OneValueSearchField(oper, String.format("DATE(%s.%s) >= ", table, column), value)

    private fun buildDateIsLessThan(oper: String, value: Date): OneValueSearchField =
            OneValueSearchField(oper, String.format("DATE(%s.%s) <= ", table, column), value)

    companion object {
        @JvmField
        val IS = "is"

        @JvmField
        val IS_NOT = "isn't"

        @JvmField
        val BEFORE = "is before"

        @JvmField
        val AFTER = "is after"

        @JvmField
        val BETWEEN = "between"

        @JvmField
        val NOT_BETWEEN = "not between"

        @JvmField
        val OPTIONS = arrayOf(IS, IS_NOT, BEFORE, AFTER, BETWEEN, NOT_BETWEEN)

        @JvmStatic
        fun inRangeDate(dateParam: DateParam, variableInjector: VariableInjector<*>): SearchField? {
            val value = variableInjector.eval()
            return if (value != null) {
                if (value.javaClass.isArray) {
                    dateParam.buildSearchField(SearchField.AND, BETWEEN, Array.get(value, 0) as Date, Array.get(value, 1) as Date)
                } else {
                    dateParam.buildSearchField(SearchField.AND, BETWEEN, value as Date)
                }
            } else {
                null
            }
        }
    }
}
