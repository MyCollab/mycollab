/**
 * mycollab-services - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.service

import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.crm.domain.Lead
import com.mycollab.module.crm.domain.Opportunity
import com.mycollab.module.crm.domain.SimpleLead
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface LeadService : IDefaultService<Int, Lead, LeadSearchCriteria> {
    @Cacheable
    fun findById(leadId: Int, @CacheKey sAccountId: Int): SimpleLead?

    @CacheEvict
    fun convertLead(lead: SimpleLead, opportunity: Opportunity?, convertUser: String)

    fun findConvertedLeadOfAccount(accountId: Int?, @CacheKey sAccountId: Int?): SimpleLead

    fun findConvertedLeadOfContact(contactId: Int?, @CacheKey sAccountId: Int?): SimpleLead

    fun findConvertedLeadOfOpportunity(opportunity: Int?, @CacheKey sAccountId: Int?): SimpleLead
}
