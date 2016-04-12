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
package com.esofthead.mycollab.module.crm.service;

import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.core.cache.CacheArgs;
import com.esofthead.mycollab.core.cache.CacheEvict;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.cache.Cacheable;
import com.esofthead.mycollab.core.persistence.service.IDefaultService;
import com.esofthead.mycollab.module.crm.domain.Opportunity;
import com.esofthead.mycollab.module.crm.domain.OpportunityLead;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface OpportunityService extends IDefaultService<Integer, Opportunity, OpportunitySearchCriteria> {

    @Cacheable
    SimpleOpportunity findById(Integer opportunityId, @CacheKey Integer sAccountId);

    @Cacheable
    List<GroupItem> getSalesStageSummary(@CacheKey OpportunitySearchCriteria criteria);

    @Cacheable
    List<GroupItem> getPipeline(@CacheKey OpportunitySearchCriteria criteria);

    @Cacheable
    List<GroupItem> getLeadSourcesSummary(@CacheKey OpportunitySearchCriteria criteria);

    @CacheEvict
    @CacheArgs(values = {LeadService.class})
    void saveOpportunityLeadRelationship(List<OpportunityLead> associateLeads, @CacheKey Integer sAccountId);

    @CacheEvict
    @CacheArgs(values = {LeadService.class})
    void removeOpportunityLeadRelationship(OpportunityLead associateLead, @CacheKey Integer sAccountId);

    @Cacheable
    SimpleOpportunity findOpportunityAssoWithConvertedLead(Integer leadId, @CacheKey Integer accountId);
}
