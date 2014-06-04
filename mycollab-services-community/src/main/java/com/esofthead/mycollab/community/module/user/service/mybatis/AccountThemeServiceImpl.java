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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultCrudService;
import com.esofthead.mycollab.module.user.dao.AccountThemeMapper;
import com.esofthead.mycollab.module.user.domain.AccountTheme;
import com.esofthead.mycollab.module.user.service.AccountThemeService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
@Service
public class AccountThemeServiceImpl extends
		DefaultCrudService<Integer, AccountTheme> implements
		AccountThemeService {

	@Autowired
	private AccountThemeMapper accountThemeMapper;

	@Override
	public ICrudGenericDAO<Integer, AccountTheme> getCrudMapper() {
		return accountThemeMapper;
	}

	@Override
	public AccountTheme getAccountTheme(int saccountid) {
		return null;
	}

	@Override
	public AccountTheme getDefaultTheme() {
		return null;
	}

	@Override
	public void saveAccountTheme(AccountTheme theme, int saccountid) {
		throw new MyCollabException(
				"This feature is not supported except onsite and premium mode");
	}

}
