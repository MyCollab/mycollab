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

import com.mycollab.core.cache.CacheArgs;
import com.mycollab.core.cache.CacheEvict;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IDefaultService;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.domain.*;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface CampaignService extends IDefaultService<Integer, CampaignWithBLOBs, CampaignSearchCriteria> {

    @Cacheable
    SimpleCampaign findById(Integer campaignId, @CacheKey Integer sAccountId);

    @CacheEvict
    @CacheArgs(values = {AccountService.class})
    void saveCampaignAccountRelationship(List<CampaignAccount> associateAccounts, @CacheKey Integer sAccountId);

    @CacheEvict
    @CacheArgs(values = {AccountService.class})
    void removeCampaignAccountRelationship(CampaignAccount associateAccount, @CacheKey Integer sAccountId);

    @CacheEvict
    @CacheArgs(values = {ContactService.class})
    void saveCampaignContactRelationship(List<CampaignContact> associateContacts, @CacheKey Integer sAccountId);

    @CacheEvict
    @CacheArgs(values = {ContactService.class})
    void removeCampaignContactRelationship(CampaignContact associateContact, @CacheKey Integer sAccountId);

    @CacheEvict
    @CacheArgs(values = {LeadService.class})
    void saveCampaignLeadRelationship(List<CampaignLead> associateLeads, @CacheKey Integer sAccountId);

    @CacheEvict
    @CacheArgs(values = {LeadService.class})
    void removeCampaignLeadRelationship(CampaignLead associateLead, @CacheKey Integer sAccountId);
}
