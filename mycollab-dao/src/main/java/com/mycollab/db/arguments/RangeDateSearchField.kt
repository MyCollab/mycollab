package com.mycollab.db.arguments

import java.util.Date

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class RangeDateSearchField : SearchField {

    var from: Date? = null
    var to: Date? = null

    constructor()

    constructor(from: Date, to: Date) : this(SearchField.AND, from, to)

    constructor(oper: String, from: Date, to: Date) {
        this.operation = oper
        this.from = from
        this.to = to
    }

    companion object {
        private val serialVersionUID = 1L
    }
}
