/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.configuration.logging

import ch.qos.logback.classic.net.SMTPAppender
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Layout
import com.mycollab.configuration.EmailConfiguration
import com.mycollab.configuration.SiteConfiguration
import com.mycollab.core.Version
import com.mycollab.core.utils.StringUtils

/**
 * @author MyCollab Ltd
 * @since 5.0.5
 */
class MailAppender : SMTPAppender() {

    override fun makeSubjectLayout(subjectStr: String?): Layout<ILoggingEvent> {
        var subjectStr = subjectStr
        if (subjectStr == null) {
            subjectStr = "MyCollab ${Version.getVersion()} - Error: %logger{20} - %m"
        }

        return super.makeSubjectLayout(subjectStr)
    }


    override fun start() {
        val conf = SiteConfiguration.getEmailConfiguration()
        if (StringUtils.isBlank(conf.host)) {
            return
        }

        this.setSMTPHost(conf.host)
        this.setSMTPPort(conf.port!!)
        this.username = conf.user
        this.password = conf.password
        this.isSTARTTLS = conf.isStartTls
        this.from = conf.notifyEmail
        this.addTo(SiteConfiguration.getSendErrorEmail())
        super.start()
    }
}