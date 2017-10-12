/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.service;

import com.mycollab.cache.IgnoreCacheClass;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.ICrudService;
import com.mycollab.module.user.domain.BillingAccount;
import com.mycollab.module.user.domain.SimpleBillingAccount;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@IgnoreCacheClass
public interface BillingAccountService extends ICrudService<Integer, BillingAccount> {

    @Cacheable
    SimpleBillingAccount getBillingAccountById(@CacheKey Integer accountId);

    SimpleBillingAccount getAccountByDomain(String domain);

    @Cacheable
    BillingAccount getAccountById(@CacheKey Integer accountId);

    void createDefaultAccountData(String username, String password, String timezoneId, String language, Boolean isEmailVerified,
                                  Boolean isCreatedDefaultData, Integer sAccountId);
}
