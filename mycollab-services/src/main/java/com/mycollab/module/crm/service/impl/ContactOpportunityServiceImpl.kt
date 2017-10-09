package com.mycollab.module.crm.service.impl

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultSearchService
import com.mycollab.module.crm.dao.ContactOpportunityMapperExt
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria
import com.mycollab.module.crm.service.ContactOpportunityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
@Service
class ContactOpportunityServiceImpl : DefaultSearchService<ContactSearchCriteria>(), ContactOpportunityService {

    @Autowired
    private val contactOpportunityMapperExt: ContactOpportunityMapperExt? = null

    override val searchMapper: ISearchableDAO<ContactSearchCriteria>
        get() = contactOpportunityMapperExt as ISearchableDAO<ContactSearchCriteria>

}
