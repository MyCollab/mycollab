package com.mycollab.db.arguments

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
open class SearchRequest(var currentPage: Int, var numberOfItems: Int) {
    companion object {
        @JvmField val DEFAULT_NUMBER_SEARCH_ITEMS = 25
    }
}