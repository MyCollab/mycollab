package com.mycollab.db.arguments

import com.mycollab.core.utils.BeanUtility

import java.io.Serializable

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
open class SearchField : Serializable {

    var operation = AND

    constructor() {}

    constructor(operation: String) {
        this.operation = operation
    }

    override fun toString(): String {
        return BeanUtility.printBeanObj(this)
    }

    companion object {
        private const val serialVersionUID = 1L

        @JvmField
        val OR = "OR"

        @JvmField
        val AND = "AND"
    }
}
