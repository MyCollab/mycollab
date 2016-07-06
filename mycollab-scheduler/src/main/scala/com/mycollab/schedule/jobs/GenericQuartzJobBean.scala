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
package com.mycollab.schedule.jobs

import org.quartz.{JobExecutionContext, JobExecutionException}
import org.slf4j.LoggerFactory
import org.springframework.dao.{DataAccessException, TransientDataAccessException}
import org.springframework.scheduling.quartz.QuartzJobBean

/**
  * @author MyCollab Ltd.
  * @since 4.6.0
  */
abstract class GenericQuartzJobBean extends QuartzJobBean {
    private val LOG = LoggerFactory.getLogger(classOf[GenericQuartzJobBean])

    @throws(classOf[JobExecutionException])
    protected def executeInternal(context: JobExecutionContext) {
        try {
            executeJob(context)
            GenericQuartzJobBean.isSendingDbError = false
        }
        catch {
            case e@(_: DataAccessException | _: TransientDataAccessException) => {
                if (!GenericQuartzJobBean.isSendingDbError) {
                    LOG.error("Exception in running schedule", e)
                    GenericQuartzJobBean.isSendingDbError = true
                }
            }
            case e: Exception => LOG.error("Exception in running schedule", e)
        }
    }

    @throws(classOf[JobExecutionException])
    protected def executeJob(context: JobExecutionContext): Unit
}

object GenericQuartzJobBean {
    private var isSendingDbError = false
}
