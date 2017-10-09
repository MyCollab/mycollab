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