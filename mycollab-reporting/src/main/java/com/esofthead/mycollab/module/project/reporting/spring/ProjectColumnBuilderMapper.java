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
package com.esofthead.mycollab.module.project.reporting.spring;

import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.*;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.SimpleComponent;
import com.esofthead.mycollab.module.tracker.domain.SimpleVersion;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.reporting.ColumnBuilderClassMapper;
import com.esofthead.mycollab.reporting.expression.*;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
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
@SuppressWarnings("ucd")
public class ProjectColumnBuilderMapper implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectColumnBuilderMapper.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        ColumnBuilderClassMapper.put(SimpleTask.class, buildTaskMap());
        ColumnBuilderClassMapper.put(SimpleBug.class, buildBugMap());
        ColumnBuilderClassMapper.put(SimpleComponent.class, buildComponentMap());
        ColumnBuilderClassMapper.put(SimpleVersion.class, buildVersionMap());
        ColumnBuilderClassMapper.put(SimpleRisk.class, buildRiskMap());
        ColumnBuilderClassMapper.put(SimpleProblem.class, buildProblemMap());
        ColumnBuilderClassMapper.put(SimpleProjectRole.class, buildRoleMap());
        ColumnBuilderClassMapper.put(SimpleItemTimeLogging.class, buildTimeTrackingMap());
        ColumnBuilderClassMapper.put(FollowingTicket.class, buildTFollowingTicketMap());
    }

    private Map<String, MValue> buildTaskMap() {
        LOG.debug("Build report mapper for project::task module");
        Map<String, MValue> map = new HashMap<>();
        DRIExpression<String> taskNameTitleExpr = new PrimityTypeFieldExpression(Task.Field.taskname.name());
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
        map.put(Task.Field.taskname.name(), new HyperlinkValue(taskNameTitleExpr, taskNameHrefExpr));
        map.put(Task.Field.startdate.name(), new DateExpression(Task.Field.startdate.name()));
        map.put(Task.Field.deadline.name(), new DateExpression(Task.Field.deadline.name()));
        map.put(Task.Field.status.name(), new I18nExpression("status", com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.class));

        DRIExpression<String> assigneeTitleExpr = new PrimityTypeFieldExpression(SimpleTask.Field.assignUserFullName.name());
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

        map.put(SimpleTask.Field.assignUserFullName.name(), new HyperlinkValue(assigneeTitleExpr, assigneeHrefExpr));
        return map;
    }

    private Map<String, MValue> buildBugMap() {
        LOG.debug("Build report mapper for project::bug module");

        Map<String, MValue> map = new HashMap<>();
        DRIExpression<String> summaryTitleExpr = new PrimityTypeFieldExpression("summary");
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
        map.put("summary", new HyperlinkValue(summaryTitleExpr, summaryHrefExpr));

        DRIExpression<String> assigneeTitleExpr = new PrimityTypeFieldExpression("assignuserFullName");
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

        DRIExpression<String> logUserTitleExpr = new PrimityTypeFieldExpression("loguserFullName");
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

        map.put("severity", new I18nExpression("severity", OptionI18nEnum.BugSeverity.class));
        map.put("priority", new I18nExpression("priority", OptionI18nEnum.BugPriority.class));
        map.put("status", new I18nExpression("status", OptionI18nEnum.BugStatus.class));
        map.put("resolution", new I18nExpression("resolution", OptionI18nEnum.BugResolution.class));
        map.put("assignuserFullName", new HyperlinkValue(assigneeTitleExpr, assigneeHrefExpr));
        map.put("loguserFullName", new HyperlinkValue(logUserTitleExpr, logUserHrefExpr));
        map.put("duedate", new DateExpression("duedate"));
        map.put("billableHours", new HumanTimeExpression("billableHours"));
        map.put("nonBillableHours", new HumanTimeExpression("nonBillableHours"));
        return map;
    }

    private Map<String, MValue> buildComponentMap() {
        LOG.debug("Build report mapper for project::component module");

        Map<String, MValue> map = new HashMap<>();
        DRIExpression<String> summaryTitleExpr = new PrimityTypeFieldExpression("componentname");
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
        map.put("componentname", new HyperlinkValue(summaryTitleExpr, summaryHrefExpr));

        DRIExpression<String> assigneeTitleExpr = new PrimityTypeFieldExpression("userLeadFullName");
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

        map.put("userLeadFullName", new HyperlinkValue(assigneeTitleExpr, assigneeHrefExpr));
        return map;
    }

    private Map<String, MValue> buildVersionMap() {
        LOG.debug("Build report mapper for project::version module");

        Map<String, MValue> map = new HashMap<>();
        DRIExpression<String> summaryTitleExpr = new PrimityTypeFieldExpression("versionname");
        DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer versionid = reportParameters.getFieldValue("id");
                Integer projectId = reportParameters.getFieldValue("projectid");
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return ProjectLinkGenerator.generateBugVersionPreviewFullLink(siteUrl, projectId, versionid);
            }
        };
        map.put("versionname", new HyperlinkValue(summaryTitleExpr, summaryHrefExpr));
        map.put("duedate", new DateExpression("duedate"));
        return map;
    }

    private Map<String, MValue> buildRiskMap() {
        LOG.debug("Build report mapper for project::risk module");

        Map<String, MValue> map = new HashMap<>();
        DRIExpression<String> summaryTitleExpr = new PrimityTypeFieldExpression(Risk.Field.riskname.name());
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
        map.put(Risk.Field.riskname.name(), new HyperlinkValue(summaryTitleExpr, summaryHrefExpr));

        DRIExpression<String> assigneeTitleExpr = new PrimityTypeFieldExpression(SimpleRisk.Field.assignedToUserFullName.name());
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

        map.put(SimpleRisk.Field.assignedToUserFullName.name(), new HyperlinkValue(assigneeTitleExpr, assigneeHrefExpr));

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
        HorizontalListBuilder ratingBuilder = cmp.horizontalList().setFixedWidth(120);
        ImageBuilder imgBuilder = cmp.image(ratingExpr).setFixedDimension(80, 15);
        ratingBuilder.add(imgBuilder);
        map.put(Risk.Field.level.name(), new CompBuilderValue(ratingBuilder));
        map.put(Risk.Field.datedue.name(), new DateExpression(Risk.Field.datedue.name()));

        return map;
    }

    private Map<String, MValue> buildProblemMap() {
        LOG.debug("Build report mapper for project::problem module");

        Map<String, MValue> map = new HashMap<>();
        DRIExpression<String> summaryTitleExpr = new PrimityTypeFieldExpression(Problem.Field.issuename.name());
        DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer problemId = reportParameters.getFieldValue(Problem.Field.id.name());
                Integer projectId = reportParameters.getFieldValue(Problem.Field.projectid.name());
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return ProjectLinkGenerator.generateProblemPreviewFullLink(siteUrl, projectId, problemId);
            }
        };
        map.put(Problem.Field.issuename.name(), new HyperlinkValue(summaryTitleExpr, summaryHrefExpr));

        DRIExpression<String> assigneeTitleExpr = new PrimityTypeFieldExpression(SimpleProblem.Field.assignedUserFullName.name());
        DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                String assignUser = reportParameters.getFieldValue(Problem.Field.assigntouser.name());
                if (assignUser != null) {
                    String siteUrl = reportParameters.getParameterValue("siteUrl");
                    return AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, assignUser);
                }

                return "";
            }
        };

        map.put(SimpleProblem.Field.assignedUserFullName.name(), new HyperlinkValue(assigneeTitleExpr, assigneeHrefExpr));

        AbstractSimpleExpression<String> ratingExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters param) {
                Double level = param.getFieldValue("level");
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
        HorizontalListBuilder ratingBuilder = cmp.horizontalList().setFixedWidth(120);
        ImageBuilder imgBuilder = cmp.image(ratingExpr).setFixedDimension(80, 15);
        ratingBuilder.add(imgBuilder);
        map.put(Problem.Field.level.name(), new CompBuilderValue(ratingBuilder));
        map.put(Problem.Field.datedue.name(), new DateExpression(Problem.Field.datedue.name()));
        return map;
    }

    private Map<String, MValue> buildRoleMap() {
        LOG.debug("Build report mapper for project::role module");

        Map<String, MValue> map = new HashMap<>();
        DRIExpression<String> summaryTitleExpr = new PrimityTypeFieldExpression("rolename");
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
        map.put("rolename", new HyperlinkValue(summaryTitleExpr, summaryHrefExpr));
        return map;
    }

    private Map<String, MValue> buildTimeTrackingMap() {
        LOG.debug("Build report mapper for project::timetracking module");

        Map<String, MValue> map = new HashMap<>();
        DRIExpression<String> logUserTitleExpr = new PrimityTypeFieldExpression("logUserFullName");
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

        map.put("logUserFullName", new HyperlinkValue(logUserTitleExpr, logUserHrefExpr));

        DRIExpression<String> projectTitleExpr = new PrimityTypeFieldExpression("projectName");
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

        map.put("projectName", new HyperlinkValue(projectTitleExpr, projectHrefExpr));
        map.put("logforday", new DateExpression("logforday"));

        AbstractSimpleExpression<String> billingExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters param) {
                Boolean level = param.getFieldValue("isbillable");
                if (Boolean.TRUE.equals(level)) {
                    return "images/yes.png";
                } else {
                    return "images/no.png";
                }
            }
        };
        HorizontalListBuilder ratingBuilder = cmp.horizontalList().setFixedWidth(120);
        ImageBuilder imgBuilder = cmp.image(billingExpr).setFixedDimension(80, 15);
        ratingBuilder.add(imgBuilder);
        map.put("isbillable", new CompBuilderValue(ratingBuilder));

        DRIExpression<String> summaryTitleExpr = new PrimityTypeFieldExpression("summary");
        DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                String type = reportParameters.getFieldValue("type");
                Integer typeId = reportParameters.getFieldValue("typeid");
                Integer projectId = reportParameters.getFieldValue("projectId");
                String projectShortName = reportParameters.getFieldValue("projectShortName");
                String siteUrl = reportParameters.getParameterValue("siteUrl");

                if (type == null) {
                    return "";
                } else if (type.equals(ProjectTypeConstants.BUG)) {
                    return ProjectLinkGenerator.generateBugPreviewFullLink(siteUrl, typeId, projectShortName);
                } else if (type.equals(ProjectTypeConstants.TASK)) {
                    return ProjectLinkGenerator.generateTaskPreviewFullLink(siteUrl, typeId, projectShortName);
                } else if (type.equals(ProjectTypeConstants.PROBLEM)) {
                    return ProjectLinkGenerator.generateProblemPreviewFullLink(siteUrl, projectId, typeId);
                } else if (type.equals(ProjectTypeConstants.RISK)) {
                    return ProjectLinkGenerator.generateRiskPreviewFullLink(siteUrl, projectId, typeId);
                }
                return type;
            }
        };
        map.put("summary", new HyperlinkValue(summaryTitleExpr, summaryHrefExpr));
        return map;
    }

    private Map<String, MValue> buildTFollowingTicketMap() {
        LOG.debug("Build report mapper for project::following ticket module");

        Map<String, MValue> map = new HashMap<>();
        DRIExpression<String> summaryTitleExpr = new PrimityTypeFieldExpression("summary");
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
        map.put("summary", new HyperlinkValue(summaryTitleExpr, summaryHrefExpr));

        DRIExpression<String> projectTitleExpr = new PrimityTypeFieldExpression("projectName");
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

        map.put("projectName", new HyperlinkValue(projectTitleExpr, projectHrefExpr));

        DRIExpression<String> logUserTitleExpr = new PrimityTypeFieldExpression("assignUserFullName");
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

        map.put("assignUser", new HyperlinkValue(logUserTitleExpr, logUserHrefExpr));
        map.put("monitorDate", new DateExpression("monitorDate"));

        return map;
    }
}
