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

    fun buildStringParamIsNull(oper: String): NoValueSearchField {
        val NULL_EXPR = "%s.%s is null"
        return NoValueSearchField(oper, String.format(NULL_EXPR,
                this.table, this.column))
    }

    fun andStringParamIsNull(): NoValueSearchField {
        return buildStringParamIsNull(SearchField.AND)
    }

    fun orStringParamIsNull(): NoValueSearchField {
        return buildStringParamIsNull(SearchField.OR)
    }

    fun buildStringParamIsNotNull(oper: String): NoValueSearchField {
        val NOT_NULL_EXPR = "%s.%s is not null"
        return NoValueSearchField(oper, String.format(NOT_NULL_EXPR, this.table, this.column))
    }

    fun andStringParamIsNotNull(): NoValueSearchField {
        return buildStringParamIsNotNull(SearchField.AND)
    }

    fun orStringParamIsNotNull(): NoValueSearchField {
        return buildStringParamIsNull(SearchField.OR)
    }

    fun buildStringParamIsEqual(oper: String, value: Any): OneValueSearchField {
        val EQUAL_EXPR = "%s.%s = "
        return OneValueSearchField(oper, String.format(EQUAL_EXPR, this.table, this.column), value)
    }

    fun andStringParamIsEqual(value: Any): OneValueSearchField {
        return buildStringParamIsEqual(SearchField.AND, value)
    }

    fun orStringParamIsEqual(value: Any): OneValueSearchField {
        return buildStringParamIsEqual(SearchField.OR, value)
    }

    fun buildStringParamIsNotEqual(oper: String, value: Any): OneValueSearchField {
        val NOT_EQUAL_EXPR = "%s.%s <> "
        return OneValueSearchField(oper, String.format(NOT_EQUAL_EXPR, this.table, this.column), value)
    }

    fun andStringParamIsNotEqual(value: Any): OneValueSearchField {
        return buildStringParamIsNotEqual(SearchField.AND, value)
    }

    fun orStringParamIsNotEqual(param: StringParam, value: Any): OneValueSearchField {
        return buildStringParamIsNotEqual(SearchField.OR, value)
    }

    fun buildStringParamIsLike(oper: String, value: Any): OneValueSearchField {
        val LIKE_EXPR = "%s.%s like "
        return OneValueSearchField(oper, String.format(LIKE_EXPR, this.table, this.column), "%$value%")
    }

    fun andStringParamIsLike(value: Any): OneValueSearchField {
        return buildStringParamIsLike(SearchField.AND, value)
    }

    fun orStringParamIsLike(value: Any): OneValueSearchField {
        return buildStringParamIsLike(SearchField.OR, value)
    }

    fun buildStringParamIsNotLike(oper: String, value: Any): OneValueSearchField {
        val NOT_LIKE_EXPR = "%s.%s not like "
        return OneValueSearchField(oper, String.format(NOT_LIKE_EXPR,
                this.table, this.column), "%$value%")
    }

    fun andStringParamIsNotLike(value: Any): OneValueSearchField {
        return buildStringParamIsNotLike(SearchField.AND, value)
    }

    fun orStringParamIsNotLike(value: Any): OneValueSearchField {
        return buildStringParamIsNotLike(SearchField.OR, value)
    }

    companion object {

        @JvmField val OPTIONS = arrayOf(IS, IS_NOT, CONTAINS, NOT_CONTAINS, IS_EMPTY, IS_NOT_EMPTY)
    }
}
