package com.esofthead.mycollab.module.user.service;

import com.esofthead.mycollab.core.persistence.service.ICrudService;
import com.esofthead.mycollab.module.user.domain.AccountSettings;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public interface AccountSettingService extends
		ICrudService<Integer, AccountSettings> {

	AccountSettings findAccountSetting(int sAccountId);
}
