package com.mycollab.db.query

import com.mycollab.common.i18n.QueryI18nEnum.CollectionI18nEnum
import com.mycollab.db.arguments.CollectionValueSearchField

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class StringListParam(id: String, table: String, column: String, var values: List<String>?) : ColumnParam(id, table, column) {

    fun buildStringParamInList(oper: String, values: Collection<*>): CollectionValueSearchField {
        val IN_EXPR = "%s.%s in "
        return CollectionValueSearchField(oper, String.format(IN_EXPR, this.table, this.column), values)
    }

    fun buildStringParamNotInList(oper: String, values: Collection<*>): CollectionValueSearchField {
        val NOT_IN_EXPR = "%s.%s not in "
        return CollectionValueSearchField(oper, String.format(NOT_IN_EXPR, this.table, this.column), values)
    }

    companion object {

        @JvmField val OPTIONS = arrayOf(CollectionI18nEnum.IN, CollectionI18nEnum.NOT_IN)
    }
}