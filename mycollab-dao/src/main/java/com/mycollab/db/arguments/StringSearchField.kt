package com.mycollab.db.arguments

import com.mycollab.core.utils.StringUtils

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class StringSearchField(operation: String = SearchField.AND, val value: String = "") : SearchField(operation) {
    companion object {
        @JvmStatic
        fun and(value: String): StringSearchField? = if (StringUtils.isNotBlank(value)) StringSearchField(SearchField.AND, value) else null
    }
}