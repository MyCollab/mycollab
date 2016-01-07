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
package com.esofthead.mycollab.schedule.jobs

import com.esofthead.mycollab.common.MonitorTypeConstants
import com.esofthead.mycollab.common.dao.RelayEmailNotificationMapper
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification
import com.esofthead.mycollab.common.domain.criteria.RelayEmailNotificationSearchCriteria
import com.esofthead.mycollab.common.service.RelayEmailNotificationService
import com.esofthead.mycollab.core.arguments.{SearchRequest, SetSearchField}
import com.esofthead.mycollab.module.crm.CrmTypeConstants
import com.esofthead.mycollab.schedule.email.SendingRelayEmailNotificationAction
import com.esofthead.mycollab.spring.ApplicationContextUtil
import org.quartz.JobExecutionContext
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd.
  * @since 4.6.0
  */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class CrmSendingRelayEmailNotificationJob extends GenericQuartzJobBean {
  private val LOG: Logger = LoggerFactory.getLogger(classOf[CrmSendingRelayEmailNotificationJob])

  @Autowired private val relayEmailNotificationMapper: RelayEmailNotificationMapper = null

  @SuppressWarnings(Array("unchecked"))
  def executeJob(context: JobExecutionContext) {
    val relayEmailService: RelayEmailNotificationService = ApplicationContextUtil.getSpringBean(classOf[RelayEmailNotificationService])
    val criteria: RelayEmailNotificationSearchCriteria = new RelayEmailNotificationSearchCriteria
    criteria.setTypes(new SetSearchField[String](CrmTypeConstants.ACCOUNT, CrmTypeConstants.CONTACT,
      CrmTypeConstants.CAMPAIGN, CrmTypeConstants.LEAD, CrmTypeConstants.OPPORTUNITY, CrmTypeConstants.CASE,
      CrmTypeConstants.TASK, CrmTypeConstants.MEETING, CrmTypeConstants.CALL))

    import scala.collection.JavaConverters._
    val relayEmaiNotifications: List[SimpleRelayEmailNotification] = relayEmailService.findPagableListByCriteria(
      new SearchRequest[RelayEmailNotificationSearchCriteria](criteria, 0,
        Integer.MAX_VALUE)).asScala.toList.asInstanceOf[List[SimpleRelayEmailNotification]]
    var emailNotificationAction: SendingRelayEmailNotificationAction = null

    for (notification <- relayEmaiNotifications) {
      try {
        val mailServiceCls = MailServiceMap.service(notification.getType)
        if (mailServiceCls != null) {
          emailNotificationAction = ApplicationContextUtil.getSpringBean(mailServiceCls)

          if (emailNotificationAction != null) {
            notification.getAction match {
              case MonitorTypeConstants.CREATE_ACTION => emailNotificationAction.sendNotificationForCreateAction(notification)
              case MonitorTypeConstants.UPDATE_ACTION => emailNotificationAction.sendNotificationForUpdateAction(notification)
              case MonitorTypeConstants.ADD_COMMENT_ACTION => emailNotificationAction.sendNotificationForCommentAction(notification)
            }
          }
        }
      }
      catch {
        case ex: Exception => LOG.error("Error while send the schedule command " + notification.getType, ex)
      } finally {
        relayEmailNotificationMapper.deleteByPrimaryKey(notification.getId)
      }
    }
  }
}