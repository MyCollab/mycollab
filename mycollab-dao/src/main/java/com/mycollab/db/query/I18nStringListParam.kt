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
