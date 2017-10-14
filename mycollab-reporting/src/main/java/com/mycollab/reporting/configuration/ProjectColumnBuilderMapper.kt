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
package com.mycollab.reporting.configuration

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.domain.*
import com.mycollab.module.project.i18n.OptionI18nEnum
import com.mycollab.module.tracker.domain.SimpleBug
import com.mycollab.module.tracker.domain.SimpleComponent
import com.mycollab.module.tracker.domain.SimpleVersion
import com.mycollab.module.tracker.domain.Version
import com.mycollab.reporting.ColumnBuilderClassMapper
import com.mycollab.reporting.ReportStyles
import com.mycollab.reporting.expression.DateExpression
import com.mycollab.reporting.expression.HumanTimeExpression
import com.mycollab.reporting.expression.I18nExpression
import com.mycollab.reporting.expression.PrimaryTypeFieldExpression
import com.mycollab.reporting.generator.ComponentBuilderGenerator
import com.mycollab.reporting.generator.HtmlBuilderGenerator
import com.mycollab.reporting.generator.HyperlinkBuilderGenerator
import com.mycollab.reporting.generator.SimpleExpressionBuilderGenerator
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression
import net.sf.dynamicreports.report.builder.component.ComponentBuilder
import net.sf.dynamicreports.report.definition.ReportParameters
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

import java.util.HashMap

