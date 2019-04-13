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
import com.mycollab.configuration.ServerConfiguration
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.core.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
@Service
class AppPropertiesServiceImpl : AppPropertiesService, InitializingBean {

    private lateinit var properties: Properties

    @Autowired
    private lateinit var serverConfiguration: ServerConfiguration

    override val sysId: String
        get() = properties.getProperty("id", UUID.randomUUID().toString() + LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())

    override val startDate: LocalDate
        get() {
            return try {
                val dateValue = properties.getProperty("startdate")
                DateTimeUtils.convertDateByString(dateValue, "yyyy-MM-dd'T'HH:mm:ss")
            } catch (e: Exception) {
                LocalDate.now()
            }

        }

    override val edition: String
        get() = "Community"

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        try {
            val homeFolder = serverConfiguration.getHomeDir()
            val sysFile = File(homeFolder, ".app.properties")
            properties = Properties()
            if (sysFile.isFile && sysFile.exists()) {
                properties.load(FileInputStream(sysFile))
                val startDate = properties.getProperty("startdate")
                if (startDate == null) {
                    properties.setProperty("startdate", DateTimeUtils.formatDateToW3C(LocalDate.now()))
                }
                properties.setProperty("edition", edition)
                properties.store(FileOutputStream(sysFile), "")
            } else {
                properties.setProperty("id", UUID.randomUUID().toString() + LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())
                properties.setProperty("startdate", DateTimeUtils.formatDateToW3C(LocalDate.now()))
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
