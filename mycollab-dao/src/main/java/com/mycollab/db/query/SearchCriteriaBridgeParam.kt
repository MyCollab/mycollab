package com.mycollab.db.query

import com.mycollab.db.arguments.NoValueSearchField
import com.mycollab.db.arguments.SearchCriteria

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
abstract class SearchCriteriaBridgeParam<S : SearchCriteria>(id: String) : Param(id) {

    abstract fun injectCriteriaInList(searchCriteria: S, oper: String, value: Collection<*>): S

    abstract fun injectCriteriaNotInList(searchCriteria: S, oper: String, value: Collection<*>): S
}
