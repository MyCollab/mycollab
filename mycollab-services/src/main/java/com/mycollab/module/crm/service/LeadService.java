/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.service;

import com.mycollab.core.cache.CacheEvict;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IDefaultService;
import com.mycollab.module.crm.domain.Lead;
import com.mycollab.module.crm.domain.Opportunity;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface LeadService extends IDefaultService<Integer, Lead, LeadSearchCriteria> {
    @Cacheable
    SimpleLead findById(Integer leadId, @CacheKey Integer sAccountId);

    @CacheEvict
    void convertLead(SimpleLead lead, Opportunity opportunity, String convertUser);

    SimpleLead findConvertedLeadOfAccount(Integer accountId, @CacheKey Integer sAccountId);

    SimpleLead findConvertedLeadOfContact(Integer contactId, @CacheKey Integer sAccountId);

    SimpleLead findConvertedLeadOfOpportunity(Integer opportunity, @CacheKey Integer sAccountId);
}
