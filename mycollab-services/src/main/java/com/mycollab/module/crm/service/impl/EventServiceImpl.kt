package com.mycollab.module.crm.service.impl

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultSearchService
import com.mycollab.module.crm.dao.EventMapperExt
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria
import com.mycollab.module.crm.service.EventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
class EventServiceImpl : DefaultSearchService<ActivitySearchCriteria>(), EventService {

    @Autowired
    private val eventMapperExt: EventMapperExt? = null

    override val searchMapper: ISearchableDAO<ActivitySearchCriteria>
        get() = eventMapperExt as ISearchableDAO<ActivitySearchCriteria>

    override fun getTotalCount(criteria: ActivitySearchCriteria): Int {
        return eventMapperExt!!.getTotalCountFromCall(criteria) +
                eventMapperExt.getTotalCountFromTask(criteria) +
                eventMapperExt.getTotalCountFromMeeting(criteria)
    }
}
