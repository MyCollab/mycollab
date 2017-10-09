package com.mycollab.db.query

import com.mycollab.db.arguments.CompositionSearchField
import com.mycollab.db.arguments.SearchField

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class CompositionStringParam(id: String, vararg val params: StringParam) : Param(id) {

    fun buildSearchField(prefixOper: String, compareOper: String, value: String): SearchField {
        val searchField = CompositionSearchField(prefixOper)
        params.map { it.buildSearchField("", compareOper, value) }
                .forEach { searchField.addField(it) }
        return searchField
    }
}
