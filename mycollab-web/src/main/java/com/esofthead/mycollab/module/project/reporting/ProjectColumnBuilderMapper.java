/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.reporting;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

import java.util.HashMap;
import java.util.Map;

import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.ImageBuilder;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.FollowingTicket;
import com.esofthead.mycollab.module.project.domain.SimpleItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.SimpleProblem;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.domain.SimpleRisk;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.SimpleComponent;
import com.esofthead.mycollab.module.tracker.domain.SimpleVersion;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.reporting.ColumnBuilderClassMapper;
import com.esofthead.mycollab.reporting.expression.CompBuilderValue;
import com.esofthead.mycollab.reporting.expression.DateExpression;
import com.esofthead.mycollab.reporting.expression.HyperlinkValue;
import com.esofthead.mycollab.reporting.expression.MValue;
import com.esofthead.mycollab.reporting.expression.StringExpression;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
@Component
@SuppressWarnings("ucd")
public class ProjectColumnBuilderMapper implements InitializingBean {

	private static Logger log = LoggerFactory
			.getLogger(ProjectColumnBuilderMapper.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		ColumnBuilderClassMapper.put(SimpleTask.class, buildTaskMap());
		ColumnBuilderClassMapper.put(SimpleBug.class, buildBugMap());
		ColumnBuilderClassMapper
				.put(SimpleComponent.class, buildComponentMap());
		ColumnBuilderClassMapper.put(SimpleVersion.class, buildVersionMap());
		ColumnBuilderClassMapper.put(SimpleRisk.class, buildRiskMap());
		ColumnBuilderClassMapper.put(SimpleProblem.class, buildProblemMap());
		ColumnBuilderClassMapper.put(SimpleProjectRole.class, buildRoleMap());
		ColumnBuilderClassMapper.put(SimpleItemTimeLogging.class,
				buildTimeTrackingMap());
		ColumnBuilderClassMapper.put(FollowingTicket.class,
				buildTFollowingTicketMap());
	}

