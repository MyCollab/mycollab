package com.mycollab.common.service.impl

import com.mycollab.common.dao.SaveSearchResultMapper
import com.mycollab.common.dao.SaveSearchResultMapperExt
import com.mycollab.common.domain.SaveSearchResult
import com.mycollab.common.domain.SaveSearchResultExample
import com.mycollab.common.domain.criteria.SaveSearchResultCriteria
import com.mycollab.common.service.SaveSearchResultService
import com.mycollab.core.UserInvalidInputException
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
class SaveSearchResultServiceImpl(private val saveSearchResultMapper: SaveSearchResultMapper,
                                  private val saveSearchResultMapperExt: SaveSearchResultMapperExt) : DefaultService<Int, SaveSearchResult, SaveSearchResultCriteria>(), SaveSearchResultService {

    override val crudMapper: ICrudGenericDAO<Int, SaveSearchResult>
        get() = saveSearchResultMapper as ICrudGenericDAO<Int, SaveSearchResult>

    override val searchMapper: ISearchableDAO<SaveSearchResultCriteria>
        get() = saveSearchResultMapperExt

    override fun saveWithSession(record: SaveSearchResult, username: String?): Int {
        checkDuplicateEntryName(record)
        return super.saveWithSession(record, username)
    }

    override fun updateWithSession(record: SaveSearchResult, username: String?): Int {
        return super.updateWithSession(record, username)
    }

    private fun checkDuplicateEntryName(record: SaveSearchResult) {
        val ex = SaveSearchResultExample()
        ex.createCriteria().andSaccountidEqualTo(record.saccountid).andTypeEqualTo(record.type)
                .andQuerynameEqualTo(record.queryname)
        if (saveSearchResultMapper.countByExample(ex) > 0) {
            throw UserInvalidInputException("There is the query name existed")
        }
    }
}
