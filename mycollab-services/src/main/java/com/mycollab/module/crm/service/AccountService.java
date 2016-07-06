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
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.AccountLead;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface AccountService extends IDefaultService<Integer, Account, AccountSearchCriteria> {

    @Cacheable
    SimpleAccount findById(Integer id, @CacheKey Integer accountId);

    @CacheEvict
    @CacheArgs(values = {LeadService.class})
    void saveAccountLeadRelationship(List<AccountLead> associateLeads, @CacheKey Integer accountId);

    @CacheEvict
    @CacheArgs(values = {LeadService.class})
    void removeAccountLeadRelationship(AccountLead associateLead, @CacheKey Integer accountId);

    @Cacheable
    SimpleAccount findAccountAssoWithConvertedLead(Integer leadId, @CacheKey Integer accountId);
}
