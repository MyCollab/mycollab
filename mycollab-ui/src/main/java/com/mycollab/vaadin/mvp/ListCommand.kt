package com.mycollab.vaadin.mvp

import com.mycollab.db.arguments.SearchCriteria

/**
 * @param <S>
 * @author MyCollab
 * @since 1.0
</S> */
interface ListCommand<in S : SearchCriteria> {

    fun doSearch(searchCriteria: S)
}
