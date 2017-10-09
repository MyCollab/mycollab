package com.mycollab.db.arguments

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class BooleanSearchField(oper: String, var comparision: String?, var isValue: Boolean) : SearchField() {

    @JvmOverloads constructor(value: Boolean = false) : this(SearchField.AND, value)

    constructor(oper: String, value: Boolean) : this(oper, BooleanSearchField.IS, value)

    init {
        this.operation = oper
    }

    companion object {
        private val serialVersionUID = 1L

        val IS = "is"
    }
}
