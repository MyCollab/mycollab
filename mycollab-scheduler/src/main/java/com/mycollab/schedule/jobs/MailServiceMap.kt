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

import com.mycollab.core.MyCollabException
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.schedule.email.SendingRelayEmailNotificationAction
import com.mycollab.schedule.email.crm.*
import com.mycollab.schedule.email.project.*
import kotlin.reflect.KClass

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object MailServiceMap {
    private val serviceMap2 = mapOf(
            ProjectTypeConstants.BUG to BugRelayEmailNotificationAction::class.java,
            ProjectTypeConstants.BUG_COMPONENT to ComponentRelayEmailNotificationAction::class.java,
            ProjectTypeConstants.BUG_VERSION to VersionRelayEmailNotificationAction::class.java,
            ProjectTypeConstants.MESSAGE to MessageRelayEmailNotificationAction::class.java,
            ProjectTypeConstants.MILESTONE to ProjectMilestoneRelayEmailNotificationAction::class.java,
            ProjectTypeConstants.PAGE to ProjectPageRelayEmailNotificationAction::class.java,
            ProjectTypeConstants.PROJECT to ProjectRelayEmailNotificationAction::class.java,
            ProjectTypeConstants.RISK to ProjectRiskRelayEmailNotificationAction::class.java,
            ProjectTypeConstants.STANDUP to StandupRelayEmailNotificationAction::class.java,
            ProjectTypeConstants.TASK to ProjectTaskRelayEmailNotificationAction::class.java,
            CrmTypeConstants.ACCOUNT to AccountRelayEmailNotificationAction::class.java,
            CrmTypeConstants.CALL to CallRelayEmailNotificationAction::class.java,
            CrmTypeConstants.CAMPAIGN to CampaignRelayEmailNotificationAction::class.java,
            CrmTypeConstants.CASE to CaseRelayEmailNotificationAction::class.java,
            CrmTypeConstants.CONTACT to ContactRelayEmailNotificationAction::class.java,
            CrmTypeConstants.LEAD to LeadRelayEmailNotificationAction::class.java,
            CrmTypeConstants.MEETING to MeetingRelayEmailNotificationAction::class.java,
            CrmTypeConstants.OPPORTUNITY to OpportunityRelayEmailNotificationAction::class.java,
            CrmTypeConstants.TASK to TaskRelayEmailNotificationAction::class.java)

    fun service(typeVal: String): Class<out SendingRelayEmailNotificationAction> =
            serviceMap2[typeVal] ?: throw  MyCollabException("Can not find associate email action for $typeVal")

}