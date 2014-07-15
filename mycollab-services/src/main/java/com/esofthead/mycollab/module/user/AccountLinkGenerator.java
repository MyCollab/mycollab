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
package com.esofthead.mycollab.module.user;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.common.UrlEncodeDecoder;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class AccountLinkGenerator {
	public static String generateRoleLink(Integer userRoleId) {
		return "account/role/preview/" + UrlEncodeDecoder.encode(userRoleId);
	}

	public static String generatePreviewFullRoleLink(String siteUrl,
			Integer userRoleId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ generateRoleLink(userRoleId);
	}

	public static String generatePreviewFullUserLink(String siteUrl,
			String username) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ "account/user/preview/"
				+ GenericLinkUtils.encodeParam(new Object[] { username });
	}
}
