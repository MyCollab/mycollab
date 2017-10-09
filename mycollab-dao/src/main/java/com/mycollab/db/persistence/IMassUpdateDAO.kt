package com.mycollab.db.persistence

import com.mycollab.db.arguments.SearchCriteria
import org.apache.ibatis.annotations.Param

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 *
 * @param <R>
 * @param <S>
</S></R> */
interface IMassUpdateDAO<R, S : SearchCriteria> {
    fun updateBySearchCriteria(@Param("record") record: R, @Param("searchCriteria") searchCriteria: S)
}
