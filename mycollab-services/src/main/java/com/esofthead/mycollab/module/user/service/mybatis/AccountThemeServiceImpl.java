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
package com.esofthead.mycollab.module.user.service.mybatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultCrudService;
import com.esofthead.mycollab.module.user.dao.AccountSettingsMapper;
import com.esofthead.mycollab.module.user.dao.AccountThemeMapper;
import com.esofthead.mycollab.module.user.domain.AccountSettings;
import com.esofthead.mycollab.module.user.domain.AccountSettingsExample;
import com.esofthead.mycollab.module.user.domain.AccountTheme;
import com.esofthead.mycollab.module.user.service.AccountThemeService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
@Service
@SuppressWarnings("unchecked")
public class AccountThemeServiceImpl extends
		DefaultCrudService<Integer, AccountTheme> implements
		AccountThemeService {

	@Autowired
	private AccountThemeMapper userThemeMapper;

	@Autowired
	private AccountSettingsMapper accountSettingsMapper;

	@Override
	public ICrudGenericDAO<Integer, AccountTheme> getCrudMapper() {
		return userThemeMapper;
	}

	@Override
	public AccountTheme getAccountTheme(int saccountid) {
		AccountSettingsExample accountEx = new AccountSettingsExample();
		accountEx.createCriteria().andSaccountidEqualTo(saccountid);
		List<AccountSettings> accountSettings = accountSettingsMapper
				.selectByExample(accountEx);
		if (accountSettings == null || accountSettings.size() == 0
				|| accountSettings.get(0).getDefaultthemeid() == null) {
			return null;
		}

		AccountTheme accountTheme = userThemeMapper
				.selectByPrimaryKey(accountSettings.get(0).getDefaultthemeid());

		return accountTheme;
	}

}
