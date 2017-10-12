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
import com.mycollab.db.arguments.OneValueSearchField
import com.mycollab.db.arguments.SearchField

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class ConcatStringParam(id: String, private val table: String, private val columns: Array<String>) : Param(id) {

    fun buildSearchField(prefixOper: String, compareOper: String, value: String): SearchField {
        val compareValue = valueOf(compareOper)
        return when (compareValue) {
            CONTAINS -> this.buildStringParamIsLike(prefixOper, value)
            NOT_CONTAINS -> this.buildStringParamIsNotLike(prefixOper, value)
            else -> throw MyCollabException("Not support yet")
        }
    }

    private fun buildStringParamIsLike(oper: String, value: Any): OneValueSearchField {
        return OneValueSearchField(oper, buildConcatField() + " like ", "%$value%")
    }

    private fun buildStringParamIsNotLike(oper: String, value: Any): OneValueSearchField {
        return OneValueSearchField(oper, buildConcatField() + " not like ", "%$value%")
    }

    private fun buildConcatField(): String {
        val concatField = StringBuffer("concat(")
        for (i in columns.indices) {
            if (i == 0) {
                concatField.append("IFNULL(").append(table).append(".").append(columns[0]).append(",''), ' ',")
            } else if (i < columns.size - 1) {
                concatField.append("IFNULL(").append(table).append(".").append(columns[i]).append(",''), ' '")
            } else {
                concatField.append("IFNULL(").append(table).append(".").append(columns[i]).append(",'')")
            }
        }
        concatField.append(")")
        return concatField.toString()
    }

    companion object {

        @JvmField val OPTIONS = arrayOf(CONTAINS, NOT_CONTAINS)
    }
}
