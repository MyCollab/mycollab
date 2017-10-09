package com.mycollab.community.module.user.service.impl;

import com.mycollab.core.UnsupportedFeatureException;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.service.DefaultCrudService;
import com.mycollab.module.user.dao.AccountThemeMapper;
import com.mycollab.module.user.domain.AccountTheme;
import com.mycollab.module.user.domain.AccountThemeExample;
import com.mycollab.module.user.service.AccountThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
@Service
public class AccountThemeServiceImpl extends DefaultCrudService<Integer, AccountTheme> implements AccountThemeService {

    @Autowired
    private AccountThemeMapper accountThemeMapper;

    @Override
    public ICrudGenericDAO<Integer, AccountTheme> getCrudMapper() {
        return accountThemeMapper;
    }

    @Override
    public AccountTheme findTheme(@CacheKey Integer sAccountId) {
        return null;
    }

    @Override
    public AccountTheme findDefaultTheme(@CacheKey Integer sAccountId) {
        AccountThemeExample ex = new AccountThemeExample();
        ex.createCriteria().andIsdefaultEqualTo(Boolean.TRUE);
        List<AccountTheme> accountThemes = accountThemeMapper.selectByExample(ex);
        if (accountThemes != null && accountThemes.size() > 0) {
            return accountThemes.get(0);
        }

        return null;
    }

    @Override
    public void removeTheme(@CacheKey Integer sAccountId) {
        throw new UnsupportedFeatureException("Do not support this feature in the community edition");
    }
}
