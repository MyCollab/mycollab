package com.mycollab.db.query

import com.mycollab.db.arguments.CollectionValueSearchField

import com.mycollab.common.i18n.QueryI18nEnum.CollectionI18nEnum

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class PropertyListParam<P>(id: String, table: String, column: String) : ColumnParam(id, table, column) {

    fun buildPropertyParamInList(oper: String, value: Collection<P>): CollectionValueSearchField {
        val IN_EXPR = "%s.%s in "
        return CollectionValueSearchField(oper, String.format(IN_EXPR, this.table, this.column), value)
    }

    fun buildPropertyParamNotInList(oper: String, value: Collection<P>): CollectionValueSearchField {
        val NOT_IN_EXPR = "%s.%s not in "
        return CollectionValueSearchField(oper, String.format(NOT_IN_EXPR, this.table, this.column), value)
    }

    companion object {

        @JvmField val OPTIONS = arrayOf(CollectionI18nEnum.IN, CollectionI18nEnum.NOT_IN)
    }
}