	private Map<String, MValue> buildTaskMap() {
		log.debug("Build report mapper for project::task module");
		Map<String, MValue> map = new HashMap<String, MValue>();
		DRIExpression<String> taskNameTitleExpr = new StringExpression(
				"taskname");
		DRIExpression<String> taskNameHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer taskid = reportParameters.getFieldValue("id");
				return ProjectLinkBuilder.generateTaskPreviewFullLink(
						CurrentProjectVariables.getProjectId(), taskid);
			}
		};
		map.put("taskname", new HyperlinkValue(taskNameTitleExpr,
				taskNameHrefExpr));

		map.put("startdate", new DateExpression("startdate"));

		map.put("deadline", new DateExpression("deadline"));

		DRIExpression<String> assigneeTitleExpr = new StringExpression(
				"assignUserFullName");
		DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				String assignUser = reportParameters
						.getFieldValue("assignuser");
				if (assignUser != null) {
					return AccountLinkGenerator.generatePreviewFullUserLink(
							AppContext.getSiteUrl(), assignUser);
				}

				return "";
			}
		};

		map.put("assignUserFullName", new HyperlinkValue(assigneeTitleExpr,
				assigneeHrefExpr));
		return map;
	}

	private Map<String, MValue> buildBugMap() {
		log.debug("Build report mapper for project::bug module");

		Map<String, MValue> map = new HashMap<String, MValue>();
		DRIExpression<String> summaryTitleExpr = new StringExpression("summary");
		DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer bugid = reportParameters.getFieldValue("id");
				return ProjectLinkBuilder.generateBugPreviewFullLink(
						CurrentProjectVariables.getProjectId(), bugid);
			}
		};
		map.put("summary",
				new HyperlinkValue(summaryTitleExpr, summaryHrefExpr));

		DRIExpression<String> assigneeTitleExpr = new StringExpression(
				"assignuserFullName");
		DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				String assignUser = reportParameters
						.getFieldValue("assignuser");
				if (assignUser != null) {
					return AccountLinkGenerator.generatePreviewFullUserLink(
							AppContext.getSiteUrl(), assignUser);
				}

				return "";
			}
		};

		map.put("assignuserFullName", new HyperlinkValue(assigneeTitleExpr,
				assigneeHrefExpr));

		map.put("duedate", new DateExpression("duedate"));

		return map;
	}

	private Map<String, MValue> buildComponentMap() {
		log.debug("Build report mapper for project::component module");

		Map<String, MValue> map = new HashMap<String, MValue>();
		DRIExpression<String> summaryTitleExpr = new StringExpression(
				"componentname");
		DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer componentid = reportParameters.getFieldValue("id");
				return ProjectLinkBuilder.generateComponentPreviewFullLink(
						CurrentProjectVariables.getProjectId(), componentid);
			}
		};
		map.put("componentname", new HyperlinkValue(summaryTitleExpr,
				summaryHrefExpr));

		DRIExpression<String> assigneeTitleExpr = new StringExpression(
				"userLeadFullName");
		DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				String assignUser = reportParameters.getFieldValue("userlead");
				if (assignUser != null) {
					return AccountLinkGenerator.generatePreviewFullUserLink(
							AppContext.getSiteUrl(), assignUser);
				}

				return "";
			}
		};

		map.put("userLeadFullName", new HyperlinkValue(assigneeTitleExpr,
				assigneeHrefExpr));

		return map;
	}

	private Map<String, MValue> buildVersionMap() {
		log.debug("Build report mapper for project::version module");

		Map<String, MValue> map = new HashMap<String, MValue>();
		DRIExpression<String> summaryTitleExpr = new StringExpression(
				"versionname");
		DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer versionid = reportParameters.getFieldValue("id");
				return ProjectLinkBuilder.generateBugVersionPreviewFullLink(
						CurrentProjectVariables.getProjectId(), versionid);
			}
		};
		map.put("versionname", new HyperlinkValue(summaryTitleExpr,
				summaryHrefExpr));

		map.put("duedate", new DateExpression("duedate"));
		return map;
	}

	private Map<String, MValue> buildRiskMap() {
		log.debug("Build report mapper for project::risk module");

		Map<String, MValue> map = new HashMap<String, MValue>();
		DRIExpression<String> summaryTitleExpr = new StringExpression(
				"riskname");
		DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer riskid = reportParameters.getFieldValue("id");
				return ProjectLinkBuilder.generateRiskPreviewFullLink(
						CurrentProjectVariables.getProjectId(), riskid);
			}
		};
		map.put("riskname", new HyperlinkValue(summaryTitleExpr,
				summaryHrefExpr));

		DRIExpression<String> assigneeTitleExpr = new StringExpression(
				"assignedToUserFullName");
		DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				String assignUser = reportParameters
						.getFieldValue("assigntouser");
				if (assignUser != null) {
					return AccountLinkGenerator.generatePreviewFullUserLink(
							AppContext.getSiteUrl(), assignUser);
				}

				return "";
			}
		};

		map.put("assignedToUserFullName", new HyperlinkValue(assigneeTitleExpr,
				assigneeHrefExpr));

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
		HorizontalListBuilder ratingBuilder = cmp.horizontalList()
				.setFixedWidth(120);
		ImageBuilder imgBuilder = cmp.image(ratingExpr).setFixedDimension(80,
				15);
		ratingBuilder.add(imgBuilder);
		map.put("level", new CompBuilderValue(ratingBuilder));

		map.put("datedue", new DateExpression("datedue"));

		return map;
	}

	private Map<String, MValue> buildProblemMap() {
		log.debug("Build report mapper for project::problem module");

		Map<String, MValue> map = new HashMap<String, MValue>();
		DRIExpression<String> summaryTitleExpr = new StringExpression(
				"issuename");
		DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer problemid = reportParameters.getFieldValue("id");
				return ProjectLinkBuilder.generateProblemPreviewFullLink(
						CurrentProjectVariables.getProjectId(), problemid);
			}
		};
		map.put("issuename", new HyperlinkValue(summaryTitleExpr,
				summaryHrefExpr));

		DRIExpression<String> assigneeTitleExpr = new StringExpression(
				"assignedUserFullName");
		DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				String assignUser = reportParameters
						.getFieldValue("assigntouser");
				if (assignUser != null) {
					return AccountLinkGenerator.generatePreviewFullUserLink(
							AppContext.getSiteUrl(), assignUser);
				}

				return "";
			}
		};

		map.put("assignedUserFullName", new HyperlinkValue(assigneeTitleExpr,
				assigneeHrefExpr));

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
		HorizontalListBuilder ratingBuilder = cmp.horizontalList()
				.setFixedWidth(120);
		ImageBuilder imgBuilder = cmp.image(ratingExpr).setFixedDimension(80,
				15);
		ratingBuilder.add(imgBuilder);
		map.put("level", new CompBuilderValue(ratingBuilder));

		map.put("datedue", new DateExpression("datedue"));

		return map;
	}

	private Map<String, MValue> buildRoleMap() {
		log.debug("Build report mapper for project::role module");

		Map<String, MValue> map = new HashMap<String, MValue>();
		DRIExpression<String> summaryTitleExpr = new StringExpression(
				"rolename");
		DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer roleid = reportParameters.getFieldValue("id");
				return ProjectLinkBuilder.generateRolePreviewFullLink(
						CurrentProjectVariables.getProjectId(), roleid);
			}
		};
		map.put("rolename", new HyperlinkValue(summaryTitleExpr,
				summaryHrefExpr));
		return map;
	}

	private Map<String, MValue> buildTimeTrackingMap() {
		log.debug("Build report mapper for project::timetracking module");

		Map<String, MValue> map = new HashMap<String, MValue>();
		DRIExpression<String> logUserTitleExpr = new StringExpression(
				"logUserFullName");
		DRIExpression<String> logUserHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				String assignUser = reportParameters.getFieldValue("loguser");
				if (assignUser != null) {
					return AccountLinkGenerator.generatePreviewFullUserLink(
							AppContext.getSiteUrl(), assignUser);
				}

				return "";
			}
		};

		map.put("logUserFullName", new HyperlinkValue(logUserTitleExpr,
				logUserHrefExpr));

		DRIExpression<String> projectTitleExpr = new StringExpression(
				"projectName");
		DRIExpression<String> projectHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer projectid = reportParameters.getFieldValue("projectid");
				if (projectid != null) {
					return ProjectLinkBuilder
							.generateProjectFullLink(projectid);
				}

				return "";
			}
		};

		map.put("projectName", new HyperlinkValue(projectTitleExpr,
				projectHrefExpr));

		map.put("logforday", new DateExpression("logforday"));

		AbstractSimpleExpression<String> billingExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters param) {
				Boolean level = param.getFieldValue("isbillable");
				if (level != null && level == Boolean.TRUE) {
					return "images/yes.png";
				} else {
					return "images/no.png";
				}
			}
		};
		HorizontalListBuilder ratingBuilder = cmp.horizontalList()
				.setFixedWidth(120);
		ImageBuilder imgBuilder = cmp.image(billingExpr).setFixedDimension(80,
				15);
		ratingBuilder.add(imgBuilder);
		map.put("isbillable", new CompBuilderValue(ratingBuilder));

		DRIExpression<String> summaryTitleExpr = new StringExpression("summary");
		DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				String type = reportParameters.getFieldValue("type");
				Integer typeid = reportParameters.getFieldValue("typeid");

				if (type == null) {
					return "";
				} else if (type.equals(ProjectTypeConstants.BUG)) {
					return ProjectLinkBuilder.generateBugPreviewFullLink(
							CurrentProjectVariables.getProjectId(), typeid);
				} else if (type.equals(ProjectTypeConstants.TASK)) {
					return ProjectLinkBuilder.generateTaskPreviewFullLink(
							CurrentProjectVariables.getProjectId(), typeid);
				} else if (type.equals(ProjectTypeConstants.PROBLEM)) {
					return ProjectLinkBuilder.generateProblemPreviewFullLink(
							CurrentProjectVariables.getProjectId(), typeid);
				} else if (type.equals(ProjectTypeConstants.RISK)) {
					return ProjectLinkBuilder.generateRiskPreviewFullLink(
							CurrentProjectVariables.getProjectId(), typeid);
				}
				return type;
			}
		};
		map.put("summary",
				new HyperlinkValue(summaryTitleExpr, summaryHrefExpr));

		return map;
	}

	private Map<String, MValue> buildTFollowingTicketMap() {
		log.debug("Build report mapper for project::following ticket module");

		Map<String, MValue> map = new HashMap<String, MValue>();
		DRIExpression<String> summaryTitleExpr = new StringExpression("summary");
		DRIExpression<String> summaryHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				String type = reportParameters.getFieldValue("type");
				Integer typeid = reportParameters.getFieldValue("typeId");

				if (type == null) {
					return "";
				} else if (type.equals(ProjectTypeConstants.BUG)) {
					return ProjectLinkBuilder.generateBugPreviewFullLink(
							CurrentProjectVariables.getProjectId(), typeid);
				} else if (type.equals(ProjectTypeConstants.TASK)) {
					return ProjectLinkBuilder.generateTaskPreviewFullLink(
							CurrentProjectVariables.getProjectId(), typeid);
				}
				return type;
			}
		};
		map.put("summary",
				new HyperlinkValue(summaryTitleExpr, summaryHrefExpr));

		DRIExpression<String> projectTitleExpr = new StringExpression(
				"projectName");
		DRIExpression<String> projectHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer projectid = reportParameters.getFieldValue("projectId");
				if (projectid != null) {
					return ProjectLinkBuilder
							.generateProjectFullLink(projectid);
				}

				return "";
			}
		};

		map.put("projectName", new HyperlinkValue(projectTitleExpr,
				projectHrefExpr));

		DRIExpression<String> logUserTitleExpr = new StringExpression(
				"assignUserFullName");
		DRIExpression<String> logUserHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				String assignUser = reportParameters
						.getFieldValue("assignUser");
				if (assignUser != null) {
					return AccountLinkGenerator.generatePreviewFullUserLink(
							AppContext.getSiteUrl(), assignUser);
				}

				return "";
			}
		};

		map.put("assignUser", new HyperlinkValue(logUserTitleExpr,
				logUserHrefExpr));

		map.put("monitorDate", new DateExpression("monitorDate"));

		return map;
	}
}
