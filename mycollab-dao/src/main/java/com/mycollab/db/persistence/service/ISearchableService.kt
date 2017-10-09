package com.mycollab.db.persistence.service

import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.SearchCriteria

/**
 * @param <S>
 * @author MyCollab Ltd.
 * @since 1.0
</S> */
interface ISearchableService<S : SearchCriteria> : IService {
    /**
     * @param criteria
     * @return
     */
    @Cacheable
    fun getTotalCount(@CacheKey criteria: S): Int

    /**
     * @param searchRequest
     * @return
     */
    @Cacheable
    fun findPageableListByCriteria(@CacheKey searchRequest: BasicSearchRequest<S>): List<*>

    /**
     * @param searchCriteria
     * @param firstIndex
     * @param numberOfItems
     * @return
     */
    @Cacheable
    fun findAbsoluteListByCriteria(@CacheKey searchCriteria: S, firstIndex: Int?, numberOfItems: Int?): List<*>

    /**
     * @param criteria
     * @param sAccountId
     */
    @CacheEvict
    fun removeByCriteria(criteria: S, @CacheKey sAccountId: Int)

    /**
     * @param criteria
     * @return
     */
    @Cacheable
    fun getNextItemKey(@CacheKey criteria: S): Int?

    /**
     * @param criteria
     * @return
     */
    @Cacheable
    fun getPreviousItemKey(@CacheKey criteria: S): Int?
}
