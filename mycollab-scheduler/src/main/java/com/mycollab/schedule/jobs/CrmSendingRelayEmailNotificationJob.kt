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

import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.dao.RelayEmailNotificationMapper
import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.common.domain.criteria.RelayEmailNotificationSearchCriteria
import com.mycollab.common.service.RelayEmailNotificationService
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.spring.AppContextUtil
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
@Profile("program")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@DisallowConcurrentExecution
class CrmSendingRelayEmailNotificationJob : GenericQuartzJobBean() {
    companion object {
        private val LOG = LoggerFactory.getLogger(CrmSendingRelayEmailNotificationJob::class.java)
    }

    @Autowired private lateinit var relayEmailNotificationMapper: RelayEmailNotificationMapper
    override fun executeJob(context: JobExecutionContext) {
        val relayEmailService = AppContextUtil.getSpringBean(RelayEmailNotificationService::class.java)
        val criteria = RelayEmailNotificationSearchCriteria()
        criteria.types = SetSearchField(CrmTypeConstants.ACCOUNT, CrmTypeConstants.CONTACT,
                CrmTypeConstants.CAMPAIGN, CrmTypeConstants.LEAD, CrmTypeConstants.OPPORTUNITY, CrmTypeConstants.CASE,
                CrmTypeConstants.TASK, CrmTypeConstants.MEETING, CrmTypeConstants.CALL)

        val relayEmailNotifications = relayEmailService.findPageableListByCriteria(
                BasicSearchRequest(criteria, 0, Integer.MAX_VALUE)) as List<SimpleRelayEmailNotification>
        relayEmailNotifications.forEach {
            try {
                val mailServiceCls = MailServiceMap.service(it.type)
                val emailNotificationAction = AppContextUtil.getSpringBean(mailServiceCls)
                when (it.action) {
                    MonitorTypeConstants.CREATE_ACTION -> emailNotificationAction.sendNotificationForCreateAction(it)
                    MonitorTypeConstants.UPDATE_ACTION -> emailNotificationAction.sendNotificationForUpdateAction(it)
                    MonitorTypeConstants.ADD_COMMENT_ACTION -> emailNotificationAction.sendNotificationForCommentAction(it)
                }
            } catch (e: Exception) {
                LOG.error("Error while send the schedule command $it.type", e)
            } finally {
                relayEmailNotificationMapper.deleteByPrimaryKey(it.id)
            }
        }
    }
}