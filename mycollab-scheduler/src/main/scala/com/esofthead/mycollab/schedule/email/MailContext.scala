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
package com.esofthead.mycollab.schedule.email

import java.util.{Locale, TimeZone}

import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification
import com.esofthead.mycollab.configuration.LocaleHelper
import com.esofthead.mycollab.core.utils.TimezoneMapper
import com.esofthead.mycollab.i18n.LocalizationHelper
import com.esofthead.mycollab.module.user.domain.SimpleUser

import scala.annotation.varargs
import scala.beans.BeanProperty

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
class MailContext[B](@BeanProperty val emailNotification: SimpleRelayEmailNotification, @BeanProperty val
user: SimpleUser, val siteUrl: String) {
    @BeanProperty val locale: Locale = LocaleHelper.toLocale(user.getLanguage)
    @BeanProperty val timeZone: TimeZone = TimezoneMapper.getTimezone(user.getTimezone)
    @BeanProperty var wrappedBean: B = _

    def getSaccountid: Int = emailNotification.getSaccountid

    def getChangeByUserFullName: String = emailNotification.getChangeByUserFullName

    def getTypeid: String = emailNotification.getTypeid

    def getType: String = emailNotification.getType

    @varargs def getMessage(key: Enum[_], params: AnyRef*): String = LocalizationHelper.getMessage(locale, key, params: _*)
}
