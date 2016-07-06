/**
 * This file is part of mycollab-reporting.
 *
 * mycollab-reporting is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-reporting is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-reporting.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.reporting.spring;

import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.*;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.SimpleComponent;
import com.mycollab.module.tracker.domain.SimpleVersion;
import com.mycollab.module.user.AccountLinkGenerator;
import com.mycollab.reporting.ColumnBuilderClassMapper;
import com.mycollab.reporting.ReportStyles;
import com.mycollab.reporting.expression.DateExpression;
import com.mycollab.reporting.expression.HumanTimeExpression;
import com.mycollab.reporting.expression.I18nExpression;
import com.mycollab.reporting.expression.PrimaryTypeFieldExpression;
import com.mycollab.reporting.generator.ComponentBuilderGenerator;
import com.mycollab.reporting.generator.HyperlinkBuilderGenerator;
import com.mycollab.reporting.generator.SimpleExpressionBuilderGenerator;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.ImageBuilder;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
@Component
public class ProjectColumnBuilderMapper implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectColumnBuilderMapper.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        ColumnBuilderClassMapper.put(SimpleProject.class, buildProjectMap());
        ColumnBuilderClassMapper.put(SimpleMilestone.class, buildMilestoneMap());
        ColumnBuilderClassMapper.put(SimpleTask.class, buildTaskMap());
        ColumnBuilderClassMapper.put(SimpleBug.class, buildBugMap());
        ColumnBuilderClassMapper.put(SimpleComponent.class, buildComponentMap());
        ColumnBuilderClassMapper.put(SimpleVersion.class, buildVersionMap());
        ColumnBuilderClassMapper.put(SimpleRisk.class, buildRiskMap());
        ColumnBuilderClassMapper.put(SimpleProjectRole.class, buildRoleMap());
        ColumnBuilderClassMapper.put(SimpleProjectMember.class, buildProjectMemberMap());
        ColumnBuilderClassMapper.put(SimpleItemTimeLogging.class, buildTimeTrackingMap());
        ColumnBuilderClassMapper.put(FollowingTicket.class, buildTFollowingTicketMap());
    }

    private Map<String, ComponentBuilderGenerator> buildProjectMap() {
        Map<String, ComponentBuilderGenerator> map = new HashMap<>();
        DRIExpression<String> projectNameExpr = new PrimaryTypeFieldExpression<>(Project.Field.name.name());
        DRIExpression<String> projectHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer projectId = reportParameters.getFieldValue(Project.Field.id.name());
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return ProjectLinkGenerator.generateProjectFullLink(siteUrl, projectId);
            }
        };
        map.put(Milestone.Field.name.name(), new HyperlinkBuilderGenerator(projectNameExpr, projectHrefExpr));

        DRIExpression<String> leadNameExpr = new PrimaryTypeFieldExpression<>(SimpleProject.Field.leadFullName.name());
        DRIExpression<String> leadHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer projectId = reportParameters.getFieldValue(Project.Field.id.name());
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                String memberName = reportParameters.getParameterValue(Project.Field.lead.name());
                return ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, projectId, memberName);
            }
        };
        map.put(Project.Field.lead.name(), new HyperlinkBuilderGenerator(leadNameExpr, leadHrefExpr));

        DRIExpression<String> accountNameExpr = new PrimaryTypeFieldExpression<>(SimpleProject.Field.clientName.name());
        DRIExpression<String> clientHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                Integer accountId = reportParameters.getParameterValue(Project.Field.accountid.name());
                return ProjectLinkGenerator.generateClientPreviewFullLink(siteUrl, accountId);
            }
        };
        map.put(Project.Field.accountid.name(), new HyperlinkBuilderGenerator(accountNameExpr, clientHrefExpr));

        DRIExpression<String> homePageUrlExpr = new PrimaryTypeFieldExpression<>(Project.Field.homepage.name());
        map.put(Project.Field.homepage.name(), new HyperlinkBuilderGenerator(homePageUrlExpr, homePageUrlExpr));

        map.put(Project.Field.createdtime.name(), new SimpleExpressionBuilderGenerator(new DateExpression(Project.Field.createdtime.name())));
        map.put(Project.Field.planstartdate.name(), new SimpleExpressionBuilderGenerator(new DateExpression(Project.Field.planstartdate.name())));
        map.put(Project.Field.planenddate.name(), new SimpleExpressionBuilderGenerator(new DateExpression(Project.Field.planenddate.name())));
        return map;
    }

    private Map<String, ComponentBuilderGenerator> buildMilestoneMap() {
        LOG.debug("Build report mapper for project::milestone module");
        Map<String, ComponentBuilderGenerator> map = new HashMap<>();
        DRIExpression<String> milestoneNameExpr = new PrimaryTypeFieldExpression<>(Milestone.Field.name.name());
        DRIExpression<String> milestoneHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer milestoneId = reportParameters.getFieldValue(Milestone.Field.id.name());
                Integer projectId = reportParameters.getFieldValue(Milestone.Field.projectid.name());
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return ProjectLinkGenerator.generateMilestonePreviewFullLink(siteUrl, projectId, milestoneId);
            }
        };
        map.put(Milestone.Field.name.name(), new HyperlinkBuilderGenerator(milestoneNameExpr, milestoneHrefExpr));
        map.put(Milestone.Field.status.name(), new SimpleExpressionBuilderGenerator(new I18nExpression("status",
                OptionI18nEnum.MilestoneStatus.class)));
        map.put(Milestone.Field.startdate.name(), new SimpleExpressionBuilderGenerator(new DateExpression(Milestone.Field.startdate.name())));
        map.put(Milestone.Field.enddate.name(), new SimpleExpressionBuilderGenerator(new DateExpression(Milestone.Field.enddate.name())));
        DRIExpression<String> assigneeTitleExpr = new PrimaryTypeFieldExpression<>(SimpleMilestone.Field.ownerFullName.name());
        DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                String assignUser = reportParameters.getFieldValue(Milestone.Field.owner.name());
                if (assignUser != null) {
                    String siteUrl = reportParameters.getParameterValue("siteUrl");
                    return AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, assignUser);
                }

                return "";
            }
        };

        map.put(SimpleMilestone.Field.ownerFullName.name(), new HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr));

        AbstractSimpleExpression<Double> progressExpr = new AbstractSimpleExpression<Double>() {
            @Override
            public Double evaluate(ReportParameters reportParameters) {
                Integer numOpenBugs = reportParameters.getFieldValue(SimpleMilestone.Field.numOpenBugs.name());
                Integer numBugs = reportParameters.getFieldValue(SimpleMilestone.Field.numBugs.name());
                Integer numOpenTasks = reportParameters.getFieldValue(SimpleMilestone.Field.numOpenTasks.name());
                Integer numTasks = reportParameters.getFieldValue(SimpleMilestone.Field.numTasks.name());
                int openAssignments = numOpenBugs + numOpenTasks;
                int totalAssignments = numBugs + numTasks;
                return (totalAssignments > 0) ? ((totalAssignments - openAssignments) * 1d / totalAssignments) * 100d : 100d;
            }
        };
        map.put(Milestone.Field.id.name(), new SimpleExpressionBuilderGenerator(progressExpr));


        map.put(SimpleMilestone.Field.totalBillableHours.name(), new SimpleExpressionBuilderGenerator(new AbstractSimpleExpression<Double>() {
            @Override
            public Double evaluate(ReportParameters reportParameters) {
                Double taskBillableHours = reportParameters.getFieldValue(SimpleMilestone.Field.totalTaskBillableHours.name());
                Double bugBillableHours = reportParameters.getFieldValue(SimpleMilestone.Field.totalBugBillableHours.name());
                return taskBillableHours + bugBillableHours;
            }
        }));
        map.put(SimpleMilestone.Field.totalNonBillableHours.name(), new SimpleExpressionBuilderGenerator(new AbstractSimpleExpression<Double>() {
            @Override
            public Double evaluate(ReportParameters reportParameters) {
                Double taskNonBillableHours = reportParameters.getFieldValue(SimpleMilestone.Field.totalTaskNonBillableHours.name());
                Double bugNonBillableHours = reportParameters.getFieldValue(SimpleMilestone.Field.totalBugNonBillableHours.name());
                return taskNonBillableHours + bugNonBillableHours;
            }
        }));
        return map;
    }

    private Map<String, ComponentBuilderGenerator> buildTaskMap() {
        LOG.debug("Build report mapper for project::task module");
        Map<String, ComponentBuilderGenerator> map = new HashMap<>();
        DRIExpression<String> taskNameTitleExpr = new PrimaryTypeFieldExpression<>(Task.Field.taskname.name());
        DRIExpression<String> taskNameHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer taskKey = reportParameters.getFieldValue(Task.Field.taskkey.name());
                String projectShortName = reportParameters.getFieldValue("projectShortname");
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return ProjectLinkGenerator.generateTaskPreviewFullLink(siteUrl, taskKey, projectShortName);
            }
        };
        map.put(Task.Field.taskname.name(), new HyperlinkBuilderGenerator(taskNameTitleExpr, taskNameHrefExpr));
        map.put(Task.Field.startdate.name(), new SimpleExpressionBuilderGenerator(new DateExpression(Task.Field.startdate.name())));
        map.put(Task.Field.enddate.name(), new SimpleExpressionBuilderGenerator(new DateExpression(Task.Field.enddate.name())));
        map.put(Task.Field.deadline.name(), new SimpleExpressionBuilderGenerator(new DateExpression(Task.Field.deadline.name())));
        map.put(Task.Field.status.name(), new SimpleExpressionBuilderGenerator(new I18nExpression("status",
                com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.class)));

        DRIExpression<String> milestoneTitleExpr = new PrimaryTypeFieldExpression<>(SimpleTask.Field.milestoneName.name());
        DRIExpression<String> milestoneHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer milestoneId = reportParameters.getFieldValue("milestoneid");
                if (milestoneId != null) {
                    String siteUrl = reportParameters.getParameterValue("siteUrl");
                    Integer projectId = reportParameters.getFieldValue("projectid");
                    return ProjectLinkGenerator.generateMilestonePreviewFullLink(siteUrl, projectId, milestoneId);
                }

                return "";
            }
        };
        map.put(SimpleTask.Field.milestoneName.name(), new HyperlinkBuilderGenerator(milestoneTitleExpr, milestoneHrefExpr));

        DRIExpression<String> logUserTitleExpr = new PrimaryTypeFieldExpression<>(SimpleTask.Field.logByFullName.name());
        DRIExpression<String> logUserHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                String logByUser = reportParameters.getFieldValue("logby");
                if (logByUser != null) {
                    String siteUrl = reportParameters.getParameterValue("siteUrl");
                    return AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, logByUser);
                }

                return "";
            }
        };
        map.put(SimpleTask.Field.logByFullName.name(), new HyperlinkBuilderGenerator(logUserTitleExpr, logUserHrefExpr));

        DRIExpression<String> assigneeTitleExpr = new PrimaryTypeFieldExpression<>(SimpleTask.Field.assignUserFullName.name());
        DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                String assignUser = reportParameters.getFieldValue("assignuser");
                if (assignUser != null) {
                    String siteUrl = reportParameters.getParameterValue("siteUrl");
                    return AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, assignUser);
                }

                return "";
            }
        };

        map.put(SimpleTask.Field.assignUserFullName.name(), new HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr));
        return map;
    }

    private Map<String, ComponentBuilderGenerator> buildBugMap() {
        LOG.debug("Build report mapper for project::bug module");

        Map<String, ComponentBuilderGenerator> map = new HashMap<>();
        DRIExpression<String> summaryTitleExpr = new PrimaryTypeFieldExpression<>("summary");
        DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer bugKey = reportParameters.getFieldValue("bugkey");
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                String projectShortName = reportParameters.getFieldValue("projectShortName");
                return ProjectLinkGenerator.generateBugPreviewFullLink(siteUrl, bugKey, projectShortName);
            }
        };
        map.put("summary", new HyperlinkBuilderGenerator(summaryTitleExpr, summaryHrefExpr));

        DRIExpression<String> assigneeTitleExpr = new PrimaryTypeFieldExpression<>("assignuserFullName");
        DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                String assignUser = reportParameters.getFieldValue("assignuser");
                if (assignUser != null) {
                    String siteUrl = reportParameters.getParameterValue("siteUrl");
                    return AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, assignUser);
                }

                return "";
            }
        };

        DRIExpression<String> logUserTitleExpr = new PrimaryTypeFieldExpression<>("loguserFullName");
        DRIExpression<String> logUserHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                String logUser = reportParameters.getFieldValue("logby");
                if (logUser != null) {
                    String siteUrl = reportParameters.getParameterValue("siteUrl");
                    return AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, logUser);
                }

                return "";
            }
        };

        DRIExpression<String> milestoneTitleExpr = new PrimaryTypeFieldExpression<>(SimpleBug.Field.milestoneName.name());
        DRIExpression<String> milestoneHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer milestoneId = reportParameters.getFieldValue("milestoneid");
                if (milestoneId != null) {
                    String siteUrl = reportParameters.getParameterValue("siteUrl");
                    Integer projectId = reportParameters.getFieldValue("projectid");
                    return ProjectLinkGenerator.generateMilestonePreviewFullLink(siteUrl, projectId, milestoneId);
                }

                return "";
            }
        };
        map.put(SimpleBug.Field.milestoneName.name(), new HyperlinkBuilderGenerator(milestoneTitleExpr, milestoneHrefExpr));

        map.put("severity", new SimpleExpressionBuilderGenerator(new I18nExpression("severity", OptionI18nEnum.BugSeverity.class)));
        map.put("priority", new SimpleExpressionBuilderGenerator(new I18nExpression("priority", OptionI18nEnum.BugPriority.class)));
        map.put("status", new SimpleExpressionBuilderGenerator(new I18nExpression("status", OptionI18nEnum.BugStatus.class)));
        map.put("resolution", new SimpleExpressionBuilderGenerator(new I18nExpression("resolution", OptionI18nEnum.BugResolution.class)));
        map.put("assignuserFullName", new HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr));
        map.put("loguserFullName", new HyperlinkBuilderGenerator(logUserTitleExpr, logUserHrefExpr));
        map.put("duedate", new SimpleExpressionBuilderGenerator(new DateExpression("duedate")));
        map.put("startdate", new SimpleExpressionBuilderGenerator(new DateExpression("startdate")));
        map.put("enddate", new SimpleExpressionBuilderGenerator(new DateExpression("enddate")));
        map.put("billableHours", new SimpleExpressionBuilderGenerator(new HumanTimeExpression("billableHours")));
        map.put("nonBillableHours", new SimpleExpressionBuilderGenerator(new HumanTimeExpression("nonBillableHours")));
        return map;
    }

    private Map<String, ComponentBuilderGenerator> buildComponentMap() {
        LOG.debug("Build report mapper for project::component module");

        Map<String, ComponentBuilderGenerator> map = new HashMap<>();
        DRIExpression<String> summaryTitleExpr = new PrimaryTypeFieldExpression<>("componentname");
        DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer componentId = reportParameters.getFieldValue("id");
                Integer projectId = reportParameters.getFieldValue("projectid");
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return ProjectLinkGenerator.generateBugComponentPreviewFullLink(siteUrl, projectId, componentId);
            }
        };
        map.put("componentname", new HyperlinkBuilderGenerator(summaryTitleExpr, summaryHrefExpr));

        DRIExpression<String> assigneeTitleExpr = new PrimaryTypeFieldExpression<>("userLeadFullName");
        DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                String assignUser = reportParameters.getFieldValue("userlead");
                if (assignUser != null) {
                    String siteUrl = reportParameters.getParameterValue("siteUrl");
                    return AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, assignUser);
                }

                return "";
            }
        };

        map.put("userLeadFullName", new HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr));
        AbstractSimpleExpression<Double> progressExpr = new AbstractSimpleExpression<Double>() {
            @Override
            public Double evaluate(ReportParameters reportParameters) {
                Integer numOpenBugs = reportParameters.getFieldValue(SimpleComponent.Field.numOpenBugs.name());
                Integer numBugs = reportParameters.getFieldValue(SimpleComponent.Field.numBugs.name());
                return numBugs != null && numBugs != 0 ? ((numBugs - numOpenBugs) * 1d / numBugs) * 100d : 100d;
            }
        };
        map.put("id", new SimpleExpressionBuilderGenerator(progressExpr));
        return map;
    }

    private Map<String, ComponentBuilderGenerator> buildVersionMap() {
        LOG.debug("Build report mapper for project::version module");

        Map<String, ComponentBuilderGenerator> map = new HashMap<>();
        DRIExpression<String> summaryTitleExpr = new PrimaryTypeFieldExpression<>("versionname");
        DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer versionId = reportParameters.getFieldValue("id");
                Integer projectId = reportParameters.getFieldValue("projectid");
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return ProjectLinkGenerator.generateBugVersionPreviewFullLink(siteUrl, projectId, versionId);
            }
        };
        map.put("versionname", new HyperlinkBuilderGenerator(summaryTitleExpr, summaryHrefExpr));
        map.put("duedate", new SimpleExpressionBuilderGenerator(new DateExpression("duedate")));
        AbstractSimpleExpression<Double> progressExpr = new AbstractSimpleExpression<Double>() {
            @Override
            public Double evaluate(ReportParameters reportParameters) {
                Integer numOpenBugs = reportParameters.getFieldValue(SimpleVersion.Field.numOpenBugs.name());
                Integer numBugs = reportParameters.getFieldValue(SimpleVersion.Field.numBugs.name());
                return numBugs != null && numBugs != 0 ? ((numBugs - numOpenBugs) * 1d / numBugs) * 100d : 100d;
            }
        };
        map.put("id", new SimpleExpressionBuilderGenerator(progressExpr));
        return map;
    }

    private Map<String, ComponentBuilderGenerator> buildRiskMap() {
        LOG.debug("Build report mapper for project::risk module");

        Map<String, ComponentBuilderGenerator> map = new HashMap<>();
        DRIExpression<String> summaryTitleExpr = new PrimaryTypeFieldExpression<>(Risk.Field.riskname.name());
        DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer riskId = reportParameters.getFieldValue(Risk.Field.id.name());
                Integer projectId = reportParameters.getFieldValue(Risk.Field.projectid.name());
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return ProjectLinkGenerator.generateRiskPreviewFullLink(siteUrl, projectId, riskId);
            }
        };
        map.put(Risk.Field.riskname.name(), new HyperlinkBuilderGenerator(summaryTitleExpr, summaryHrefExpr));

        DRIExpression<String> assigneeTitleExpr = new PrimaryTypeFieldExpression<>(SimpleRisk.Field.assignedToUserFullName.name());
        DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                String assignUser = reportParameters.getFieldValue(Risk.Field.assigntouser.name());
                if (assignUser != null) {
                    String siteUrl = reportParameters.getParameterValue("siteUrl");
                    return AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, assignUser);
                }

                return "";
            }
        };

        map.put(SimpleRisk.Field.assignedToUserFullName.name(), new HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr));

        AbstractSimpleExpression<String> ratingExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters param) {
                Double level = param.getFieldValue(Risk.Field.level.name());
                switch (level.intValue()) {
                    case 1:
                        return "images/1.png";
                    case 2:
                        return "images/2.png";
                    case 3:
                        return "images/3.png";
                    case 4:
                        return "images/4.png";
                    case 5:
                        return "images/5.png";
                    default:
                        return "images/severity_major.png";
                }

            }
        };
        final HorizontalListBuilder ratingBuilder = cmp.horizontalList().setFixedWidth(120);
        ImageBuilder imgBuilder = cmp.image(ratingExpr).setFixedDimension(80, 15);
        ratingBuilder.add(imgBuilder);
        map.put(Risk.Field.level.name(), new ComponentBuilderGenerator() {
            @Override
            public ComponentBuilder getCompBuilder(ReportStyles reportStyles) {
                return ratingBuilder;
            }
        });
        map.put(Risk.Field.datedue.name(), new SimpleExpressionBuilderGenerator(new DateExpression(Risk.Field.datedue.name())));

        return map;
    }

    private Map<String, ComponentBuilderGenerator> buildProjectMemberMap() {
        Map<String, ComponentBuilderGenerator> map = new HashMap<>();
        DRIExpression<String> memberNameExpr = new PrimaryTypeFieldExpression<>(SimpleProjectMember.Field.memberFullName.name());
        DRIExpression<String> memberHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer projectId = reportParameters.getFieldValue(ProjectMember.Field.projectid.name());
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                String username = reportParameters.getParameterValue(ProjectMember.Field.username.name());
                return ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, projectId, username);
            }
        };
        map.put(ProjectMember.Field.username.name(), new HyperlinkBuilderGenerator(memberNameExpr, memberHrefExpr));

        DRIExpression<String> roleNameExpr = new PrimaryTypeFieldExpression<>(SimpleProjectMember.Field.roleName.name());
        DRIExpression<String> roleHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer projectId = reportParameters.getFieldValue(ProjectMember.Field.projectid.name());
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                Integer roleId = reportParameters.getParameterValue(ProjectMember.Field.projectroleid.name());
                return ProjectLinkGenerator.generateRolePreviewFullLink(siteUrl, projectId, roleId);
            }
        };
        map.put(ProjectMember.Field.projectroleid.name(), new HyperlinkBuilderGenerator(roleNameExpr, roleHrefExpr));
        return map;
    }

    private Map<String, ComponentBuilderGenerator> buildRoleMap() {
        LOG.debug("Build report mapper for project::role module");

        Map<String, ComponentBuilderGenerator> map = new HashMap<>();
        DRIExpression<String> summaryTitleExpr = new PrimaryTypeFieldExpression<>("rolename");
        DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer roleId = reportParameters.getFieldValue("id");
                Integer projectId = reportParameters.getFieldValue(ProjectRole.Field.projectid.name());
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return ProjectLinkGenerator.generateRolePreviewFullLink(siteUrl, projectId, roleId);
            }
        };
        map.put("rolename", new HyperlinkBuilderGenerator(summaryTitleExpr, summaryHrefExpr));
        return map;
    }

    private Map<String, ComponentBuilderGenerator> buildTimeTrackingMap() {
        LOG.debug("Build report mapper for project::timetracking module");

        Map<String, ComponentBuilderGenerator> map = new HashMap<>();
        DRIExpression<String> logUserTitleExpr = new PrimaryTypeFieldExpression<>("logUserFullName");
        DRIExpression<String> logUserHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                String assignUser = reportParameters.getFieldValue("loguser");
                if (assignUser != null) {
                    String siteUrl = reportParameters.getParameterValue("siteUrl");
                    return AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, assignUser);
                }

                return "";
            }
        };

        map.put(SimpleItemTimeLogging.Field.logUserFullName.name(), new HyperlinkBuilderGenerator(logUserTitleExpr, logUserHrefExpr));

        DRIExpression<String> projectTitleExpr = new PrimaryTypeFieldExpression<>("projectName");
        DRIExpression<String> projectHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer projectId = reportParameters.getFieldValue("projectid");
                if (projectId != null) {
                    String siteUrl = reportParameters.getParameterValue("siteUrl");
                    return ProjectLinkGenerator.generateProjectFullLink(siteUrl, projectId);
                }

                return "";
            }
        };

        map.put(SimpleItemTimeLogging.Field.projectName.name(), new HyperlinkBuilderGenerator(projectTitleExpr, projectHrefExpr));
        map.put(ItemTimeLogging.Field.logforday.name(),
                new SimpleExpressionBuilderGenerator(new DateExpression(ItemTimeLogging.Field.logforday.name())));

        AbstractSimpleExpression<String> overtimeExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters param) {
                Boolean level = param.getFieldValue(ItemTimeLogging.Field.isovertime.name());
                return (Boolean.TRUE.equals(level)) ? "Yes" : "No";
            }
        };
        map.put(ItemTimeLogging.Field.isovertime.name(), new SimpleExpressionBuilderGenerator(overtimeExpr));

        AbstractSimpleExpression<String> billingExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters param) {
                Boolean level = param.getFieldValue(ItemTimeLogging.Field.isbillable.name());
                return (Boolean.TRUE.equals(level)) ? "Yes" : "No";
            }
        };
        map.put(ItemTimeLogging.Field.isbillable.name(), new SimpleExpressionBuilderGenerator(billingExpr));

        final DRIExpression<String> summaryTitleExpr = new PrimaryTypeFieldExpression<>(SimpleItemTimeLogging.Field.summary.name());
        final DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                String type = reportParameters.getFieldValue("type");
                Integer typeId = reportParameters.getFieldValue("typeid");
                Integer projectId = reportParameters.getFieldValue("projectid");
                String projectShortName = reportParameters.getFieldValue("projectShortName");
                String siteUrl = reportParameters.getParameterValue("siteUrl");

                if (type == null) {
                    return "";
                } else if (type.equals(ProjectTypeConstants.BUG)) {
                    return ProjectLinkGenerator.generateBugPreviewFullLink(siteUrl, typeId, projectShortName);
                } else if (type.equals(ProjectTypeConstants.TASK)) {
                    return ProjectLinkGenerator.generateTaskPreviewFullLink(siteUrl, typeId, projectShortName);
                } else if (type.equals(ProjectTypeConstants.RISK)) {
                    return ProjectLinkGenerator.generateRiskPreviewFullLink(siteUrl, projectId, typeId);
                }
                return type;
            }
        };

        final DRIExpression<String> noteExpr = new PrimaryTypeFieldExpression<>(ItemTimeLogging.Field.note.name());

        map.put(SimpleItemTimeLogging.Field.summary.name(), new ComponentBuilderGenerator() {
            @Override
            public ComponentBuilder getCompBuilder(ReportStyles abstractReportStyles) {
                return cmp.verticalList(new HyperlinkBuilderGenerator(summaryTitleExpr, summaryHrefExpr)
                        .getCompBuilder(abstractReportStyles), cmp.text(noteExpr));
            }
        });
        return map;
    }

    private Map<String, ComponentBuilderGenerator> buildTFollowingTicketMap() {
        LOG.debug("Build report mapper for project::following ticket module");

        Map<String, ComponentBuilderGenerator> map = new HashMap<>();
        DRIExpression<String> summaryTitleExpr = new PrimaryTypeFieldExpression<>("summary");
        DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                String type = reportParameters.getFieldValue("type");
                Integer typeId = reportParameters.getFieldValue("typeId");
                String projectShortName = reportParameters.getFieldValue("projectShortName");
                String siteUrl = reportParameters.getParameterValue("siteUrl");

                if (type == null) {
                    return "";
                } else if (type.equals(ProjectTypeConstants.BUG)) {
                    return ProjectLinkGenerator.generateBugPreviewFullLink(siteUrl, typeId, projectShortName);
                } else if (type.equals(ProjectTypeConstants.TASK)) {
                    return ProjectLinkGenerator.generateTaskPreviewFullLink(siteUrl, typeId, projectShortName);
                }
                return type;
            }
        };
        map.put("summary", new HyperlinkBuilderGenerator(summaryTitleExpr, summaryHrefExpr));

        DRIExpression<String> projectTitleExpr = new PrimaryTypeFieldExpression<>("projectName");
        DRIExpression<String> projectHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer projectId = reportParameters.getFieldValue("projectId");
                if (projectId != null) {
                    String siteUrl = reportParameters.getParameterValue("siteUrl");
                    return ProjectLinkGenerator.generateProjectFullLink(siteUrl, projectId);
                }

                return "";
            }
        };

        map.put("projectName", new HyperlinkBuilderGenerator(projectTitleExpr, projectHrefExpr));

        DRIExpression<String> logUserTitleExpr = new PrimaryTypeFieldExpression<>("assignUserFullName");
        DRIExpression<String> logUserHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                String assignUser = reportParameters.getFieldValue("assignUser");
                if (assignUser != null) {
                    String siteUrl = reportParameters.getParameterValue("siteUrl");
                    return AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, assignUser);
                }

                return "";
            }
        };

        map.put("assignUser", new HyperlinkBuilderGenerator(logUserTitleExpr, logUserHrefExpr));
        map.put("monitorDate", new SimpleExpressionBuilderGenerator(new DateExpression("monitorDate")));

        return map;
    }
}
