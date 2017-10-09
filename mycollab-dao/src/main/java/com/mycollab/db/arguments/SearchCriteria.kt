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
