package com.mycollab.db.arguments

import com.mycollab.core.utils.BeanUtility

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class BitSearchField @JvmOverloads constructor(oper: String = SearchField.AND, value: Int = 0, compareOperator: String = NumberSearchField.EQUAL) : NumberSearchField(oper, value, compareOperator) {

    override fun toString(): String {
        return BeanUtility.printBeanObj(this)
    }

    companion object {
        private val serialVersionUID = 1L

        @JvmField val TRUE = BitSearchField(SearchField.AND, 1)
        @JvmField val FALSE = BitSearchField(SearchField.AND, 0)
    }
}
