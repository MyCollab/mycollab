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

import com.esofthead.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
public class AccountLinkBuilder {
    public static String generatePreviewFullUserLink(String username) {
        return AccountLinkGenerator.generatePreviewFullUserLink(
                AppContext.getSiteUrl(), username);
    }

    public static String generatePreviewFullRoleLink(Integer userRoleId) {
        return AccountLinkGenerator.generatePreviewFullRoleLink(
                AppContext.getSiteUrl(), userRoleId);
    }
}
