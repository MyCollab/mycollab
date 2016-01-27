/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.html

import com.esofthead.mycollab.common.UrlEncodeDecoder
import com.esofthead.mycollab.configuration.{SiteConfiguration, StorageFactory}
import com.hp.gagawa.java.elements.Img

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
object LinkUtils {

    def newAvatar(avatarId: String): Img = new Img("", StorageFactory.getInstance.getAvatarPath(avatarId, 16)).setWidth("16").
        setHeight("16").setStyle("display: inline-block; vertical-align: top;")

    def generateUserAcceptLink(subDomain: String, accountId: Integer, username: String): String =
        "%suser/confirm_invite/%s".format(SiteConfiguration.getSiteUrl(subDomain), UrlEncodeDecoder.encode("%s/%s/%s".format(accountId,
            username, subDomain)))

    def generateUserDenyLink(subDomain: String, accountId: Integer, username: String, inviterName: String, inviterEmail: String) =
        "%suser/deny_invite/%s".format(SiteConfiguration.getSiteUrl(subDomain), UrlEncodeDecoder.encode("%s/%s/%s/%s/%s".format(accountId,
            username, inviterName, inviterEmail, subDomain)))
}
