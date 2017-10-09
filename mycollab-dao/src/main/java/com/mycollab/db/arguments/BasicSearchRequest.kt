package com.mycollab.db.arguments

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class BasicSearchRequest<S : SearchCriteria>(val searchCriteria: S, currentPage: Int, numberOfItems: Int) : SearchRequest(currentPage, numberOfItems) {
    constructor(searchCriteria: S) : this(searchCriteria, 1, Integer.MAX_VALUE);
}