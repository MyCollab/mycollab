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
package com.mycollab.community.common.service

import com.mycollab.common.service.AppPropertiesService
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.core.utils.FileUtils
import org.joda.time.LocalDateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date
import java.util.GregorianCalendar
import java.util.Properties
import java.util.UUID

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
@Service
class AppPropertiesServiceImpl : AppPropertiesService, InitializingBean {

    private lateinit var properties: Properties

    override val sysId: String
        get() = properties.getProperty("id", UUID.randomUUID().toString() + LocalDateTime().millisOfSecond)

    override val startDate: Date
        get() {
            return try {
                val dateValue = properties.getProperty("startdate")
                DateTimeUtils.convertDateByString(dateValue, "yyyy-MM-dd'T'HH:mm:ss")
            } catch (e: Exception) {
                GregorianCalendar().time
            }

        }

    override val edition: String
        get() = "Community"

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        try {
            val homeFolder = FileUtils.homeFolder
            val sysFile = File(homeFolder, ".app.properties")
            properties = Properties()
            if (sysFile.isFile && sysFile.exists()) {
                properties.load(FileInputStream(sysFile))
                val startDate = properties.getProperty("startdate")
                if (startDate == null) {
                    properties.setProperty("startdate", DateTimeUtils.formatDateToW3C(GregorianCalendar().time))
                }
                properties.setProperty("edition", edition)
                properties.store(FileOutputStream(sysFile), "")
            } else {
                properties.setProperty("id", UUID.randomUUID().toString() + LocalDateTime().millisOfSecond)
                properties.setProperty("startdate", DateTimeUtils.formatDateToW3C(GregorianCalendar().time))
                properties.setProperty("edition", edition)
                properties.store(FileOutputStream(sysFile), "")
            }
        } catch (e: IOException) {
            LOG.error("Error", e)
        }

    }

    companion object {
        private val LOG = LoggerFactory.getLogger(AppPropertiesServiceImpl::class.java)
    }
}
