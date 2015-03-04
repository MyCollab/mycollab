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
import com.esofthead.mycollab.common.service.RelayEmailNotificationService
import com.esofthead.mycollab.module.project.domain.ProjectRelayEmailNotification
import com.esofthead.mycollab.module.project.service.ProjectService
import com.esofthead.mycollab.schedule.email.SendingRelayEmailNotificationAction
import com.esofthead.mycollab.spring.ApplicationContextUtil
import org.quartz.JobExecutionContext
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class ProjectSendingRelayEmailNotificationJob extends GenericQuartzJobBean {
  private val LOG: Logger = LoggerFactory.getLogger(classOf[ProjectSendingRelayEmailNotificationJob])

  def executeJob(context: JobExecutionContext) {
    val projectService: ProjectService = ApplicationContextUtil.getSpringBean(classOf[ProjectService])
    import scala.collection.JavaConverters._
    val relayEmaiNotifications: List[ProjectRelayEmailNotification] = projectService.findProjectRelayEmailNotifications.asScala.toList
    val relayNotificationService: RelayEmailNotificationService = ApplicationContextUtil.getSpringBean(classOf[RelayEmailNotificationService])
    var emailNotificationAction: SendingRelayEmailNotificationAction = null
    for (notification <- relayEmaiNotifications) {
      try {
        if (notification.getEmailhandlerbean != null) {
          emailNotificationAction = ApplicationContextUtil.getSpringBean(Class.forName(notification.getEmailhandlerbean)).asInstanceOf[SendingRelayEmailNotificationAction]
          if (emailNotificationAction != null) {
            if (MonitorTypeConstants.CREATE_ACTION == notification.getAction) {
              emailNotificationAction.sendNotificationForCreateAction(notification)
            }
            else if (MonitorTypeConstants.UPDATE_ACTION == notification.getAction) {
              emailNotificationAction.sendNotificationForUpdateAction(notification)
            }
            else if (MonitorTypeConstants.ADD_COMMENT_ACTION == notification.getAction) {
              emailNotificationAction.sendNotificationForCommentAction(notification)
            }
          }
        }
      }
      catch {
        case ex: Exception => LOG.error("Error while sending scheduler command", ex)
      } finally {
        relayNotificationService.removeWithSession(notification.getId, "", notification.getSaccountid)
      }
    }
  }
}
