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

import com.mycollab.schedule.email.SendingRelayEmailNotificationAction
import com.mycollab.schedule.email.crm.{LeadRelayEmailNotificationAction, MeetingRelayEmailNotificationAction, OpportunityRelayEmailNotificationAction, TaskRelayEmailNotificationAction, _}
import com.mycollab.schedule.email.project._
import com.mycollab.core.MyCollabException
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.project.ProjectTypeConstants

/**
  * @author MyCollab Ltd
  * @since 5.2.5
  */
object MailServiceMap {
  private val serviceMap2: Map[String, Class[_ <: SendingRelayEmailNotificationAction]] = Map(
    ProjectTypeConstants.BUG -> classOf[BugRelayEmailNotificationAction],
    ProjectTypeConstants.BUG_COMPONENT -> classOf[ComponentRelayEmailNotificationAction],
    ProjectTypeConstants.BUG_VERSION -> classOf[VersionRelayEmailNotificationAction],
    ProjectTypeConstants.MESSAGE -> classOf[MessageRelayEmailNotificationAction],
    ProjectTypeConstants.MILESTONE -> classOf[ProjectMilestoneRelayEmailNotificationAction],
    ProjectTypeConstants.PAGE -> classOf[ProjectPageRelayEmailNotificationAction],
    ProjectTypeConstants.PROJECT -> classOf[ProjectRelayEmailNotificationAction],
    ProjectTypeConstants.RISK -> classOf[ProjectRiskRelayEmailNotificationAction],
    ProjectTypeConstants.STANDUP -> classOf[StandupRelayEmailNotificationAction],
    ProjectTypeConstants.TASK -> classOf[ProjectTaskRelayEmailNotificationAction],
    CrmTypeConstants.ACCOUNT -> classOf[AccountRelayEmailNotificationAction],
    CrmTypeConstants.CALL -> classOf[CallRelayEmailNotificationAction],
    CrmTypeConstants.CAMPAIGN -> classOf[CampaignRelayEmailNotificationAction],
    CrmTypeConstants.CASE -> classOf[CaseRelayEmailNotificationAction],
    CrmTypeConstants.CONTACT -> classOf[ContactRelayEmailNotificationAction],
    CrmTypeConstants.LEAD -> classOf[LeadRelayEmailNotificationAction],
    CrmTypeConstants.MEETING -> classOf[MeetingRelayEmailNotificationAction],
    CrmTypeConstants.OPPORTUNITY -> classOf[OpportunityRelayEmailNotificationAction],
    CrmTypeConstants.TASK -> classOf[TaskRelayEmailNotificationAction])

  def service(typeVal: String): Class[_ <: SendingRelayEmailNotificationAction] = {
    val actionCls = serviceMap2(typeVal)
    if (actionCls == null) {
      throw new MyCollabException("Can not find associate email action for " + typeVal)
    }
    return actionCls;
  }
}
