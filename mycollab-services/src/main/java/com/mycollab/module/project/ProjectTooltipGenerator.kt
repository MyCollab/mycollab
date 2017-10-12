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
package com.mycollab.module.project

import com.hp.gagawa.java.elements.*
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.core.utils.StringUtils
import com.mycollab.core.utils.StringUtils.trimHtmlTags
import com.mycollab.html.TooltipBuilder
import com.mycollab.html.TooltipBuilder.Companion.buildCellLink
import com.mycollab.html.TooltipBuilder.Companion.buildCellName
import com.mycollab.html.TooltipBuilder.Companion.buildCellValue
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.file.service.AbstractStorageService
import com.mycollab.module.page.domain.Page
import com.mycollab.module.project.domain.*
import com.mycollab.module.project.i18n.*
import com.mycollab.module.project.i18n.OptionI18nEnum.*
import com.mycollab.module.project.service.MilestoneService
import com.mycollab.module.project.service.ProjectTaskService
import com.mycollab.module.project.service.RiskService
import com.mycollab.module.tracker.domain.SimpleBug
import com.mycollab.module.tracker.domain.SimpleComponent
import com.mycollab.module.tracker.domain.Version
import com.mycollab.module.tracker.service.BugService
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.spring.AppContextUtil
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object ProjectTooltipGenerator {
    private val LOG = LoggerFactory.getLogger(ProjectTooltipGenerator::class.java)

    private fun generateToolTipNull(locale: Locale): String {
        val div = Div()
        val table = Table()
        table.style = "padding-left:10px;  color: #5a5a5a; font-size:11px;"

        val trRow1 = Tr()
        trRow1.appendChild(Td().setStyle("vertical-align: top; text-align: left;")
                .appendText(LocalizationHelper.getMessage(locale, GenericI18Enum.TOOLTIP_NO_ITEM_EXISTED)))
        table.appendChild(trRow1)
        div.appendChild(table)

        return div.write()
    }

    @JvmStatic
    fun generateTooltipEntity(locale: Locale, dateFormat: String, type: String, typeId: Int, sAccountId: Int, siteUrl: String, timeZone: TimeZone, showProject: Boolean): String? {
        return when (type) {
            ProjectTypeConstants.BUG -> {
                val bugService = AppContextUtil.getSpringBean(BugService::class.java)
                val bug = bugService.findById(typeId, sAccountId)
                generateToolTipBug(locale, dateFormat, bug, siteUrl, timeZone, showProject)
            }
            ProjectTypeConstants.TASK -> {
                val taskService = AppContextUtil.getSpringBean(ProjectTaskService::class.java)
                val task = taskService.findById(typeId, sAccountId)
                generateToolTipTask(locale, dateFormat, task, siteUrl, timeZone, showProject)
            }
            ProjectTypeConstants.MILESTONE -> {
                val milestoneService = AppContextUtil.getSpringBean(MilestoneService::class.java)
                val milestone = milestoneService.findById(typeId, sAccountId)
                generateToolTipMilestone(locale, dateFormat, milestone, siteUrl, timeZone, showProject)
            }
            ProjectTypeConstants.RISK -> {
                val riskService = AppContextUtil.getSpringBean(RiskService::class.java)
                val risk = riskService.findById(typeId, sAccountId)
                generateToolTipRisk(locale, dateFormat, risk, siteUrl, timeZone, showProject)
            }
            else -> ""
        }
    }

    @JvmStatic
    fun generateToolTipTask(locale: Locale, dateFormat: String, task: SimpleTask?, siteURL: String,
                            timeZone: TimeZone, showProject: Boolean): String? {
        if (task == null) {
            return generateToolTipNull(locale)
        }
        try {
            val tooltipManager = TooltipBuilder()
            tooltipManager.appendTitle(task.name)
            if (showProject) {
                tooltipManager.appendTitle(String.format("[%s] %s", task.projectShortname, task.projectName))
            }

            val trRow1 = Tr()
            val cell11 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_START_DATE))
            val startDate = DateTimeUtils.convertToStringWithUserTimeZone(task.startdate, dateFormat, locale, timeZone)
            val cell12 = buildCellValue(startDate)
            val cell13 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_END_DATE))
            val actualStartDate = DateTimeUtils.convertToStringWithUserTimeZone(task.enddate, dateFormat, locale, timeZone)
            val cell14 = buildCellValue(actualStartDate)
            trRow1.appendChild(cell11, cell12, cell13, cell14)
            tooltipManager.appendRow(trRow1)

            val trRow3 = Tr()
            val cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DUE_DATE))
            val deadline = DateTimeUtils.convertToStringWithUserTimeZone(task.duedate, dateFormat, locale,
                    timeZone)
            val cell32 = buildCellValue(deadline)
            val cell33 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_PRIORITY))
            val cell34 = buildCellValue(LocalizationHelper.getMessage(locale, Priority::class.java, task.priority))
            trRow3.appendChild(cell31, cell32, cell33, cell34)
            tooltipManager.appendRow(trRow3)

            val trRow4 = Tr()
            val cell41 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE))
            val assignUserLink = if (task.assignuser != null)
                AccountLinkGenerator.generatePreviewFullUserLink(siteURL, task.assignuser)
            else
                ""
            val assignUserAvatarLink = getAvatarPath(task.assignUserAvatarId, 16)
            val cell42 = buildCellLink(assignUserLink, assignUserAvatarLink, task.assignUserFullName)
            val cell43 = buildCellName(LocalizationHelper.getMessage(locale, MilestoneI18nEnum.SINGLE))
            val milestoneLink = if (task.milestoneName != null)
                ProjectLinkGenerator
                        .generateMilestonePreviewFullLink(siteURL, task.projectid!!, task.milestoneid!!)
            else
                ""
            val cell44 = buildCellLink(milestoneLink, task.milestoneName)
            trRow4.appendChild(cell41, cell42, cell43, cell44)
            tooltipManager.appendRow(trRow4)

            val trRow5 = Tr()
            val cell51 = buildCellName(LocalizationHelper.getMessage(locale, TaskI18nEnum.FORM_PERCENTAGE_COMPLETE))
            val cell52 = buildCellValue(task.percentagecomplete)
            trRow5.appendChild(cell51, cell52)
            tooltipManager.appendRow(trRow5)

            val trRow6 = Tr()
            val cell61 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION))
            val cell62 = buildCellValue(trimHtmlTags(task.description))
            cell62.setAttribute("colspan", "3")
            trRow6.appendChild(cell61, cell62)
            tooltipManager.appendRow(trRow6)

            return tooltipManager.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate tooltip for servlet project-task tooltip", e)
            return null
        }

    }

    @JvmStatic
    fun generateToolTipBug(locale: Locale, dateFormat: String, bug: SimpleBug?, siteURL: String, timeZone: TimeZone, showProject: Boolean): String? {
        if (bug == null) {
            return generateToolTipNull(locale)
        }

        try {
            val tooltipManager = TooltipBuilder()
            tooltipManager.appendTitle(bug.name)

            if (showProject) {
                tooltipManager.appendTitle(String.format("[%s] %s", bug.projectShortName, bug.projectname))
            }

            val trRow1 = Tr()
            val cell11 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION))
            val cell12 = buildCellValue(trimHtmlTags(bug.description))
            cell12.setAttribute("colspan", "3")
            trRow1.appendChild(cell11, cell12)
            tooltipManager.appendRow(trRow1)

            val trRow2 = Tr()
            val cell21 = buildCellName(LocalizationHelper.getMessage(locale, BugI18nEnum.FORM_ENVIRONMENT))
            val cell22 = buildCellValue(trimHtmlTags(bug.environment))
            cell22.setAttribute("colspan", "3")
            trRow2.appendChild(cell21, cell22)
            tooltipManager.appendRow(trRow2)

            val trRow3 = Tr()
            val cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS))
            val cell32 = buildCellValue(LocalizationHelper.getMessage(locale, BugStatus::class.java, bug.status))
            val cell33 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_PRIORITY))
            val cell34 = buildCellValue(LocalizationHelper.getMessage(locale, Priority::class.java, bug.priority))
            trRow3.appendChild(cell31, cell32, cell33, cell34)
            tooltipManager.appendRow(trRow3)

            val trRow4 = Tr()
            val cell41 = buildCellName(LocalizationHelper.getMessage(locale, BugI18nEnum.FORM_SEVERITY))
            val cell42 = buildCellValue(LocalizationHelper.getMessage(locale, BugSeverity::class.java, bug.severity))
            val cell43 = buildCellName(LocalizationHelper.getMessage(locale, BugI18nEnum.FORM_RESOLUTION))
            val cell44 = buildCellValue(LocalizationHelper.getMessage(locale, BugResolution::class.java, bug.resolution))
            trRow4.appendChild(cell41, cell42, cell43, cell44)
            tooltipManager.appendRow(trRow4)

            val trRow5 = Tr()
            val cell51 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DUE_DATE))
            val dueDate = DateTimeUtils.convertToStringWithUserTimeZone(bug.duedate, dateFormat, locale, timeZone)
            val cell52 = buildCellValue(dueDate)
            val cell53 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_CREATED_TIME))
            val createdTime = DateTimeUtils.convertToStringWithUserTimeZone(bug.createdtime, dateFormat, locale, timeZone)
            val cell54 = buildCellValue(createdTime)
            trRow5.appendChild(cell51, cell52, cell53, cell54)
            tooltipManager.appendRow(trRow5)

            // Assignee

            val trRow6 = Tr()
            val cell61 = buildCellName(LocalizationHelper.getMessage(locale, BugI18nEnum.FORM_LOG_BY))
            val logbyUserLink = if (bug.createduser != null) AccountLinkGenerator.generatePreviewFullUserLink(siteURL, bug.createduser) else ""
            val logbyAvatarLink = getAvatarPath(bug.loguserAvatarId, 16)
            val cell62 = buildCellLink(logbyUserLink, logbyAvatarLink, bug.loguserFullName)
            val cell63 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE))
            val assignUserLink = if (bug.assignuser != null) AccountLinkGenerator.generatePreviewFullUserLink(siteURL, bug.assignuser) else ""
            val assignUserAvatarLink = getAvatarPath(bug.assignUserAvatarId, 16)
            val cell64 = buildCellLink(assignUserLink, assignUserAvatarLink, bug.assignuserFullName)
            trRow6.appendChild(cell61, cell62, cell63, cell64)
            tooltipManager.appendRow(trRow6)

            val trRow7 = Tr()
            val cell71 = buildCellName(LocalizationHelper.getMessage(locale, MilestoneI18nEnum.SINGLE))
            val phaseLink = if (bug.milestoneid != null) ProjectLinkGenerator.generateMilestonePreviewFullLink(siteURL, bug.projectid!!, bug.milestoneid!!) else ""
            val cell72 = buildCellLink(phaseLink, bug.milestoneName)
            cell72.setAttribute("colspan", "3")
            trRow7.appendChild(cell71, cell72)
            tooltipManager.appendRow(trRow7)

            return tooltipManager.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate tooltip for servlet project-bug tooltip", e)
            return null
        }

    }

    @JvmStatic
    fun generateToolTipRisk(locale: Locale, dateFormat: String, risk: SimpleRisk?, siteURL: String,
                            timeZone: TimeZone, showProject: Boolean): String? {
        if (risk == null)
            return generateToolTipNull(locale)
        try {
            val tooltipManager = TooltipBuilder()
            tooltipManager.appendTitle(risk.name)

            if (showProject) {
                tooltipManager.appendTitle(String.format("[%s] %s", risk.projectShortName, risk.projectName))
            }

            val trRow5 = Tr()
            val cell51 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION))
            val cell52 = buildCellValue(trimHtmlTags(risk.description))
            cell52.setAttribute("colspan", "3")
            trRow5.appendChild(cell51, cell52)
            tooltipManager.appendRow(trRow5)

            val trRow1 = Tr()
            val cell11 = buildCellName(LocalizationHelper.getMessage(locale, RiskI18nEnum.FORM_RAISED_BY))
            val raisedUserLink = if (risk.createduser != null)
                AccountLinkGenerator
                        .generatePreviewFullUserLink(siteURL, risk.createduser)
            else
                ""
            val raisedUserAvatarLink = getAvatarPath(risk.raisedByUserAvatarId, 16)
            val cell12 = buildCellLink(raisedUserLink, raisedUserAvatarLink, risk.raisedByUserFullName)
            val cell13 = buildCellName(LocalizationHelper.getMessage(locale, RiskI18nEnum.FORM_CONSEQUENCE))
            val cell14 = buildCellValue(risk.consequence)
            trRow1.appendChild(cell11, cell12, cell13, cell14)
            tooltipManager.appendRow(trRow1)

            val trRow2 = Tr()
            val cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE))
            val assignUserLink = if (risk.assignuser != null)
                AccountLinkGenerator.generatePreviewFullUserLink(siteURL,
                        risk.assignuser)
            else
                ""
            val assignUserAvatarLink = getAvatarPath(risk.assignToUserAvatarId, 16)
            val cell22 = buildCellLink(assignUserLink, assignUserAvatarLink,
                    risk.assignedToUserFullName)
            val cell23 = buildCellName(LocalizationHelper.getMessage(locale, RiskI18nEnum.FORM_PROBABILITY))
            val cell24 = buildCellValue(risk.probalitity)
            trRow2.appendChild(cell21, cell22, cell23, cell24)
            tooltipManager.appendRow(trRow2)

            val trRow3 = Tr()
            val cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DUE_DATE))
            val dueDate = DateTimeUtils.convertToStringWithUserTimeZone(risk.duedate, dateFormat, locale, timeZone)
            val cell32 = buildCellValue(dueDate)
            val cell33 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS))
            val cell34 = buildCellValue(LocalizationHelper.getMessage(locale, StatusI18nEnum::class.java, risk.status))
            trRow3.appendChild(cell31, cell32, cell33, cell34)
            tooltipManager.appendRow(trRow3)

            val trRow4 = Tr()
            val cell41 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_START_DATE))
            val startDate = DateTimeUtils.convertToStringWithUserTimeZone(risk.startdate, dateFormat, locale, timeZone)
            val cell42 = buildCellValue(startDate)
            val cell43 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_END_DATE))
            val endDate = DateTimeUtils.convertToStringWithUserTimeZone(risk.enddate, dateFormat, locale, timeZone)
            val cell44 = buildCellValue(endDate)
            trRow4.appendChild(cell41, cell42, cell43, cell44)
            tooltipManager.appendRow(trRow4)

            val trRow6 = Tr()
            val cell61 = buildCellName(LocalizationHelper.getMessage(locale, RiskI18nEnum.FORM_RESPONSE))
            val cell62 = buildCellValue(trimHtmlTags(risk.response))
            cell62.setAttribute("colspan", "3")
            trRow6.appendChild(cell61, cell62)
            tooltipManager.appendRow(trRow6)

            return tooltipManager.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate tooltip for Risk in TooptipGeneratorServlet", e)
            return null
        }

    }

    @JvmStatic
    fun generateToolTipVersion(locale: Locale, dateFormat: String, version: Version?, siteURL: String,
                               timeZone: TimeZone): String? {
        if (version == null)
            return generateToolTipNull(locale)
        try {
            val tooltipManager = TooltipBuilder()
            tooltipManager.appendTitle(version.name)

            val trRow2 = Tr()
            val cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION))
            val cell22 = buildCellValue(trimHtmlTags(version.description))
            cell22.setAttribute("colspan", "3")
            trRow2.appendChild(cell21, cell22)
            tooltipManager.appendRow(trRow2)

            val trRow3 = Tr()
            val cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DUE_DATE))
            val duedate = DateTimeUtils.convertToStringWithUserTimeZone(version.duedate, dateFormat, locale, timeZone)
            val cell32 = buildCellValue(duedate)
            val cell33 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS))
            val cell34 = buildCellValue(LocalizationHelper.getMessage(locale, StatusI18nEnum::class.java, version.status))
            trRow3.appendChild(cell31, cell32, cell33, cell34)
            tooltipManager.appendRow(trRow3)

            return tooltipManager.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate tooltip for Version", e)
            return null
        }

    }

    @JvmStatic
    fun generateToolTipComponent(locale: Locale, component: SimpleComponent?, siteURL: String, timeZone: TimeZone): String? {
        if (component == null)
            return generateToolTipNull(locale)

        try {
            val tooltipManager = TooltipBuilder()
            tooltipManager.appendTitle(component.name)

            val trRow2 = Tr()
            val cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION))
            val cell22 = buildCellValue(trimHtmlTags(component.description))
            cell22.setAttribute("colspan", "3")
            trRow2.appendChild(cell21, cell22)
            tooltipManager.appendRow(trRow2)

            val trRow3 = Tr()
            val cell31 = buildCellName(LocalizationHelper.getMessage(locale, ComponentI18nEnum.FORM_LEAD))
            val leadLink = if (component.userlead != null)
                AccountLinkGenerator.generatePreviewFullUserLink(siteURL,
                        component.userlead)
            else
                ""
            val leadAvatarLink = getAvatarPath(component.userLeadAvatarId, 16)
            val cell32 = buildCellLink(leadLink, leadAvatarLink, component.userLeadFullName)
            trRow3.appendChild(cell31, cell32)
            tooltipManager.appendRow(trRow3)

            return tooltipManager.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate tooltip for Component", e)
            return null
        }

    }

    @JvmStatic
    fun generateToolTipProject(locale: Locale, dateFormat: String, project: SimpleProject?, siteURL: String, timeZone: TimeZone): String? {
        if (project == null)
            return generateToolTipNull(locale)

        try {
            val tooltipManager = TooltipBuilder()
            tooltipManager.appendTitle(project.name)

            val trRow1 = Tr()
            val cell11 = buildCellName(LocalizationHelper.getMessage(locale, ProjectI18nEnum.FORM_HOME_PAGE))
            val homepageLink = if (project.homepage != null) project.homepage else ""
            val cell12 = buildCellLink(homepageLink, project.homepage)
            val cell13 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS))
            val cell14 = buildCellValue(LocalizationHelper.getMessage(locale, StatusI18nEnum::class.java, project.projectstatus))
            trRow1.appendChild(cell11, cell12, cell13, cell14)

            val trRow2 = Tr()
            val cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_START_DATE))
            val planStartDate = DateTimeUtils.convertToStringWithUserTimeZone(project.planstartdate, dateFormat, locale, timeZone)
            val cell22 = buildCellValue(planStartDate)
            val cell23 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_CURRENCY))
            val currency = if (project.currencyid != null) project.currencyid else ""
            val cell24 = buildCellValue(currency)
            trRow2.appendChild(cell21, cell22, cell23, cell24)
            tooltipManager.appendRow(trRow2)

            val trRow3 = Tr()
            val cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_END_DATE))
            val planEndDate = DateTimeUtils.convertToStringWithUserTimeZone(project.planenddate, dateFormat, locale, timeZone)
            val cell32 = buildCellValue(planEndDate)
            val cell33 = buildCellName(LocalizationHelper.getMessage(locale, ProjectI18nEnum.FORM_BILLING_RATE))
            val cell34 = buildCellValue(project.defaultbillingrate)
            trRow3.appendChild(cell31, cell32, cell33, cell34)
            tooltipManager.appendRow(trRow3)

            val trRow4 = Tr()
            val cell41 = buildCellName(LocalizationHelper.getMessage(locale, ProjectI18nEnum.FORM_TARGET_BUDGET))
            val cell42 = buildCellValue(project.targetbudget)
            val cell43 = buildCellName(LocalizationHelper.getMessage(locale, ProjectI18nEnum.FORM_ACTUAL_BUDGET))
            val cell44 = buildCellValue(project.actualbudget)
            trRow4.appendChild(cell41, cell42, cell43, cell44)
            tooltipManager.appendRow(trRow4)

            val trRow6 = Tr()
            val cell61 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION))
            val cell62 = buildCellValue(trimHtmlTags(project.description))
            cell62.setAttribute("colspan", "3")
            trRow6.appendChild(cell61, cell62)
            tooltipManager.appendRow(trRow6)

            return tooltipManager.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate tooltip for servlet project tooltip", e)
            return null
        }

    }

    @JvmStatic
    fun generateToolTipMilestone(locale: Locale, dateFormat: String, milestone: SimpleMilestone?, siteURL: String,
                                 timeZone: TimeZone, showProject: Boolean): String? {
        if (milestone == null)
            return generateToolTipNull(locale)

        try {
            val tooltipManager = TooltipBuilder()
            tooltipManager.appendTitle(milestone.name)

            if (showProject) {
                tooltipManager.appendTitle(String.format("[%s] %s", milestone.projectShortName, milestone.projectName))
            }

            val trRow2 = Tr()
            val cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_START_DATE))
            val startDate = DateTimeUtils.convertToStringWithUserTimeZone(milestone.startdate, dateFormat, locale, timeZone)
            val cell22 = buildCellValue(startDate)
            val cell23 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE))
            val assignUserLink = if (milestone.assignuser != null)
                AccountLinkGenerator.generatePreviewFullUserLink(siteURL, milestone.assignuser)
            else
                ""
            val assignUserAvatarLink = getAvatarPath(milestone.ownerAvatarId, 16)
            val cell24 = buildCellLink(assignUserLink, assignUserAvatarLink,
                    milestone.ownerFullName)
            trRow2.appendChild(cell21, cell22, cell23, cell24)
            tooltipManager.appendRow(trRow2)

            val trRow3 = Tr()
            val cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_END_DATE))
            val endDate = DateTimeUtils.convertToStringWithUserTimeZone(milestone.enddate, dateFormat, locale, timeZone)
            val cell32 = buildCellValue(endDate)
            val cell33 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS))
            val cell34 = buildCellValue(LocalizationHelper.getMessage(locale, MilestoneStatus::class.java, milestone.status))
            trRow3.appendChild(cell31, cell32, cell33, cell34)
            tooltipManager.appendRow(trRow3)

            val trRow6 = Tr()
            val cell61 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION))
            val cell62 = buildCellValue(trimHtmlTags(milestone.description))
            cell62.setAttribute("colspan", "3")
            trRow6.appendChild(cell61, cell62)
            tooltipManager.appendRow(trRow6)

            return tooltipManager.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate tooltip for servlet project tooltip", e)
            return null
        }

    }

    @JvmStatic
    fun generateToolTipStandUp(locale: Locale, dateFormat: String, standup: SimpleStandupReport?, siteURL: String, timeZone: TimeZone): String? {
        if (standup == null)
            return generateToolTipNull(locale)

        try {
            val div = Div().setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;")
            val name = H3()
            name.appendText(Jsoup.parse(DateTimeUtils.convertToStringWithUserTimeZone(
                    standup.createdtime, dateFormat, locale, timeZone)).html())
            div.appendChild(name)

            val table = Table()
            table.style = "padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;"

            val trRow3 = Tr()
            trRow3.appendChild(Td().setStyle("width: 165px; vertical-align: top; text-align: right;")
                    .appendText(LocalizationHelper.getMessage(locale, StandupI18nEnum.STANDUP_LASTDAY)))
                    .appendChild(Td().setStyle("word-wrap: break-word; white-space: normal;vertical-align: top;")
                            .appendText(standup.whatlastday))

            val trRow4 = Tr()
            trRow4.appendChild(Td().setStyle("width: 165px;vertical-align: top; text-align: right;")
                    .appendText(LocalizationHelper.getMessage(locale, StandupI18nEnum.STANDUP_TODAY)))
                    .appendChild(Td().setStyle("break-word; white-space: normal;vertical-align: top;")
                            .appendText(standup.whattoday))
            val trRow5 = Tr()
            trRow5.appendChild(Td().setStyle("width: 165px;vertical-align: top; text-align: right;")
                    .appendText(LocalizationHelper.getMessage(locale, StandupI18nEnum.STANDUP_ISSUE)))
                    .appendChild(Td().setStyle("break-word; white-space: normal;vertical-align: top;")
                            .appendText(standup.whatproblem))

            table.appendChild(trRow3)
            table.appendChild(trRow4)
            table.appendChild(trRow5)
            div.appendChild(table)

            return div.write()
        } catch (e: Exception) {
            LOG.error("Error while generate tooltip for servlet project tooltip", e)
            return null
        }

    }

    @JvmStatic
    fun generateToolTipPage(locale: Locale, page: Page?, siteURL: String, timeZone: TimeZone): String? {
        if (page == null)
            return generateToolTipNull(locale)

        try {
            val tooltipManager = TooltipBuilder()
            tooltipManager.appendTitle(page.subject)

            val trRow2 = Tr()
            val cell21 = Td().setStyle("vertical-align: top; text-align: left;word-wrap: break-word; white-space: normal;vertical-align: top;")
                    .appendText(StringUtils.trim(page.content, 500, true))

            trRow2.appendChild(cell21)
            tooltipManager.appendRow(trRow2)

            return tooltipManager.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate tooltip for servlet project tooltip", e)
            return null
        }

    }

    @JvmStatic
    fun generateToolTipMessage(locale: Locale, message: SimpleMessage?, siteURL: String, timeZone: TimeZone): String? {
        if (message == null)
            return generateToolTipNull(locale)

        try {
            val tooltipManager = TooltipBuilder()
            tooltipManager.appendTitle(message.title)

            val trRow2 = Tr()
            val cell21 = Td().setStyle("vertical-align: top; text-align: left;word-wrap: break-word; white-space: normal;vertical-align: top;")
                    .appendText(StringUtils.trim(message.message, 500, true))

            trRow2.appendChild(cell21)
            tooltipManager.appendRow(trRow2)

            return tooltipManager.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate tooltip for servlet project tooltip", e)
            return null
        }

    }

    private fun getAvatarPath(userAvatarId: String?, size: Int): String {
        val abstractStorageService = AppContextUtil.getSpringBean(AbstractStorageService::class.java)
        return abstractStorageService.getAvatarPath(userAvatarId, size)
    }
}