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
package com.esofthead.mycollab.community.module.user.service.mybatis;

import java.util.List;

import org.springframework.stereotype.Service;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.user.domain.AccountTheme;
import com.esofthead.mycollab.module.user.service.AccountThemeService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
@Service
public class AccountThemeServiceImpl implements AccountThemeService {

	@Override
	public int saveWithSession(AccountTheme record, String username) {
		throw new MyCollabException(
				"This feature is not supported except onsite and premium mode");
	}

	@Override
	public int updateWithSession(AccountTheme record, String username) {
		throw new MyCollabException(
				"This feature is not supported except onsite and premium mode");
	}

	@Override
	public int updateWithSessionWithSelective(AccountTheme record,
			String username) {
		throw new MyCollabException(
				"This feature is not supported except onsite and premium mode");
	}

	@Override
	public void massUpdateWithSession(AccountTheme record,
			List<Integer> primaryKeys, int accountId) {
		throw new MyCollabException(
				"This feature is not supported except onsite and premium mode");
	}

	@Override
	public AccountTheme findByPrimaryKey(Integer primaryKey, int sAccountId) {
		throw new MyCollabException(
				"This feature is not supported except onsite and premium mode");
	}

	@Override
	public int removeWithSession(Integer primaryKey, String username,
			int sAccountId) {
		throw new MyCollabException(
				"This feature is not supported except onsite and premium mode");
	}

	@Override
	public void massRemoveWithSession(List<Integer> primaryKeys,
			String username, int sAccountId) {
		throw new MyCollabException(
				"This feature is not supported except onsite and premium mode");
	}

	@Override
	public AccountTheme getAccountTheme(int saccountid) {
		throw new MyCollabException(
				"This feature is not supported except onsite and premium mode");
	}

	@Override
	public AccountTheme getDefaultTheme() {
		throw new MyCollabException(
				"This feature is not supported except onsite and premium mode");
	}

	@Override
	public void saveAccountTheme(AccountTheme theme, int saccountid) {
		throw new MyCollabException(
				"This feature is not supported except onsite and premium mode");
	}

}
