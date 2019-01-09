package com.mycollab.db.query

import com.mycollab.core.MyCollabException
import com.mycollab.db.arguments.OneValueSearchField
import com.mycollab.db.arguments.SearchField
import com.mycollab.common.i18n.QueryI18nEnum.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class BooleanParam(id: String, table: String, column: String) : ColumnParam(id, table, column) {
    fun buildSearchField(prefixOper: String, compareOper: String, value: String): SearchField {
        val compareValue = valueOf(compareOper)
        return when (compareValue) {
            IS -> OneValueSearchField(prefixOper, "$table.$column = ", convertValueToBoolean(value))
            else -> throw MyCollabException("Not support yet")
        }
    }
    companion object {

        @JvmField
        val OPTIONS = arrayOf(IS)

        fun convertValueToBoolean(value: String): Int = if (value == "ACTION_YES" || value == "true") 1 else 0
    }
}