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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.schedule.jobs

import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.slf4j.LoggerFactory
import org.springframework.dao.TransientDataAccessException
import org.springframework.scheduling.quartz.QuartzJobBean

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
abstract class GenericQuartzJobBean : QuartzJobBean() {
    companion object {
        private val LOG = LoggerFactory.getLogger(GenericQuartzJobBean::class.java)
        private var isSendingDbError = false
    }

    @Throws(JobExecutionException::class)
    abstract fun executeJob(context: JobExecutionContext)

    @Throws(JobExecutionException::class)
    override fun executeInternal(context: JobExecutionContext) {
        try {
            executeJob(context)
            isSendingDbError = false
        } catch (e: TransientDataAccessException) {
            if (isSendingDbError) {
                LOG.error("Exception in running schedule", e)
                isSendingDbError = true
            }
        } catch (e: Exception) {
            LOG.error("Exception in running schedule", e)
        }
    }
}

