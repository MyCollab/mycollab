package com.mycollab.module.crm.dao

import com.mycollab.db.persistence.IMassUpdateDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.crm.domain.Contact
import com.mycollab.module.crm.domain.SimpleContact
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria
import org.apache.ibatis.annotations.Param

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface ContactMapperExt : ISearchableDAO<ContactSearchCriteria>, IMassUpdateDAO<Contact, ContactSearchCriteria> {

    fun findById(contactId: Int?): SimpleContact

    fun findContactAssoWithConvertedLead(@Param("leadId") leadId: Int?): SimpleContact
}
