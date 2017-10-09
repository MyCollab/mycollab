package com.mycollab.db.arguments

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class CollectionValueSearchField(oper: String, var queryCount: String?, var value: Collection<*>?) : SearchField() {
    var querySelect: String? = null

    init {
        this.operation = oper
        this.querySelect = queryCount
    }

    companion object {
        private val serialVersionUID = 1L
    }
}
