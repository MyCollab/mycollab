/**
 * This file is part of mycollab-services-community.
 *
 * mycollab-services-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services-community.  If not, see <http://www.gnu.org/licenses/>.
 */
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
