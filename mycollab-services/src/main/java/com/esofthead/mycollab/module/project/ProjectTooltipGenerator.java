/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project;

import com.esofthead.mycollab.common.TooltipBuilder;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.configuration.LocaleHelper;
import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.i18n.LocalizationHelper;
import com.esofthead.mycollab.module.page.domain.Page;
import com.esofthead.mycollab.module.project.domain.*;
import com.esofthead.mycollab.module.project.i18n.*;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.*;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.service.RiskService;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.SimpleComponent;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.hp.gagawa.java.elements.*;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.TimeZone;

import static com.esofthead.mycollab.common.TooltipBuilder.TdUtil.*;
import static com.esofthead.mycollab.core.utils.StringUtils.trimHtmlTags;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectTooltipGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectTooltipGenerator.class);

    private static String generateTolltipNull(Locale locale) {
        Div div = new Div();
        Table table = new Table();
        table.setStyle("padding-left:10px;  color: #5a5a5a; font-size:11px;");

        Tr trRow1 = new Tr();
        trRow1.appendChild(new Td().setStyle("vertical-align: top; text-align: left;")
                .appendText(LocalizationHelper.getMessage(locale, GenericI18Enum.TOOLTIP_NO_ITEM_EXISTED)));
        table.appendChild(trRow1);
        div.appendChild(table);

        return div.write();
    }

    public static String generateTooltipEntity(Locale locale, String type, Integer typeId, Integer sAccountId,
                                               String siteUrl, TimeZone timeZone, boolean showProject) {
        if (ProjectTypeConstants.BUG.equals(type)) {
            BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
            SimpleBug bug = bugService.findById(typeId, sAccountId);
            return generateToolTipBug(locale, bug, siteUrl, timeZone, showProject);
        } else if (ProjectTypeConstants.TASK.equals(type)) {
            ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
            SimpleTask task = taskService.findById(typeId, sAccountId);
            return generateToolTipTask(locale, task, siteUrl, timeZone, showProject);
        } else if (ProjectTypeConstants.MILESTONE.equals(type)) {
            MilestoneService milestoneService = ApplicationContextUtil.getSpringBean(MilestoneService.class);
            SimpleMilestone milestone = milestoneService.findById(typeId, sAccountId);
            return generateToolTipMilestone(locale, milestone, siteUrl, timeZone, showProject);
        } else if (ProjectTypeConstants.RISK.equals(type)) {
            RiskService riskService = ApplicationContextUtil.getSpringBean(RiskService.class);
            SimpleRisk risk = riskService.findById(typeId, sAccountId);
            return generateToolTipRisk(locale, risk, siteUrl, timeZone, showProject);
        } else {
            return "";
        }
    }

    public static String generateToolTipTask(Locale locale, SimpleTask task, String siteURL, TimeZone timeZone,
                                             boolean showProject) {
        if (task == null) {
            return generateTolltipNull(locale);
        }
        try {
            TooltipBuilder tooltipManager = new TooltipBuilder();
            tooltipManager.appendTitle(task.getTaskname());
            if (showProject) {
                tooltipManager.appendTitle(String.format("[%s] %s", task.getProjectShortname(), task.getProjectName()));
            }

            String dateFormat = LocaleHelper.getDateFormatInstance(locale).getDateFormat();

            Tr trRow1 = new Tr();
            Td cell11 = TooltipBuilder.TdUtil.buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_START_DATE));
            String startdate = DateTimeUtils.convertToStringWithUserTimeZone(task.getStartdate(), dateFormat, timeZone);
            Td cell12 = buildCellValue(startdate);
            Td cell13 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_END_DATE));
            String actualStartDate = DateTimeUtils.convertToStringWithUserTimeZone(task.getEnddate(), dateFormat, timeZone);
            Td cell14 = buildCellValue(actualStartDate);
            trRow1.appendChild(cell11, cell12, cell13, cell14);
            tooltipManager.appendRow(trRow1);

            Tr trRow3 = new Tr();
            Td cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DUE_DATE));
            String deadline = DateTimeUtils.convertToStringWithUserTimeZone(task.getDeadline(), dateFormat, timeZone);
            Td cell32 = buildCellValue(deadline);
            Td cell33 = buildCellName(LocalizationHelper.getMessage(locale, TaskI18nEnum.FORM_PRIORITY));
            Td cell34 = buildCellValue(LocalizationHelper.getMessage(locale, TaskPriority.class, task.getPriority()));
            trRow3.appendChild(cell31, cell32, cell33, cell34);
            tooltipManager.appendRow(trRow3);

            Tr trRow4 = new Tr();
            Td cell41 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE));
            String assignUserLink = (task.getAssignuser() != null) ? AccountLinkGenerator
                    .generatePreviewFullUserLink(siteURL, task.getAssignuser())
                    : "";
            String assignUserAvatarLink = StorageFactory.getInstance().getAvatarPath(task.getAssignUserAvatarId(), 16);
            Td cell42 = buildCellLink(assignUserLink, assignUserAvatarLink, task.getAssignUserFullName());
            Td cell43 = buildCellName(LocalizationHelper.getMessage(locale, TaskI18nEnum.FORM_PHASE));
            String taskgroupLink = (task.getMilestoneName() != null) ? ProjectLinkGenerator
                    .generateMilestonePreviewFullLink(siteURL, task.getProjectid(), task.getMilestoneid()) : "";
            Td cell44 = buildCellLink(taskgroupLink, task.getMilestoneName());
            trRow4.appendChild(cell41, cell42, cell43, cell44);
            tooltipManager.appendRow(trRow4);

            Tr trRow5 = new Tr();
            Td cell51 = buildCellName(LocalizationHelper.getMessage(locale, TaskI18nEnum.FORM_PERCENTAGE_COMPLETE));
            Td cell52 = buildCellValue(task.getPercentagecomplete());
            trRow5.appendChild(cell51, cell52);
            tooltipManager.appendRow(trRow5);

            Tr trRow6 = new Tr();
            Td cell61 = buildCellName(LocalizationHelper.getMessage(locale, TaskI18nEnum.FORM_NOTES));
            Td cell62 = buildCellValue(trimHtmlTags(task.getNotes()));
            cell62.setAttribute("colspan", "3");
            trRow6.appendChild(cell61, cell62);
            tooltipManager.appendRow(trRow6);

            return tooltipManager.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate tooltip for servlet project-task tooltip", e);
            return null;
        }
    }

    public static String generateToolTipBug(Locale locale, SimpleBug bug, String siteURL, TimeZone timeZone, boolean showProject) {
        if (bug == null) {
            return generateTolltipNull(locale);
        }

        try {
            TooltipBuilder tooltipManager = new TooltipBuilder();
            tooltipManager.appendTitle(bug.getSummary());

            if (showProject) {
                tooltipManager.appendTitle(String.format("[%s] %s", bug.getProjectShortName(), bug.getProjectname()));
            }

            String dateFormat = LocaleHelper.getDateFormatInstance(locale).getDateFormat();

            Tr trRow1 = new Tr();
            Td cell11 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION));
            Td cell12 = buildCellValue(trimHtmlTags(bug.getDescription()));
            cell12.setAttribute("colspan", "3");
            trRow1.appendChild(cell11, cell12);
            tooltipManager.appendRow(trRow1);

            Tr trRow2 = new Tr();
            Td cell21 = buildCellName(LocalizationHelper.getMessage(locale, BugI18nEnum.FORM_ENVIRONMENT));
            Td cell22 = buildCellValue(trimHtmlTags(bug.getEnvironment()));
            cell22.setAttribute("colspan", "3");
            trRow2.appendChild(cell21, cell22);
            tooltipManager.appendRow(trRow2);

            Tr trRow3 = new Tr();
            Td cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS));
            Td cell32 = buildCellValue(LocalizationHelper.getMessage(locale, BugStatus.class, bug.getStatus()));
            Td cell33 = buildCellName(LocalizationHelper.getMessage(locale, BugI18nEnum.FORM_PRIORITY));
            Td cell34 = buildCellValue(LocalizationHelper.getMessage(locale, BugPriority.class, bug.getPriority()));
            trRow3.appendChild(cell31, cell32, cell33, cell34);
            tooltipManager.appendRow(trRow3);

            Tr trRow4 = new Tr();
            Td cell41 = buildCellName(LocalizationHelper.getMessage(locale, BugI18nEnum.FORM_SEVERITY));
            Td cell42 = buildCellValue(LocalizationHelper.getMessage(locale, BugSeverity.class, bug.getSeverity()));
            Td cell43 = buildCellName(LocalizationHelper.getMessage(locale, BugI18nEnum.FORM_RESOLUTION));
            Td cell44 = buildCellValue(LocalizationHelper.getMessage(locale, BugResolution.class, bug.getResolution()));
            trRow4.appendChild(cell41, cell42, cell43, cell44);
            tooltipManager.appendRow(trRow4);

            Tr trRow5 = new Tr();
            Td cell51 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DUE_DATE));
            String dueDate = DateTimeUtils.convertToStringWithUserTimeZone(bug.getDuedate(), dateFormat, timeZone);
            Td cell52 = buildCellValue(dueDate);
            Td cell53 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_CREATED_TIME));
            String createdTime = DateTimeUtils.convertToStringWithUserTimeZone(bug.getCreatedtime(), dateFormat, timeZone);
            Td cell54 = buildCellValue(createdTime);
            trRow5.appendChild(cell51, cell52, cell53, cell54);
            tooltipManager.appendRow(trRow5);

            // Assignee

            Tr trRow6 = new Tr();
            Td cell61 = buildCellName(LocalizationHelper.getMessage(locale, BugI18nEnum.FORM_LOG_BY));
            String logbyUserLink = (bug.getLogby() != null) ? AccountLinkGenerator.generatePreviewFullUserLink(siteURL, bug.getLogby()) : "";
            String logbyAvatarLink = StorageFactory.getInstance().getAvatarPath(bug.getLoguserAvatarId(), 16);
            Td cell62 = buildCellLink(logbyUserLink, logbyAvatarLink, bug.getLoguserFullName());
            Td cell63 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE));
            String assignUserLink = (bug.getAssignuser() != null) ? AccountLinkGenerator.generatePreviewFullUserLink(siteURL, bug.getAssignuser()) : "";
            String assignUserAvatarLink = StorageFactory.getInstance().getAvatarPath(bug.getAssignUserAvatarId(), 16);
            Td cell64 = buildCellLink(assignUserLink, assignUserAvatarLink, bug.getAssignuserFullName());
            trRow6.appendChild(cell61, cell62, cell63, cell64);
            tooltipManager.appendRow(trRow6);

            Tr trRow7 = new Tr();
            Td cell71 = buildCellName(LocalizationHelper.getMessage(locale, BugI18nEnum.FORM_PHASE));
            String phaseLink = (bug.getMilestoneid() != null) ? ProjectLinkGenerator.generateMilestonePreviewFullLink(siteURL, bug.getProjectid(), bug.getMilestoneid()) : "";
            Td cell72 = buildCellLink(phaseLink, bug.getMilestoneName());
            cell72.setAttribute("colspan", "3");
            trRow7.appendChild(cell71, cell72);
            tooltipManager.appendRow(trRow7);

            return tooltipManager.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate tooltip for servlet project-bug tooltip", e);
            return null;
        }
    }

    public static String generateToolTipRisk(Locale locale, SimpleRisk risk, String siteURL, TimeZone timeZone,
                                             boolean showProject) {
        if (risk == null)
            return generateTolltipNull(locale);
        try {
            TooltipBuilder tooltipManager = new TooltipBuilder();
            tooltipManager.appendTitle(risk.getRiskname());

            if (showProject) {
                tooltipManager.appendTitle(String.format("[%s] %s", risk.getProjectShortName(), risk.getProjectName()));
            }

            String dateFormat = LocaleHelper.getDateFormatInstance(locale).getDateFormat();

            Tr trRow5 = new Tr();
            Td cell51 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION));
            Td cell52 = buildCellValue(trimHtmlTags(risk.getDescription()));
            cell52.setAttribute("colspan", "3");
            trRow5.appendChild(cell51, cell52);
            tooltipManager.appendRow(trRow5);

            Tr trRow1 = new Tr();
            Td cell11 = buildCellName(LocalizationHelper.getMessage(locale, RiskI18nEnum.FORM_RAISED_BY));
            String raisedUserLink = (risk.getRaisedbyuser() != null) ? AccountLinkGenerator.generatePreviewFullUserLink(siteURL, risk.getRaisedbyuser()) : "";
            String raisedUserAvatarLink = StorageFactory.getInstance().getAvatarPath(risk.getRaisedByUserAvatarId(), 16);
            Td cell12 = buildCellLink(raisedUserLink, raisedUserAvatarLink, risk.getRaisedByUserFullName());
            Td cell13 = buildCellName(LocalizationHelper.getMessage(locale, RiskI18nEnum.FORM_CONSEQUENCE));
            Td cell14 = buildCellValue(risk.getConsequence());
            trRow1.appendChild(cell11, cell12, cell13, cell14);
            tooltipManager.appendRow(trRow1);

            Tr trRow2 = new Tr();
            Td cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE));
            String assignUserLink = (risk.getAssigntouser() != null) ? AccountLinkGenerator.generatePreviewFullUserLink(siteURL,
                    risk.getAssigntouser()) : "";
            String assignUserAvatarLink = StorageFactory.getInstance().getAvatarPath(risk.getAssignToUserAvatarId(), 16);
            Td cell22 = buildCellLink(assignUserLink, assignUserAvatarLink,
                    risk.getAssignedToUserFullName());
            Td cell23 = buildCellName(LocalizationHelper.getMessage(locale, RiskI18nEnum.FORM_PROBABILITY));
            Td cell24 = buildCellValue(risk.getProbalitity());
            trRow2.appendChild(cell21, cell22, cell23, cell24);
            tooltipManager.appendRow(trRow2);

            Tr trRow3 = new Tr();
            Td cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DUE_DATE));
            String datedue = DateTimeUtils.convertToStringWithUserTimeZone(risk.getDatedue(), dateFormat, timeZone);
            Td cell32 = buildCellValue(datedue);
            Td cell33 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS));
            Td cell34 = buildCellValue(LocalizationHelper.getMessage(locale, StatusI18nEnum.class, risk.getStatus()));
            trRow3.appendChild(cell31, cell32, cell33, cell34);
            tooltipManager.appendRow(trRow3);

            Tr trRow4 = new Tr();
            Td cell41 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_START_DATE));
            String startdate = DateTimeUtils.convertToStringWithUserTimeZone(risk.getStartdate(), dateFormat, timeZone);
            Td cell42 = buildCellValue(startdate);
            Td cell43 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_END_DATE));
            String enddate = DateTimeUtils.convertToStringWithUserTimeZone(risk.getEnddate(), dateFormat, timeZone);
            Td cell44 = buildCellValue(enddate);
            trRow4.appendChild(cell41, cell42, cell43, cell44);
            tooltipManager.appendRow(trRow4);

            Tr trRow6 = new Tr();
            Td cell61 = buildCellName(LocalizationHelper.getMessage(locale, RiskI18nEnum.FORM_RESPONSE));
            Td cell62 = buildCellValue(trimHtmlTags(risk.getResponse()));
            cell62.setAttribute("colspan", "3");
            trRow6.appendChild(cell61, cell62);
            tooltipManager.appendRow(trRow6);

            return tooltipManager.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate tooltip for Risk in TooptipGeneratorServlet", e);
            return null;
        }
    }

    public static String generateToolTipVersion(Locale locale, Version version, String siteURL, TimeZone timeZone) {
        if (version == null)
            return generateTolltipNull(locale);
        try {
            TooltipBuilder tooltipManager = new TooltipBuilder();
            tooltipManager.appendTitle(version.getVersionname());

            String dateFormat = LocaleHelper.getDateFormatInstance(locale).getDateFormat();

            Tr trRow2 = new Tr();
            Td cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION));
            Td cell22 = buildCellValue(trimHtmlTags(version.getDescription()));
            cell22.setAttribute("colspan", "3");
            trRow2.appendChild(cell21, cell22);
            tooltipManager.appendRow(trRow2);

            Tr trRow3 = new Tr();
            Td cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DUE_DATE));
            String duedate = DateTimeUtils.convertToStringWithUserTimeZone(version.getDuedate(), dateFormat, timeZone);
            Td cell32 = buildCellValue(duedate);
            Td cell33 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS));
            Td cell34 = buildCellValue(LocalizationHelper.getMessage(locale, StatusI18nEnum.class, version.getStatus()));
            trRow3.appendChild(cell31, cell32, cell33, cell34);
            tooltipManager.appendRow(trRow3);

            return tooltipManager.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate tooltip for Version", e);
            return null;
        }
    }

    public static String generateToolTipComponent(Locale locale, SimpleComponent component, String siteURL, TimeZone timeZone) {
        if (component == null)
            return generateTolltipNull(locale);

        try {
            TooltipBuilder tooltipManager = new TooltipBuilder();
            tooltipManager.appendTitle(component.getComponentname());

            Tr trRow2 = new Tr();
            Td cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION));
            Td cell22 = buildCellValue(trimHtmlTags(component.getDescription()));
            cell22.setAttribute("colspan", "3");
            trRow2.appendChild(cell21, cell22);
            tooltipManager.appendRow(trRow2);

            Tr trRow3 = new Tr();
            Td cell31 = buildCellName(LocalizationHelper.getMessage(locale, ComponentI18nEnum.FORM_LEAD));
            String leadLink = (component.getUserlead() != null) ? AccountLinkGenerator.generatePreviewFullUserLink(siteURL,
                    component.getUserlead()) : "";
            String leadAvatarLink = StorageFactory.getInstance().getAvatarPath(component.getUserLeadAvatarId(), 16);
            Td cell32 = buildCellLink(leadLink, leadAvatarLink, component.getUserLeadFullName());
            trRow3.appendChild(cell31, cell32);
            tooltipManager.appendRow(trRow3);

            return tooltipManager.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate tooltip for Component", e);
            return null;
        }
    }

    public static String generateToolTipProject(Locale locale, SimpleProject project, String siteURL, TimeZone timeZone) {
        if (project == null)
            return generateTolltipNull(locale);

        try {
            TooltipBuilder tooltipManager = new TooltipBuilder();
            tooltipManager.appendTitle(project.getName());

            String dateFormat = LocaleHelper.getDateFormatInstance(locale).getDateFormat();

            Tr trRow1 = new Tr();
            Td cell11 = buildCellName(LocalizationHelper.getMessage(locale, ProjectI18nEnum.FORM_HOME_PAGE));
            String homepageLink = (project.getHomepage() != null) ? project.getHomepage() : "";
            Td cell12 = buildCellLink(homepageLink, project.getHomepage());
            Td cell13 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS));
            Td cell14 = buildCellValue(LocalizationHelper.getMessage(locale, StatusI18nEnum.class, project.getProjectstatus()));
            trRow1.appendChild(cell11, cell12, cell13, cell14);

            Tr trRow2 = new Tr();
            Td cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_START_DATE));
            String planStartDate = DateTimeUtils.convertToStringWithUserTimeZone(project.getPlanstartdate(), dateFormat, timeZone);
            Td cell22 = buildCellValue(planStartDate);
            Td cell23 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_CURRENCY));
            String currency = (project.getCurrencyid() != null) ? project.getCurrencyid() : "";
            Td cell24 = buildCellValue(currency);
            trRow2.appendChild(cell21, cell22, cell23, cell24);
            tooltipManager.appendRow(trRow2);

            Tr trRow3 = new Tr();
            Td cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_END_DATE));
            String planEndDate = DateTimeUtils.convertToStringWithUserTimeZone(project.getPlanenddate(), dateFormat, timeZone);
            Td cell32 = buildCellValue(planEndDate);
            Td cell33 = buildCellName(LocalizationHelper.getMessage(locale, ProjectI18nEnum.FORM_BILLING_RATE));
            Td cell34 = buildCellValue(project.getDefaultbillingrate());
            trRow3.appendChild(cell31, cell32, cell33, cell34);
            tooltipManager.appendRow(trRow3);

            Tr trRow4 = new Tr();
            Td cell41 = buildCellName(LocalizationHelper.getMessage(locale, ProjectI18nEnum.FORM_TARGET_BUDGET));
            Td cell42 = buildCellValue(project.getTargetbudget());
            Td cell43 = buildCellName(LocalizationHelper.getMessage(locale, ProjectI18nEnum.FORM_ACTUAL_BUDGET));
            Td cell44 = buildCellValue(project.getActualbudget());
            trRow4.appendChild(cell41, cell42, cell43, cell44);
            tooltipManager.appendRow(trRow4);

            Tr trRow6 = new Tr();
            Td cell61 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION));
            Td cell62 = buildCellValue(trimHtmlTags(project.getDescription()));
            cell62.setAttribute("colspan", "3");
            trRow6.appendChild(cell61, cell62);
            tooltipManager.appendRow(trRow6);

            return tooltipManager.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate tooltip for servlet project tooltip", e);
            return null;
        }
    }

    public static String generateToolTipMilestone(Locale locale, SimpleMilestone milestone, String siteURL, TimeZone
            timeZone, boolean showProject) {
        if (milestone == null)
            return generateTolltipNull(locale);

        try {
            TooltipBuilder tooltipManager = new TooltipBuilder();
            tooltipManager.appendTitle(milestone.getName());

            if (showProject) {
                tooltipManager.appendTitle(String.format("[%s] %s", milestone.getProjectShortName(), milestone.getProjectName
                        ()));
            }

            String dateFormat = LocaleHelper.getDateFormatInstance(locale).getDateFormat();

            Tr trRow2 = new Tr();
            Td cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_START_DATE));
            String startDate = DateTimeUtils.convertToStringWithUserTimeZone(milestone.getStartdate(), dateFormat, timeZone);
            Td cell22 = buildCellValue(startDate);
            Td cell23 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE));
            String assignUserLink = (milestone.getOwner() != null) ? AccountLinkGenerator.generatePreviewFullUserLink(siteURL, milestone.getOwner()) : "";
            String assignUserAvatarLink = StorageFactory.getInstance().getAvatarPath(milestone.getOwnerAvatarId(), 16);
            Td cell24 = buildCellLink(assignUserLink, assignUserAvatarLink,
                    milestone.getOwnerFullName());
            trRow2.appendChild(cell21, cell22, cell23, cell24);
            tooltipManager.appendRow(trRow2);

            Tr trRow3 = new Tr();
            Td cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_END_DATE));
            String endDate = DateTimeUtils.convertToStringWithUserTimeZone(milestone.getEnddate(), dateFormat, timeZone);
            Td cell32 = buildCellValue(endDate);
            Td cell33 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS));
            Td cell34 = buildCellValue(LocalizationHelper.getMessage(locale, MilestoneStatus.class, milestone.getStatus()));
            trRow3.appendChild(cell31, cell32, cell33, cell34);
            tooltipManager.appendRow(trRow3);

            Tr trRow6 = new Tr();
            Td cell61 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION));
            Td cell62 = buildCellValue(trimHtmlTags(milestone.getDescription()));
            cell62.setAttribute("colspan", "3");
            trRow6.appendChild(cell61, cell62);
            tooltipManager.appendRow(trRow6);

            return tooltipManager.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate tooltip for servlet project tooltip", e);
            return null;
        }
    }

    public static String generateToolTipStandUp(Locale locale, SimpleStandupReport standup, String siteURL, TimeZone timeZone) {
        if (standup == null)
            return generateTolltipNull(locale);

        try {
            String dateFormat = LocaleHelper.getDateFormatInstance(locale).getDateFormat();
            Div div = new Div().setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
            H3 name = new H3();
            name.appendText(Jsoup.parse(DateTimeUtils.convertToStringWithUserTimeZone(
                    standup.getCreatedtime(), dateFormat, timeZone)).html());
            div.appendChild(name);

            Table table = new Table();
            table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");

            Tr trRow3 = new Tr();
            trRow3.appendChild(new Td().setStyle("width: 165px; vertical-align: top; text-align: right;")
                    .appendText(LocalizationHelper.getMessage(locale, StandupI18nEnum.STANDUP_LASTDAY)))
                    .appendChild(new Td().setStyle("word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
                            .appendText(standup.getWhatlastday()));

            Tr trRow4 = new Tr();
            trRow4.appendChild(new Td().setStyle("width: 165px;vertical-align: top; text-align: right;")
                    .appendText(LocalizationHelper.getMessage(locale, StandupI18nEnum.STANDUP_TODAY)))
                    .appendChild(new Td().setStyle("break-word; white-space: normal;vertical-align: top; word-break: break-all;")
                            .appendText(standup.getWhattoday()));
            Tr trRow5 = new Tr();
            trRow5.appendChild(new Td().setStyle("width: 165px;vertical-align: top; text-align: right;")
                    .appendText(LocalizationHelper.getMessage(locale, StandupI18nEnum.STANDUP_ISSUE)))
                    .appendChild(new Td().setStyle("break-word; white-space: normal;vertical-align: top; word-break: break-all;")
                            .appendText(standup.getWhatproblem()));

            table.appendChild(trRow3);
            table.appendChild(trRow4);
            table.appendChild(trRow5);
            div.appendChild(table);

            return div.write();
        } catch (Exception e) {
            LOG.error("Error while generate tooltip for servlet project tooltip", e);
            return null;
        }
    }

    public static String generateToolTipPage(Locale locale, Page page, String siteURL, TimeZone timeZone) {
        if (page == null)
            return generateTolltipNull(locale);

        try {
            TooltipBuilder tooltipManager = new TooltipBuilder();
            tooltipManager.appendTitle(page.getSubject());

            Tr trRow2 = new Tr();
            Td cell21 = new Td().setStyle("vertical-align: top; text-align: left;word-wrap: break-word; white-space: normal;vertical-align: top;")
                    .appendText(StringUtils.trim(page.getContent(), 500, true));

            trRow2.appendChild(cell21);
            tooltipManager.appendRow(trRow2);

            return tooltipManager.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate tooltip for servlet project tooltip", e);
            return null;
        }
    }

    public static String generateToolTipMessage(Locale locale, SimpleMessage message, String siteURL, TimeZone timeZone) {
        if (message == null)
            return generateTolltipNull(locale);

        try {
            TooltipBuilder tooltipManager = new TooltipBuilder();
            tooltipManager.appendTitle(message.getTitle());

            Tr trRow2 = new Tr();
            Td cell21 = new Td().setStyle("vertical-align: top; text-align: left;word-wrap: break-word; white-space: normal;vertical-align: top;")
                    .appendText(StringUtils.trim(message.getMessage(), 500, true));

            trRow2.appendChild(cell21);
            tooltipManager.appendRow(trRow2);

            return tooltipManager.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate tooltip for servlet project tooltip", e);
            return null;
        }
    }
}