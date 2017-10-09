package com.mycollab.db.query

import com.mycollab.db.arguments.SearchCriteria
import java.util.Arrays

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
class SearchQueryInfo<S: SearchCriteria>(val queryId: String, var queryName: String, var searchFieldInfos: List<SearchFieldInfo<S>>) {

    constructor(queryName: String, vararg searchFieldInfoArr: SearchFieldInfo<S>) : this(queryName, Arrays.asList(*searchFieldInfoArr))

    constructor(queryName: String, searchFieldInfos: List<SearchFieldInfo<S>>) : this("", queryName, searchFieldInfos)

    constructor(queryId: String, queryName: String, vararg searchFieldInfoArr: SearchFieldInfo<S>) : this(queryId, queryName, Arrays.asList(*searchFieldInfoArr))
}
