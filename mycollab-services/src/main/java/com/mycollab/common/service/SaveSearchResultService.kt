package com.mycollab.common.service

import com.mycollab.common.domain.SaveSearchResult
import com.mycollab.common.domain.criteria.SaveSearchResultCriteria
import com.mycollab.db.persistence.service.IDefaultService

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface SaveSearchResultService : IDefaultService<Int, SaveSearchResult, SaveSearchResultCriteria>
