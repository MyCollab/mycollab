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
package com.mycollab.db.arguments

import java.io.Serializable
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
abstract class SearchCriteria : Serializable {

    private var orderFields: MutableList<OrderField>? = null
    var saccountid: NumberSearchField? = null
    private var extraFields: MutableList<SearchField>? = null

    init {
        saccountid = NumberSearchField(GroupIdProvider.accountId)
    }

    fun getExtraFields(): List<SearchField>? {
        return extraFields
    }

    fun setExtraFields(extraFields: MutableList<SearchField>) {
        this.extraFields = extraFields
    }

    fun addExtraField(extraField: SearchField?): SearchCriteria {
        if (extraField == null) {
            return this
        }
        if (extraFields == null) {
            extraFields = mutableListOf()
        }
        extraFields!!.add(extraField)
        return this
    }

    fun addOrderField(orderField: OrderField) {
        if (orderFields == null) {
            orderFields = ArrayList()
        }
        orderFields!!.add(orderField)
    }

    fun getOrderFields(): List<OrderField>? {
        return orderFields
    }

    fun setOrderFields(orderFields: MutableList<OrderField>) {
        this.orderFields = orderFields
    }

    class OrderField(val field: String, val direction: String) : Serializable

    companion object {
        private const val serialVersionUID = 1L

        @JvmField
        val ASC = "ASC"

        @JvmField
        val DESC = "DESC"
    }
}
