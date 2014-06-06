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
package com.esofthead.mycollab.module.user;

import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Text;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public class AccountLinkBuilder {

	public static String generatePreviewFullUserLink(String username) {
		return AccountLinkUtils.generatePreviewFullUserLink(
				AppContext.getSiteUrl(), username);
	}

	public static String generateUserHtmlLink(String username) {
		UserService userService = ApplicationContextUtil
				.getSpringBean(UserService.class);
		SimpleUser user = userService.findUserByUserNameInAccount(username,
				AppContext.getAccountId());
		if (user != null) {
			A link = new A();
			link.setHref(generatePreviewFullUserLink(username));
			Text text = new Text(user.getDisplayName());
			link.appendChild(text);
			return link.write();
		} else {
			return null;
		}
	}
}
