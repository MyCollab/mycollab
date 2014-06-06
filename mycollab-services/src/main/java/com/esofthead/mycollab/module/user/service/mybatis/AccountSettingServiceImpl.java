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
import com.esofthead.mycollab.module.user.domain.AccountSettings;
import com.esofthead.mycollab.module.user.domain.AccountSettingsExample;
import com.esofthead.mycollab.module.user.service.AccountSettingService;

@Service
public class AccountSettingServiceImpl extends
		DefaultCrudService<Integer, AccountSettings> implements
		AccountSettingService {

	@Autowired
	private AccountSettingsMapper accountSettingMapper;

	@Override
	public ICrudGenericDAO<Integer, AccountSettings> getCrudMapper() {
		return accountSettingMapper;
	}

	@Override
	public AccountSettings findAccountSetting(int sAccountId) {
		AccountSettingsExample ex = new AccountSettingsExample();
		ex.createCriteria().andSaccountidEqualTo(sAccountId);
		List<AccountSettings> settings = accountSettingMapper
				.selectByExample(ex);
		if (settings != null && settings.size() > 0) {
			return settings.get(0);
		} else {
			AccountSettings setting = new AccountSettings();
			setting.setDefaulttimezone("3");
			setting.setSaccountid(sAccountId);
			accountSettingMapper.insertAndReturnKey(setting);
			return setting;
		}
	}
}