import net.sf.dynamicreports.report.builder.DynamicReports.cmp

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
@Component
class ProjectColumnBuilderMapper : InitializingBean {

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        ColumnBuilderClassMapper.put(SimpleProject::class.java, buildProjectMap())
        ColumnBuilderClassMapper.put(SimpleMilestone::class.java, buildMilestoneMap())
        ColumnBuilderClassMapper.put(ProjectTicket::class.java, buildTicketMap())
        ColumnBuilderClassMapper.put(SimpleTask::class.java, buildTaskMap())
        ColumnBuilderClassMapper.put(SimpleBug::class.java, buildBugMap())
        ColumnBuilderClassMapper.put(SimpleComponent::class.java, buildComponentMap())
        ColumnBuilderClassMapper.put(SimpleVersion::class.java, buildVersionMap())
        ColumnBuilderClassMapper.put(SimpleRisk::class.java, buildRiskMap())
        ColumnBuilderClassMapper.put(SimpleProjectRole::class.java, buildRoleMap())
        ColumnBuilderClassMapper.put(SimpleProjectMember::class.java, buildProjectMemberMap())
        ColumnBuilderClassMapper.put(SimpleItemTimeLogging::class.java, buildTimeTrackingMap())
        ColumnBuilderClassMapper.put(FollowingTicket::class.java, buildTFollowingTicketMap())
    }

    private fun buildProjectMap(): Map<String, ComponentBuilderGenerator> {
        val map = HashMap<String, ComponentBuilderGenerator>()
        val projectNameExpr = PrimaryTypeFieldExpression<String>(Project.Field.name.name)
        val projectHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val projectId = reportParameters.getFieldValue<Int>(Project.Field.id.name)
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                return ProjectLinkGenerator.generateProjectFullLink(siteUrl, projectId!!)
            }
        }
        map.put(Milestone.Field.name.name, HyperlinkBuilderGenerator(projectNameExpr, projectHrefExpr))

        val leadNameExpr = PrimaryTypeFieldExpression<String>(SimpleProject.Field.leadFullName.name)
        val leadHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val projectId = reportParameters.getFieldValue<Int>(Project.Field.id.name)
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                val memberName = reportParameters.getParameterValue<String>(Project.Field.lead.name)
                return ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, projectId!!, memberName)
            }
        }
        map.put(Project.Field.lead.name, HyperlinkBuilderGenerator(leadNameExpr, leadHrefExpr))

        val accountNameExpr = PrimaryTypeFieldExpression<String>(SimpleProject.Field.clientName.name)
        val clientHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                val accountId = reportParameters.getParameterValue<Int>(Project.Field.accountid.name)
                return ProjectLinkGenerator.generateClientPreviewFullLink(siteUrl, accountId!!)
            }
        }
        map.put(Project.Field.accountid.name, HyperlinkBuilderGenerator(accountNameExpr, clientHrefExpr))

        val homePageUrlExpr = PrimaryTypeFieldExpression<String>(Project.Field.homepage.name)
        map.put(Project.Field.homepage.name, HyperlinkBuilderGenerator(homePageUrlExpr, homePageUrlExpr))

        map.put(Project.Field.createdtime.name, SimpleExpressionBuilderGenerator(DateExpression(Project.Field.createdtime.name)))
        map.put(Project.Field.planstartdate.name, SimpleExpressionBuilderGenerator(DateExpression(Project.Field.planstartdate.name)))
        map.put(Project.Field.planenddate.name, SimpleExpressionBuilderGenerator(DateExpression(Project.Field.planenddate.name)))
        return map
    }

    private fun buildMilestoneMap(): Map<String, ComponentBuilderGenerator> {
        LOG.debug("Build report mapper for project::milestone module")
        val map = HashMap<String, ComponentBuilderGenerator>()
        val milestoneNameExpr = PrimaryTypeFieldExpression<String>(Milestone.Field.name.name)
        val milestoneHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val milestoneId = reportParameters.getFieldValue<Int>(Milestone.Field.id.name)
                val projectId = reportParameters.getFieldValue<Int>(Milestone.Field.projectid.name)
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                return ProjectLinkGenerator.generateMilestonePreviewFullLink(siteUrl, projectId!!, milestoneId!!)
            }
        }
        map.put(Milestone.Field.name.name, HyperlinkBuilderGenerator(milestoneNameExpr, milestoneHrefExpr))
        map.put(Milestone.Field.status.name, SimpleExpressionBuilderGenerator(I18nExpression("status",
                OptionI18nEnum.MilestoneStatus::class.java)))
        map.put(Milestone.Field.startdate.name, SimpleExpressionBuilderGenerator(DateExpression(Milestone.Field.startdate.name)))
        map.put(Milestone.Field.enddate.name, SimpleExpressionBuilderGenerator(DateExpression(Milestone.Field.enddate.name)))
        val assigneeTitleExpr = PrimaryTypeFieldExpression<String>(SimpleMilestone.Field.ownerFullName.name)
        val assigneeHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val assignUser = reportParameters.getFieldValue<String>(Milestone.Field.assignuser.name)
                return when {
                    assignUser != null -> {
                        val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                        val projectId = reportParameters.getFieldValue<Int>(Milestone.Field.projectid.name)
                        ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, projectId!!, assignUser)
                    }
                    else -> ""
                }

            }
        }

        map.put(SimpleMilestone.Field.ownerFullName.name, HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr))

        val progressExpr = object : AbstractSimpleExpression<Double>() {
            override fun evaluate(reportParameters: ReportParameters): Double {
                val numOpenBugs = reportParameters.getFieldValue<Int>(SimpleMilestone.Field.numOpenBugs.name)
                val numBugs = reportParameters.getFieldValue<Int>(SimpleMilestone.Field.numBugs.name)
                val numOpenTasks = reportParameters.getFieldValue<Int>(SimpleMilestone.Field.numOpenTasks.name)
                val numTasks = reportParameters.getFieldValue<Int>(SimpleMilestone.Field.numTasks.name)
                val openAssignments = numOpenBugs!! + numOpenTasks!!
                val totalAssignments = numBugs!! + numTasks!!
                return if (totalAssignments > 0) (totalAssignments - openAssignments) * 1.0 / totalAssignments * 100.0 else 100.0
            }
        }
        map.put(Milestone.Field.id.name, SimpleExpressionBuilderGenerator(progressExpr))


        map.put(SimpleMilestone.Field.totalBillableHours.name, SimpleExpressionBuilderGenerator(object : AbstractSimpleExpression<Double>() {
            override fun evaluate(reportParameters: ReportParameters): Double? {
                val taskBillableHours = reportParameters.getFieldValue<Double>(SimpleMilestone.Field.totalTaskBillableHours.name)
                val bugBillableHours = reportParameters.getFieldValue<Double>(SimpleMilestone.Field.totalBugBillableHours.name)
                return taskBillableHours!! + bugBillableHours!!
            }
        }))
        map.put(SimpleMilestone.Field.totalNonBillableHours.name, SimpleExpressionBuilderGenerator(object : AbstractSimpleExpression<Double>() {
            override fun evaluate(reportParameters: ReportParameters): Double? {
                val taskNonBillableHours = reportParameters.getFieldValue<Double>(SimpleMilestone.Field.totalTaskNonBillableHours.name)
                val bugNonBillableHours = reportParameters.getFieldValue<Double>(SimpleMilestone.Field.totalBugNonBillableHours.name)
                return taskNonBillableHours!! + bugNonBillableHours!!
            }
        }))
        return map
    }

    private fun buildTicketMap(): Map<String, ComponentBuilderGenerator> {
        LOG.debug("Build report mapper for project::ticket module")
        val map = HashMap<String, ComponentBuilderGenerator>()
        val ticketTitleExpr = PrimaryTypeFieldExpression<String>("name")
        val ticketHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val extraTypeId = reportParameters.getFieldValue<Int>("extraTypeId")
                val projectId = reportParameters.getFieldValue<Int>("projectId")
                val type = reportParameters.getFieldValue<String>("type")
                val typeId = reportParameters.getFieldValue<Int>("typeId")
                val projectShortName = reportParameters.getFieldValue<String>("projectShortName")
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                return if (ProjectTypeConstants.BUG == type || ProjectTypeConstants.TASK == type) {
                    """$siteUrl${ProjectLinkGenerator.generateProjectItemLink(projectShortName, projectId!!, type, extraTypeId!!.toString() + "")}"""
                } else {
                    """$siteUrl${ProjectLinkGenerator.generateProjectItemLink(projectShortName, projectId!!, type, typeId!!.toString() + "")}"""
                }
            }
        }
        map.put("name", HyperlinkBuilderGenerator(ticketTitleExpr, ticketHrefExpr))
        map.put("startDate", SimpleExpressionBuilderGenerator(DateExpression("startDate")))
        map.put("endDate", SimpleExpressionBuilderGenerator(DateExpression("endDate")))
        map.put("dueDate", SimpleExpressionBuilderGenerator(DateExpression("dueDate")))
        map.put("description", HtmlBuilderGenerator(PrimaryTypeFieldExpression<String>("description")))

        val assigneeTitleExpr = PrimaryTypeFieldExpression<String>("assignUserFullName")
        val assigneeHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val assignUser = reportParameters.getFieldValue<String>("assignUser")
                if (assignUser != null) {
                    val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                    val projectId = reportParameters.getFieldValue<Int>("projectId")
                    return ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, projectId!!, assignUser)
                }

                return ""
            }
        }
        map.put("assignUserFullName", HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr))

        val createdUserTitleExpr = PrimaryTypeFieldExpression<String>("createdUserFullName")
        val createdUserHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val createdUser = reportParameters.getFieldValue<String>("createdUser")
                if (createdUser != null) {
                    val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                    val projectId = reportParameters.getFieldValue<Int>("projectId")
                    return ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, projectId!!, createdUser)
                }

                return ""
            }
        }
        map.put("createdUserFullName", HyperlinkBuilderGenerator(createdUserTitleExpr, createdUserHrefExpr))

        val milestoneTitleExpr = PrimaryTypeFieldExpression<String>("milestoneName")
        val milestoneHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val milestoneId = reportParameters.getFieldValue<Int>("milestoneId")
                if (milestoneId != null) {
                    val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                    val projectId = reportParameters.getFieldValue<Int>("projectId")
                    return ProjectLinkGenerator.generateMilestonePreviewFullLink(siteUrl, projectId!!, milestoneId)
                }

                return ""
            }
        }
        map.put("milestoneName", HyperlinkBuilderGenerator(milestoneTitleExpr, milestoneHrefExpr))

        return map
    }

    private fun buildTaskMap(): Map<String, ComponentBuilderGenerator> {
        LOG.debug("Build report mapper for project::task module")
        val map = HashMap<String, ComponentBuilderGenerator>()
        val taskNameTitleExpr = PrimaryTypeFieldExpression<String>(Task.Field.name.name)
        val taskNameHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val taskKey = reportParameters.getFieldValue<Int>(Task.Field.taskkey.name)
                val projectShortName = reportParameters.getFieldValue<String>("projectShortname")
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                return ProjectLinkGenerator.generateTaskPreviewFullLink(siteUrl, taskKey, projectShortName)
            }
        }
        map.put(Task.Field.name.name, HyperlinkBuilderGenerator(taskNameTitleExpr, taskNameHrefExpr))
        map.put(Task.Field.startdate.name, SimpleExpressionBuilderGenerator(DateExpression(Task.Field.startdate.name)))
        map.put(Task.Field.enddate.name, SimpleExpressionBuilderGenerator(DateExpression(Task.Field.enddate.name)))
        map.put(Task.Field.duedate.name, SimpleExpressionBuilderGenerator(DateExpression(Task.Field.duedate.name)))
        map.put(Task.Field.status.name, SimpleExpressionBuilderGenerator(I18nExpression("status", StatusI18nEnum::class.java)))

        val milestoneTitleExpr = PrimaryTypeFieldExpression<String>(SimpleTask.Field.milestoneName.name)
        val milestoneHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val milestoneId = reportParameters.getFieldValue<Int>("milestoneid")
                return when {
                    milestoneId != null -> {
                        val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                        val projectId = reportParameters.getFieldValue<Int>("projectid")
                        ProjectLinkGenerator.generateMilestonePreviewFullLink(siteUrl, projectId!!, milestoneId)
                    }
                    else -> ""
                }

            }
        }
        map.put(SimpleTask.Field.milestoneName.name, HyperlinkBuilderGenerator(milestoneTitleExpr, milestoneHrefExpr))

        val logUserTitleExpr = PrimaryTypeFieldExpression<String>(SimpleTask.Field.logByFullName.name)
        val logUserHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val logByUser = reportParameters.getFieldValue<String>("logby")
                if (logByUser != null) {
                    val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                    val projectId = reportParameters.getFieldValue<Int>("projectid")
                    return ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, projectId!!, logByUser)
                }

                return ""
            }
        }
        map.put(SimpleTask.Field.logByFullName.name, HyperlinkBuilderGenerator(logUserTitleExpr, logUserHrefExpr))

        val assigneeTitleExpr = PrimaryTypeFieldExpression<String>(SimpleTask.Field.assignUserFullName.name)
        val assigneeHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val assignUser = reportParameters.getFieldValue<String>("assignuser")
                return when {
                    assignUser != null -> {
                        val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                        val projectId = reportParameters.getFieldValue<Int>("projectid")
                        ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, projectId!!, assignUser)
                    }
                    else -> ""
                }

            }
        }

        map.put(SimpleTask.Field.assignUserFullName.name, HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr))
        return map
    }

    private fun buildBugMap(): Map<String, ComponentBuilderGenerator> {
        LOG.debug("Build report mapper for project::bug module")

        val map = HashMap<String, ComponentBuilderGenerator>()
        val summaryTitleExpr = PrimaryTypeFieldExpression<String>("name")
        val summaryHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val bugKey = reportParameters.getFieldValue<Int>("bugkey")
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                val projectShortName = reportParameters.getFieldValue<String>("projectShortName")
                return ProjectLinkGenerator.generateBugPreviewFullLink(siteUrl, bugKey, projectShortName)
            }
        }
        map.put("name", HyperlinkBuilderGenerator(summaryTitleExpr, summaryHrefExpr))

        val assigneeTitleExpr = PrimaryTypeFieldExpression<String>("assignuserFullName")
        val assigneeHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val assignUser = reportParameters.getFieldValue<String>("assignuser")
                if (assignUser != null) {
                    val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                    val projectId = reportParameters.getFieldValue<Int>("projectid")
                    return ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, projectId!!, assignUser)
                }

                return ""
            }
        }

        val logUserTitleExpr = PrimaryTypeFieldExpression<String>("loguserFullName")
        val logUserHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val logUser = reportParameters.getFieldValue<String>("logby")
                if (logUser != null) {
                    val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                    val projectId = reportParameters.getFieldValue<Int>("projectid")
                    return ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, projectId!!, logUser)
                }

                return ""
            }
        }

        val milestoneTitleExpr = PrimaryTypeFieldExpression<String>(SimpleBug.Field.milestoneName.name)
        val milestoneHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val milestoneId = reportParameters.getFieldValue<Int>("milestoneid")
                return when {
                    milestoneId != null -> {
                        val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                        val projectId = reportParameters.getFieldValue<Int>("projectid")
                        ProjectLinkGenerator.generateMilestonePreviewFullLink(siteUrl, projectId!!, milestoneId)
                    }
                    else -> ""
                }

            }
        }
        map.put(SimpleBug.Field.milestoneName.name, HyperlinkBuilderGenerator(milestoneTitleExpr, milestoneHrefExpr))

        map.put("severity", SimpleExpressionBuilderGenerator(I18nExpression("severity", OptionI18nEnum.BugSeverity::class.java)))
        map.put("priority", SimpleExpressionBuilderGenerator(I18nExpression("priority", OptionI18nEnum.Priority::class.java)))
        map.put("status", SimpleExpressionBuilderGenerator(I18nExpression("status", OptionI18nEnum.BugStatus::class.java)))
        map.put("resolution", SimpleExpressionBuilderGenerator(I18nExpression("resolution", OptionI18nEnum.BugResolution::class.java)))
        map.put("assignuserFullName", HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr))
        map.put("loguserFullName", HyperlinkBuilderGenerator(logUserTitleExpr, logUserHrefExpr))
        map.put("duedate", SimpleExpressionBuilderGenerator(DateExpression("duedate")))
        map.put("startdate", SimpleExpressionBuilderGenerator(DateExpression("startdate")))
        map.put("enddate", SimpleExpressionBuilderGenerator(DateExpression("enddate")))
        map.put("billableHours", SimpleExpressionBuilderGenerator(HumanTimeExpression("billableHours")))
        map.put("nonBillableHours", SimpleExpressionBuilderGenerator(HumanTimeExpression("nonBillableHours")))
        return map
    }

    private fun buildComponentMap(): Map<String, ComponentBuilderGenerator> {
        LOG.debug("Build report mapper for project::component module")

        val map = HashMap<String, ComponentBuilderGenerator>()
        val summaryTitleExpr = PrimaryTypeFieldExpression<String>(com.mycollab.module.tracker.domain
                .Component.Field.name.name)
        val summaryHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val componentId = reportParameters.getFieldValue<Int>("id")
                val projectId = reportParameters.getFieldValue<Int>("projectid")
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                return ProjectLinkGenerator.generateBugComponentPreviewFullLink(siteUrl, projectId!!, componentId!!)
            }
        }
        map.put("name", HyperlinkBuilderGenerator(summaryTitleExpr, summaryHrefExpr))

        val assigneeTitleExpr = PrimaryTypeFieldExpression<String>("userLeadFullName")
        val assigneeHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val assignUser = reportParameters.getFieldValue<String>("userlead")
                if (assignUser != null) {
                    val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                    val projectId = reportParameters.getFieldValue<Int>("projectid")
                    return ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, projectId!!, assignUser)
                }

                return ""
            }
        }

        map.put("userLeadFullName", HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr))
        val progressExpr = object : AbstractSimpleExpression<Double>() {
            override fun evaluate(reportParameters: ReportParameters): Double {
                val numOpenBugs = reportParameters.getFieldValue<Int>(SimpleComponent.Field.numOpenBugs.name)
                val numBugs = reportParameters.getFieldValue<Int>(SimpleComponent.Field.numBugs.name)
                return if (numBugs != null && numBugs != 0) (numBugs - numOpenBugs!!) * 1.0 / numBugs * 100.0 else 100.0
            }
        }
        map.put("id", SimpleExpressionBuilderGenerator(progressExpr))
        return map
    }

    private fun buildVersionMap(): Map<String, ComponentBuilderGenerator> {
        LOG.debug("Build report mapper for project::version module")

        val map = HashMap<String, ComponentBuilderGenerator>()
        val summaryTitleExpr = PrimaryTypeFieldExpression<String>(Version.Field.name.name)
        val summaryHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val versionId = reportParameters.getFieldValue<Int>("id")
                val projectId = reportParameters.getFieldValue<Int>("projectid")
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                return ProjectLinkGenerator.generateBugVersionPreviewFullLink(siteUrl, projectId!!, versionId!!)
            }
        }
        map.put(Version.Field.name.name, HyperlinkBuilderGenerator(summaryTitleExpr, summaryHrefExpr))
        map.put("duedate", SimpleExpressionBuilderGenerator(DateExpression("duedate")))
        val progressExpr = object : AbstractSimpleExpression<Double>() {
            override fun evaluate(reportParameters: ReportParameters): Double {
                val numOpenBugs = reportParameters.getFieldValue<Int>(SimpleVersion.Field.numOpenBugs.name)
                val numBugs = reportParameters.getFieldValue<Int>(SimpleVersion.Field.numBugs.name)
                return if (numBugs != null && numBugs != 0) (numBugs - numOpenBugs!!) * 1.0 / numBugs * 100.0 else 100.0
            }
        }
        map.put("id", SimpleExpressionBuilderGenerator(progressExpr))
        return map
    }

    private fun buildRiskMap(): Map<String, ComponentBuilderGenerator> {
        LOG.debug("Build report mapper for project::risk module")

        val map = HashMap<String, ComponentBuilderGenerator>()
        val summaryTitleExpr = PrimaryTypeFieldExpression<String>(Risk.Field.name.name)
        val summaryHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val riskId = reportParameters.getFieldValue<Int>(Risk.Field.id.name)
                val projectId = reportParameters.getFieldValue<Int>(Risk.Field.projectid.name)
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                return ProjectLinkGenerator.generateRiskPreviewFullLink(siteUrl, projectId!!, riskId!!)
            }
        }
        map.put(Risk.Field.name.name, HyperlinkBuilderGenerator(summaryTitleExpr, summaryHrefExpr))

        val assigneeTitleExpr = PrimaryTypeFieldExpression<String>(SimpleRisk.Field.assignedToUserFullName.name)
        val assigneeHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val assignUser = reportParameters.getFieldValue<String>(Risk.Field.assignuser.name)
                if (assignUser != null) {
                    val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                    val projectId = reportParameters.getFieldValue<Int>("projectid")
                    return ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, projectId!!, assignUser)
                }

                return ""
            }
        }

        map.put(SimpleRisk.Field.assignedToUserFullName.name, HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr))

        map.put(Risk.Field.status.name, SimpleExpressionBuilderGenerator(I18nExpression("status", StatusI18nEnum::class.java)))
        map.put(Risk.Field.priority.name, SimpleExpressionBuilderGenerator(I18nExpression("priority", OptionI18nEnum.Priority::class.java)))
        map.put(Risk.Field.duedate.name, SimpleExpressionBuilderGenerator(DateExpression(Risk.Field.duedate.name)))

        return map
    }

    private fun buildProjectMemberMap(): Map<String, ComponentBuilderGenerator> {
        val map = HashMap<String, ComponentBuilderGenerator>()
        val memberNameExpr = PrimaryTypeFieldExpression<String>(SimpleProjectMember.Field.memberFullName.name)
        val memberHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val projectId = reportParameters.getFieldValue<Int>(ProjectMember.Field.projectid.name)
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                val username = reportParameters.getParameterValue<String>(ProjectMember.Field.username.name)
                return ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, projectId!!, username)
            }
        }
        map.put(ProjectMember.Field.username.name, HyperlinkBuilderGenerator(memberNameExpr, memberHrefExpr))

        val roleNameExpr = PrimaryTypeFieldExpression<String>(SimpleProjectMember.Field.roleName.name)
        val roleHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val projectId = reportParameters.getFieldValue<Int>(ProjectMember.Field.projectid.name)
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                val roleId = reportParameters.getParameterValue<Int>(ProjectMember.Field.projectroleid.name)
                return ProjectLinkGenerator.generateRolePreviewFullLink(siteUrl, projectId!!, roleId)
            }
        }
        map.put(ProjectMember.Field.projectroleid.name, HyperlinkBuilderGenerator(roleNameExpr, roleHrefExpr))
        return map
    }

    private fun buildRoleMap(): Map<String, ComponentBuilderGenerator> {
        LOG.debug("Build report mapper for project::role module")

        val map = HashMap<String, ComponentBuilderGenerator>()
        val summaryTitleExpr = PrimaryTypeFieldExpression<String>("rolename")
        val summaryHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val roleId = reportParameters.getFieldValue<Int>("id")
                val projectId = reportParameters.getFieldValue<Int>(ProjectRole.Field.projectid.name)
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                return ProjectLinkGenerator.generateRolePreviewFullLink(siteUrl, projectId!!, roleId)
            }
        }
        map.put("rolename", HyperlinkBuilderGenerator(summaryTitleExpr, summaryHrefExpr))
        return map
    }

    private fun buildTimeTrackingMap(): Map<String, ComponentBuilderGenerator> {
        LOG.debug("Build report mapper for project::timetracking module")

        val map = HashMap<String, ComponentBuilderGenerator>()
        val logUserTitleExpr = PrimaryTypeFieldExpression<String>("logUserFullName")
        val logUserHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val assignUser = reportParameters.getFieldValue<String>("loguser")
                if (assignUser != null) {
                    val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                    val projectId = reportParameters.getFieldValue<Int>("projectid")
                    return ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, projectId!!, assignUser)
                }

                return ""
            }
        }

        map.put(SimpleItemTimeLogging.Field.logUserFullName.name, HyperlinkBuilderGenerator(logUserTitleExpr, logUserHrefExpr))

        val projectTitleExpr = PrimaryTypeFieldExpression<String>("projectName")
        val projectHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val projectId = reportParameters.getFieldValue<Int>("projectid")
                if (projectId != null) {
                    val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                    return ProjectLinkGenerator.generateProjectFullLink(siteUrl, projectId)
                }

                return ""
            }
        }

        map.put(SimpleItemTimeLogging.Field.projectName.name, HyperlinkBuilderGenerator(projectTitleExpr, projectHrefExpr))
        map.put(ItemTimeLogging.Field.logforday.name, SimpleExpressionBuilderGenerator(DateExpression(ItemTimeLogging.Field.logforday.name)))

        val overtimeExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(param: ReportParameters): String {
                val level = param.getFieldValue<Boolean>(ItemTimeLogging.Field.isovertime.name)
                return if (java.lang.Boolean.TRUE == level) "Yes" else "No"
            }
        }
        map.put(ItemTimeLogging.Field.isovertime.name, SimpleExpressionBuilderGenerator(overtimeExpr))

        val billingExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(param: ReportParameters): String {
                val level = param.getFieldValue<Boolean>(ItemTimeLogging.Field.isbillable.name)
                return if (java.lang.Boolean.TRUE == level) "Yes" else "No"
            }
        }
        map.put(ItemTimeLogging.Field.isbillable.name, SimpleExpressionBuilderGenerator(billingExpr))

        val summaryTitleExpr = PrimaryTypeFieldExpression<String>(SimpleItemTimeLogging.Field.summary.name)
        val summaryHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val type = reportParameters.getFieldValue<String>("type")
                val typeId = reportParameters.getFieldValue<Int>("typeid")
                val projectId = reportParameters.getFieldValue<Int>("projectid")
                val projectShortName = reportParameters.getFieldValue<String>("projectShortName")
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")

                return when (type) {
                    null -> ""
                    ProjectTypeConstants.BUG -> ProjectLinkGenerator.generateBugPreviewFullLink(siteUrl, typeId, projectShortName)
                    ProjectTypeConstants.TASK -> ProjectLinkGenerator.generateTaskPreviewFullLink(siteUrl, typeId, projectShortName)
                    ProjectTypeConstants.RISK -> ProjectLinkGenerator.generateRiskPreviewFullLink(siteUrl, projectId!!, typeId!!)
                    else -> type
                }
            }
        }

        val noteExpr = PrimaryTypeFieldExpression<String>(ItemTimeLogging.Field.note.name)

        map.put(SimpleItemTimeLogging.Field.summary.name, object : ComponentBuilderGenerator {
            override fun getCompBuilder(reportStyles: ReportStyles): ComponentBuilder<*, *> =
                    cmp.verticalList(HyperlinkBuilderGenerator(summaryTitleExpr, summaryHrefExpr)
                            .getCompBuilder(reportStyles), cmp.text(noteExpr))
        })
        return map
    }

    private fun buildTFollowingTicketMap(): Map<String, ComponentBuilderGenerator> {
        LOG.debug("Build report mapper for project::following ticket module")

        val map = HashMap<String, ComponentBuilderGenerator>()
        val summaryTitleExpr = PrimaryTypeFieldExpression<String>("name")
        val summaryHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val type = reportParameters.getFieldValue<String>("type")
                val typeId = reportParameters.getFieldValue<Int>("typeId")
                val projectShortName = reportParameters.getFieldValue<String>("projectShortName")
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")

                return when (type) {
                    null -> ""
                    ProjectTypeConstants.BUG -> ProjectLinkGenerator.generateBugPreviewFullLink(siteUrl, typeId, projectShortName)
                    ProjectTypeConstants.TASK -> ProjectLinkGenerator.generateTaskPreviewFullLink(siteUrl, typeId, projectShortName)
                    else -> type
                }
            }
        }
        map.put("name", HyperlinkBuilderGenerator(summaryTitleExpr, summaryHrefExpr))

        val projectTitleExpr = PrimaryTypeFieldExpression<String>("projectName")
        val projectHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val projectId = reportParameters.getFieldValue<Int>("projectId")
                if (projectId != null) {
                    val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                    return ProjectLinkGenerator.generateProjectFullLink(siteUrl, projectId)
                }

                return ""
            }
        }

        map.put("projectName", HyperlinkBuilderGenerator(projectTitleExpr, projectHrefExpr))

        val logUserTitleExpr = PrimaryTypeFieldExpression<String>("assignUserFullName")
        val logUserHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val assignUser = reportParameters.getFieldValue<String>("assignUser")
                return when {
                    assignUser != null -> {
                        val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                        val projectId = reportParameters.getFieldValue<Int>("projectId")
                        ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, projectId!!, assignUser)
                    }
                    else -> ""
                }

            }
        }

        map.put("assignUser", HyperlinkBuilderGenerator(logUserTitleExpr, logUserHrefExpr))
        map.put("monitorDate", SimpleExpressionBuilderGenerator(DateExpression("monitorDate")))

        return map
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ProjectColumnBuilderMapper::class.java)
    }
}
