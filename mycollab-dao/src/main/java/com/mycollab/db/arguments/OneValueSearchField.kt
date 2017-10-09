package com.mycollab.db.arguments

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class OneValueSearchField(operation: String, expression: String, val value: Any) : SearchField(operation) {
    val queryCount = expression
    val querySelect = expression
}