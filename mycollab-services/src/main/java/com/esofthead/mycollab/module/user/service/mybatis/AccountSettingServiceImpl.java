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
