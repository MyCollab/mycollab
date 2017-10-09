package com.mycollab.db.arguments

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class BetweenValuesSearchField(operation: String, expression: String, var value: Any, var secondValue: Any, var queryCount: String = expression, var querySelect: String = expression): SearchField(operation) {

}