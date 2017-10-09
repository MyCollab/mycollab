package com.mycollab.db.query

import com.mycollab.db.arguments.CollectionValueSearchField
import com.mycollab.db.arguments.SearchField

import com.mycollab.common.i18n.QueryI18nEnum.CollectionI18nEnum

/**
 * @author MyCollab Ltd.
 * @since 4.3.0
 */
class I18nStringListParam(id: String, table: String, column: String, var values: List<Enum<*>>?) : ColumnParam(id, table, column) {

    fun buildStringParamInList(oper: String, value: Collection<*>): CollectionValueSearchField {
        val IN_EXPR = "%s.%s in "
        return CollectionValueSearchField(oper, String.format(IN_EXPR, this.table, this.column), value)
    }

    fun andStringParamInList(value: Collection<*>): CollectionValueSearchField {
        return buildStringParamInList(SearchField.AND, value)
    }

    fun orStringParamInList(value: Collection<*>): CollectionValueSearchField {
        return buildStringParamInList(SearchField.OR, value)
    }

    fun buildStringParamNotInList(oper: String, value: Collection<*>): CollectionValueSearchField {
        val NOT_IN_EXPR = "%s.%s not in "
        return CollectionValueSearchField(oper, String.format(NOT_IN_EXPR,
                this.table, this.column), value)
    }

    fun andStringParamNotInList(value: List<*>): CollectionValueSearchField {
        return buildStringParamNotInList(SearchField.AND, value)
    }

    fun orStringParamNotInList(value: List<*>): CollectionValueSearchField {
        return buildStringParamNotInList(SearchField.OR, value)
    }

    companion object {

        @JvmField val OPTIONS = arrayOf(CollectionI18nEnum.IN, CollectionI18nEnum.NOT_IN)
    }
}
