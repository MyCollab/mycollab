/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.user.accountsettings.view.parameters;

import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class UserScreenData {
	public static class Read extends ScreenData<String> {

		public Read(String params) {
			super(params);
		}
	}

	public static class Add extends ScreenData<SimpleUser> {
		public Add(SimpleUser params) {
			super(params);
		}
	}

	public static class Edit extends ScreenData<User> {
		public Edit(User params) {
			super(params);
		}
	}

	public static class Search extends ScreenData<UserSearchCriteria> {

		public Search(UserSearchCriteria params) {
			super(params);
		}
	}
}
