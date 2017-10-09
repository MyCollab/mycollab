package com.mycollab.aspect

import java.util.ArrayList

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
class ClassInfo(var module: String?, var type: String?) {
    private val excludeHistoryFields: MutableList<String>

    init {
        excludeHistoryFields = ArrayList()
        excludeHistoryFields.add("id")
        excludeHistoryFields.add("lastupdatedtime")
        excludeHistoryFields.add("createdtime")
    }

    fun addExcludeHistoryField(field: String) {
        excludeHistoryFields.add(field)
    }

    fun getExcludeHistoryFields(): List<String> {
        return excludeHistoryFields
    }
}
