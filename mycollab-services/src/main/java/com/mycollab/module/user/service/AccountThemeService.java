package com.mycollab.module.user.service;

import com.mycollab.core.cache.CacheEvict;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.ICrudService;
import com.mycollab.module.user.domain.AccountTheme;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public interface AccountThemeService extends ICrudService<Integer, AccountTheme> {
    @Cacheable
    AccountTheme findTheme(@CacheKey Integer sAccountId);

    @Cacheable
    AccountTheme findDefaultTheme(@CacheKey Integer sAccountId);

    @CacheEvict
    void removeTheme(@CacheKey Integer sAccountId);
}
