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

import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultCrudService;
import com.esofthead.mycollab.module.user.dao.UserPreferenceMapper;
import com.esofthead.mycollab.module.user.domain.UserPreference;
import com.esofthead.mycollab.module.user.domain.UserPreferenceExample;
import com.esofthead.mycollab.module.user.service.UserPreferenceService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
public class UserPreferenceServiceImpl extends
		DefaultCrudService<Integer, UserPreference> implements
		UserPreferenceService {

	@Autowired
	protected UserPreferenceMapper userPreferenceMapper;

	@Override
	public ICrudGenericDAO<Integer, UserPreference> getCrudMapper() {
		return userPreferenceMapper;
	}

	@Override
	public UserPreference getPreferenceOfUser(String username, Integer accountId) {
		UserPreferenceExample ex = new UserPreferenceExample();
		ex.createCriteria().andUsernameEqualTo(username)
				.andSaccountidEqualTo(accountId);
		List<UserPreference> userPreferences = userPreferenceMapper
				.selectByExample(ex);
		UserPreference pref;

		if (CollectionUtils.isNotEmpty(userPreferences)) {
			pref = userPreferences.get(0);
		} else {
			// create default user preference then save to database
			pref = new UserPreference();
			pref.setLastaccessedtime(new GregorianCalendar().getTime());
			pref.setUsername(username);
			pref.setSaccountid(accountId);
			userPreferenceMapper.insert(pref);
		}

		return pref;
	}

}
