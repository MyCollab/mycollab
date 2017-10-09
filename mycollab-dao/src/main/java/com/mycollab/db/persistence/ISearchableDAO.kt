package com.mycollab.db.persistence

import com.mycollab.db.arguments.SearchCriteria
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.session.RowBounds

/**
 * @param <S>
 * @author MyCollab Ltd.
 * @since 1.0
</S> */
interface ISearchableDAO<S : SearchCriteria> {

    /**
     * @param criteria
     * @return
     */
    fun getTotalCount(@Param("searchCriteria") criteria: S): Int

    /**
     * @param criteria
     * @param rowBounds
     * @return
     */
    fun findPageableListByCriteria(@Param("searchCriteria") criteria: S, rowBounds: RowBounds): List<*>

    /**
     * @param criteria
     * @return
     */
    fun getNextItemKey(@Param("searchCriteria") criteria: S): Int?

    /**
     * @param criteria
     * @return
     */
    fun getPreviousItemKey(@Param("searchCriteria") criteria: S): Int?

    /**
     * @param criteria
     */
    fun removeByCriteria(@Param("searchCriteria") criteria: S)
}
