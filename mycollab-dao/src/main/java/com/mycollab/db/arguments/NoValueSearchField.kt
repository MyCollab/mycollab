package com.mycollab.db.arguments

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class NoValueSearchField(operation: String, var expression: String) : SearchField(operation) {
    val queryCount = expression
    val querySelect = expression
}