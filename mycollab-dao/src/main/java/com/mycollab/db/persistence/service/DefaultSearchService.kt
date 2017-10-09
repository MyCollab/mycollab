package com.mycollab.db.persistence.service

import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.SearchCriteria
import com.mycollab.db.persistence.ISearchableDAO
import org.apache.ibatis.session.RowBounds

/**
 * @param <S>
 * @author MyCollab Ltd.
 * @since 1.0
</S> */
abstract class DefaultSearchService<S : SearchCriteria> : ISearchableService<S> {

    abstract val searchMapper: ISearchableDAO<S>

    override fun getTotalCount(criteria: S): Int {
        return searchMapper.getTotalCount(criteria)
    }

    override fun findPageableListByCriteria(searchRequest: BasicSearchRequest<S>): List<*> {
        return searchMapper.findPageableListByCriteria(searchRequest.searchCriteria,
                RowBounds((searchRequest.currentPage - 1) * searchRequest.numberOfItems,
                        searchRequest.numberOfItems))
    }

    override fun findAbsoluteListByCriteria(searchCriteria: S, firstIndex: Int?, numberOftems: Int?): List<*> {
        return searchMapper.findPageableListByCriteria(searchCriteria,
                RowBounds(firstIndex!!, numberOftems!!))
    }

    override fun removeByCriteria(criteria: S, sAccountId: Int) {
        searchMapper.removeByCriteria(criteria)
    }

    override fun getNextItemKey(criteria: S): Int? {
        return searchMapper.getNextItemKey(criteria)
    }

    override fun getPreviousItemKey(criteria: S): Int? {
        return searchMapper.getPreviousItemKey(criteria)
    }
}
