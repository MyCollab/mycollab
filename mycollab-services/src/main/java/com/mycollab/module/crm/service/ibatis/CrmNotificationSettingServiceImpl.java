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
package com.mycollab.module.crm.service.ibatis;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycollab.common.NotificationType;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.service.DefaultCrudService;
import com.mycollab.module.crm.dao.CrmNotificationSettingMapper;
import com.mycollab.module.crm.domain.CrmNotificationSetting;
import com.mycollab.module.crm.domain.CrmNotificationSettingExample;
import com.mycollab.module.crm.service.CrmNotificationSettingService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
public class CrmNotificationSettingServiceImpl extends
		DefaultCrudService<Integer, CrmNotificationSetting> implements
		CrmNotificationSettingService {

	@Autowired
	private CrmNotificationSettingMapper crmNotificationSettingMapper;

	@Override
	public ICrudGenericDAO<Integer, CrmNotificationSetting> getCrudMapper() {
		return crmNotificationSettingMapper;
	}

	@Override
	@Cacheable
	public CrmNotificationSetting findNotification(String username,
			@CacheKey Integer sAccountId) {
		CrmNotificationSettingExample ex = new CrmNotificationSettingExample();
		ex.createCriteria().andUsernameEqualTo(username)
				.andSaccountidEqualTo(sAccountId);
		List<CrmNotificationSetting> notifications = crmNotificationSettingMapper
				.selectByExample(ex);
		if (CollectionUtils.isNotEmpty(notifications)) {
			return notifications.get(0);
		} else {
			CrmNotificationSetting notification = new CrmNotificationSetting();
			notification.setSaccountid(sAccountId);
			notification.setUsername(username);
			notification.setLevel(NotificationType.Default.name());
			return notification;
		}
	}

	@Override
	@Cacheable
	public List<CrmNotificationSetting> findNotifications(
			@CacheKey Integer sAccountId) {
		CrmNotificationSettingExample ex = new CrmNotificationSettingExample();
		ex.createCriteria().andSaccountidEqualTo(sAccountId);
		return crmNotificationSettingMapper.selectByExample(ex);
	}

}
