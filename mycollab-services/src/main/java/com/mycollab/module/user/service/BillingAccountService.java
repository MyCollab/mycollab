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
