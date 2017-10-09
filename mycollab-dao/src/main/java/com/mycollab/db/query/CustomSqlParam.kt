package com.mycollab.db.query

import com.mycollab.db.arguments.NoValueSearchField

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
abstract class CustomSqlParam(id: String) : Param(id) {

    abstract fun buildPropertyParamInList(oper: String, value: Collection<*>): NoValueSearchField

    abstract fun buildPropertyParamNotInList(oper: String, value: Collection<*>): NoValueSearchField
}
