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
package com.esofthead.mycollab.reporting;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.hyperLink;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.ImageBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;

import com.esofthead.mycollab.common.domain.Currency;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.project.BugPriorityStatusConstants;
import com.esofthead.mycollab.module.project.TaskPriorityStatusContants;
import com.esofthead.mycollab.module.project.domain.SimpleProblem;
import com.esofthead.mycollab.module.project.domain.SimpleRisk;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.tracker.BugStatusConstants;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.user.domain.BillingAccount;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.project.ProjectMailLinkGenerator;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("unchecked")
public class SimpleColumnComponentBuilderMap {
	private static Map<Class, List<? extends ColumnFieldComponentBuilder>> mapInjection = new HashMap<Class, List<? extends ColumnFieldComponentBuilder>>();

	public static class TypeRender {
		public static final String HYPERLINK = "hyperLink";
		public static final String EMAILTYPE = "emailType";
		public static final String DATE = "date";
		public static final String RATING = "rating";
		public static final String ASSIGNEE = "assignee";
		public static final String PERCENT = "percent";
		public static final String CURRENCY = "currency";
		public static final String ACOUNT_LINK = "account_link";
	}

	public static class ProjectMoulde {
		public static final String BUG = "bug";
		public static final String TASKLIST = "taskList";
		public static final String RISK = "risk";
		public static final String PROBLEM = "problem";
	}

	static {
		mapInjection.put(SimpleAccount.class, Arrays.asList(
				new CrmFieldComponentBuilder("accountname",
						CrmTypeConstants.ACCOUNT, TypeRender.HYPERLINK),
				new CrmFieldComponentBuilder("email", CrmTypeConstants.ACCOUNT,
						TypeRender.EMAILTYPE), new CrmFieldComponentBuilder(
						"assignUserFullName", CrmTypeConstants.ACCOUNT,
						TypeRender.ASSIGNEE)));

		mapInjection.put(SimpleContact.class, Arrays.asList(
				new CrmFieldComponentBuilder("contactName",
						CrmTypeConstants.CONTACT, TypeRender.HYPERLINK),
				new CrmFieldComponentBuilder("email", CrmTypeConstants.CONTACT,
						TypeRender.EMAILTYPE), new CrmFieldComponentBuilder(
						"assignUserFullName", CrmTypeConstants.CONTACT,
						TypeRender.ASSIGNEE), new CrmFieldComponentBuilder(
						"accountName", CrmTypeConstants.CONTACT,
						TypeRender.ACOUNT_LINK)));

		mapInjection.put(SimpleCampaign.class, Arrays.asList(
				new CrmFieldComponentBuilder("campaignname",
						CrmTypeConstants.CAMPAIGN, TypeRender.HYPERLINK),
				new CrmFieldComponentBuilder("assignUserFullName",
						CrmTypeConstants.CAMPAIGN, TypeRender.ASSIGNEE),
				new CrmFieldComponentBuilder("enddate",
						CrmTypeConstants.CAMPAIGN, TypeRender.DATE),
				new CrmFieldComponentBuilder("expectedrevenue",
						CrmTypeConstants.CAMPAIGN, TypeRender.CURRENCY)));

		mapInjection.put(SimpleLead.class, Arrays.asList(
				new CrmFieldComponentBuilder("leadName", CrmTypeConstants.LEAD,
						TypeRender.HYPERLINK), new CrmFieldComponentBuilder(
						"assignUserFullName", CrmTypeConstants.LEAD,
						TypeRender.ASSIGNEE)));

		mapInjection.put(SimpleOpportunity.class, Arrays.asList(
				new CrmFieldComponentBuilder("opportunityname",
						CrmTypeConstants.OPPORTUNITY, TypeRender.HYPERLINK),
				new CrmFieldComponentBuilder("assignUserFullName",
						CrmTypeConstants.OPPORTUNITY, TypeRender.ASSIGNEE),
				new CrmFieldComponentBuilder("amount",
						CrmTypeConstants.OPPORTUNITY, TypeRender.CURRENCY),
				new CrmFieldComponentBuilder("expectedcloseddate",
						CrmTypeConstants.OPPORTUNITY, TypeRender.DATE),
				new CrmFieldComponentBuilder("accountName",
						CrmTypeConstants.OPPORTUNITY, TypeRender.ACOUNT_LINK)));

		mapInjection.put(SimpleCase.class, Arrays.asList(
				new CrmFieldComponentBuilder("subject", CrmTypeConstants.CASE,
						TypeRender.HYPERLINK), new CrmFieldComponentBuilder(
						"assignUserFullName", CrmTypeConstants.CASE,
						TypeRender.ASSIGNEE), new CrmFieldComponentBuilder(
						"createdtime", CrmTypeConstants.CASE, TypeRender.DATE),
				new CrmFieldComponentBuilder("accountName",
						CrmTypeConstants.CASE, TypeRender.ACOUNT_LINK)));

		mapInjection.put(SimpleBug.class, Arrays.asList(
				new ProjectFieldBuilderFactory("summary", ProjectMoulde.BUG,
						TypeRender.HYPERLINK), new ProjectFieldBuilderFactory(
						"assignuserFullName", ProjectMoulde.BUG,
						TypeRender.ASSIGNEE), new ProjectFieldBuilderFactory(
						"duedate", ProjectMoulde.BUG, TypeRender.DATE)));

		mapInjection.put(SimpleRisk.class, Arrays.asList(
				new ProjectFieldBuilderFactory("riskname", ProjectMoulde.RISK,
						TypeRender.HYPERLINK), new RatingComponentBuilder(
						"level", ProjectMoulde.RISK),
				new ProjectFieldBuilderFactory("assignedToUserFullName",
						ProjectMoulde.RISK, TypeRender.ASSIGNEE),
				new ProjectFieldBuilderFactory("datedue", ProjectMoulde.RISK,
						TypeRender.DATE)));

		mapInjection.put(SimpleProblem.class, Arrays.asList(
				new ProjectFieldBuilderFactory("issuename",
						ProjectMoulde.PROBLEM, TypeRender.HYPERLINK),
				new RatingComponentBuilder("level", ProjectMoulde.PROBLEM),
				new ProjectFieldBuilderFactory("assignuserFullName",
						ProjectMoulde.PROBLEM, TypeRender.ASSIGNEE),
				new ProjectFieldBuilderFactory("datedue",
						ProjectMoulde.PROBLEM, TypeRender.DATE)));

		mapInjection.put(Task.class, Arrays.asList(
				new ProjectFieldBuilderFactory("taskname",
						ProjectMoulde.TASKLIST, TypeRender.HYPERLINK),
				new ProjectFieldBuilderFactory("percentagecomplete",
						ProjectMoulde.TASKLIST, TypeRender.PERCENT),
				new ProjectFieldBuilderFactory("assignuser",
						ProjectMoulde.TASKLIST, TypeRender.ASSIGNEE),
				new ProjectFieldBuilderFactory("startdate",
						ProjectMoulde.TASKLIST, TypeRender.DATE),
				new ProjectFieldBuilderFactory("deadline",
						ProjectMoulde.TASKLIST, TypeRender.DATE)));

	}

	public static String getSiteUrl(int accountId) {
		String siteUrl = "";
		if (SiteConfiguration.getDeploymentMode() == DeploymentMode.SITE) {
			BillingAccountService billingAccountService = ApplicationContextUtil
					.getSpringBean(BillingAccountService.class);
			BillingAccount account = billingAccountService
					.getAccountById(accountId);
			if (account != null) {
				siteUrl = SiteConfiguration.getSiteUrl(account.getSubdomain());
			}
		} else {
			siteUrl = SiteConfiguration.getSiteUrl("");
		}
		return siteUrl;

	}

	public static List<? extends ColumnFieldComponentBuilder> getListFieldBuilder(
			Class cls) {
		return mapInjection.get(cls);
	}

	/**
	 * ------------------------------------------------------------------------
	 * --------- CRM Field Factory ----------------------------
	 * ------------------------------------------
	 * ----------------------------------------
	 */
	public static class CrmFieldComponentBuilder implements
			ColumnFieldComponentBuilder {

		private String field;
		private String classType;
		private String typeRender;

		public CrmFieldComponentBuilder(String field, String classType,
				String typeRender) {
			this.field = field;
			this.classType = classType;
			this.typeRender = typeRender;
		}

		private class CrmFieldComponetBuilderExpression extends
				AbstractSimpleExpression<String> {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer id = reportParameters.getFieldValue("id");
				Integer sAccountId = reportParameters
						.getFieldValue("saccountid");
				String fieldName = reportParameters.getFieldValue(field)
						.toString();
				String assignUser = "";
				Currency currency = null;
				try {
					assignUser = reportParameters.getFieldValue("assignuser");
					currency = reportParameters.getFieldValue("currency");
				} catch (Exception e) {
				}

				String hyperLinkStr = fieldName;
				if (typeRender.equals(TypeRender.ACOUNT_LINK)) {
					Integer accountid = reportParameters
							.getFieldValue("accountid");
					hyperLinkStr = getSiteUrl(sAccountId)
							+ CrmLinkGenerator.generateCrmItemLink(
									CrmTypeConstants.ACCOUNT, accountid);
				} else if (typeRender.equals(TypeRender.CURRENCY)) {
					return fieldName
							+ ((currency != null) ? currency.getSymbol() : "");
				} else if (typeRender.equals(TypeRender.ASSIGNEE)) {
					hyperLinkStr = "mailto:" + assignUser;
				} else if (typeRender.equals(TypeRender.HYPERLINK)) {
					hyperLinkStr = getSiteUrl(sAccountId)
							+ CrmLinkGenerator.generateCrmItemLink(classType,
									id);
				} else if (typeRender.equals(TypeRender.EMAILTYPE)) {
					hyperLinkStr = "mailto:" + fieldName;
				}
				return hyperLinkStr;
			}
		}

		@Override
		public String getFieldName() {
			return field;
		}

		@Override
		public DRIExpression getDriExpression() {
			return new CrmFieldComponetBuilderExpression();
		}

		@Override
		public ComponentBuilder getComponentBuilder() {
			HorizontalListBuilder componentBuilder = cmp.horizontalList();
			TextFieldBuilder textBuilder = null;
			if (classType.equals(CrmTypeConstants.OPPORTUNITY)
					&& field.equals("opportunityname")) {

				ConditionalStyleBuilder overDueStyle = stl.conditionalStyle(
						new IsOpportunityOverDue()).setForegroundColor(
						Color.RED);
				ConditionalStyleBuilder isCompleteStyle = stl.conditionalStyle(
						new IsOpportunityComplete()).setStrikeThrough(true);

				StyleBuilder styleBuilder = stl.style(Templates.underlineStyle)
						.addConditionalStyle(overDueStyle)
						.addConditionalStyle(isCompleteStyle);
				textBuilder = cmp
						.text(new StringFieldUtilExpression(field, null))
						.setHyperLink(
								hyperLink(new CrmFieldComponetBuilderExpression()))
						.setStyle(styleBuilder);
			} else if (typeRender.equals(TypeRender.ACOUNT_LINK)) {
				textBuilder = cmp
						.text(new StringFieldUtilExpression(field, null))
						.setHyperLink(
								hyperLink(new CrmFieldComponetBuilderExpression()))
						.setStyle(Templates.underlineStyle);
			} else if (typeRender.equals(TypeRender.DATE)) {
				textBuilder = cmp.text(new StringFieldUtilExpression(field,
						typeRender));
			} else if (typeRender.equals(TypeRender.CURRENCY)) {
				textBuilder = cmp.text(new CrmFieldComponetBuilderExpression());
			} else {
				textBuilder = cmp
						.text(new StringFieldUtilExpression(field, null))
						.setHyperLink(hyperLink(this.getDriExpression()))
						.setStyle(Templates.underlineStyle);
			}
			componentBuilder.add(textBuilder);
			return componentBuilder;
		}
	}

	public static class IsOpportunityComplete extends
			AbstractSimpleExpression<Boolean> {
		private static final long serialVersionUID = 1L;

		@Override
		public Boolean evaluate(ReportParameters reportParameters) {
			String saleStage = reportParameters.getFieldValue("salesstage");
			if ("Closed Won".equals(saleStage)
					|| "Closed Lost".equals(saleStage)) {
				return true;
			}
			return false;
		}
	}

	public static class IsOpportunityOverDue extends
			AbstractSimpleExpression<Boolean> {
		private static final long serialVersionUID = 1L;

		@Override
		public Boolean evaluate(ReportParameters reportParameters) {
			Date expectedCloseDate = reportParameters
					.getFieldValue("expectedcloseddate");
			if (expectedCloseDate != null
					&& (expectedCloseDate.before(new GregorianCalendar()
							.getTime()))) {
				String saleStage = reportParameters.getFieldValue("salesstage");
				if ("Closed Won".equals(saleStage)
						|| "Closed Lost".equals(saleStage)) {
					return false;
				}
				return true;
			}
			return false;
		}
	}

	/**
	 * ------------------------------------------------------------------------
	 * --------- Project Field Factory ----------------------------
	 * ------------------------------------------
	 * ----------------------------------------
	 */

	private static class ProjectFieldBuilderFactory implements
			ColumnFieldComponentBuilder {
		private static final long serialVersionUID = 1L;

		private String field;
		private String typeRender;
		private String projectModule;

		public ProjectFieldBuilderFactory(String field, String projectModule,
				String typeRender) {
			this.field = field;
			this.typeRender = typeRender;
			this.projectModule = projectModule;
		}

		@Override
		public String getFieldName() {
			return field;
		}

		@Override
		public DRIExpression getDriExpression() {
			return null;
		}

		@Override
		public ComponentBuilder getComponentBuilder() {
			HorizontalListBuilder lstBuilder = cmp.horizontalList();
			if (projectModule.equals(ProjectMoulde.TASKLIST)
					&& typeRender.equals(TypeRender.PERCENT)) {
				lstBuilder.add(cmp.text(new StringFieldUtilExpression(field,
						null)));
				return lstBuilder;
			}
			if (typeRender.equals(TypeRender.DATE)) {
				return cmp.text(new StringFieldUtilExpression(field,
						TypeRender.DATE));
			}
			if (typeRender.equals(TypeRender.ASSIGNEE)) {
				if (projectModule.equals(ProjectMoulde.TASKLIST)) {
					lstBuilder.add(cmp
							.text(new StringFieldUtilExpression(field,
									TypeRender.ASSIGNEE))
							.setHyperLink(
									hyperLink(new StringFieldUtilExpression(
											field, TypeRender.EMAILTYPE)))
							.setStyle(Templates.underlineStyle));
				} else {
					if (!projectModule.equals(ProjectMoulde.BUG)) {
						lstBuilder
								.add(cmp.text(
										new StringFieldUtilExpression(field,
												null))
										.setHyperLink(
												hyperLink(new StringFieldUtilExpression(
														"assigntouser",
														TypeRender.EMAILTYPE)))
										.setStyle(Templates.underlineStyle));
					} else {
						lstBuilder
								.add(cmp.text(
										new StringFieldUtilExpression(field,
												null))
										.setHyperLink(
												hyperLink(new StringFieldUtilExpression(
														"assignuser",
														TypeRender.EMAILTYPE)))
										.setStyle(Templates.underlineStyle));
					}
				}
				return lstBuilder;
			}
			if (typeRender.equals(TypeRender.EMAILTYPE)) {
				lstBuilder.add(cmp
						.text(new StringFieldUtilExpression(field, null))
						.setHyperLink(
								hyperLink(new StringFieldUtilExpression(field,
										TypeRender.EMAILTYPE)))
						.setStyle(Templates.underlineStyle));
				return lstBuilder;
			}
			if (projectModule.equals(ProjectMoulde.BUG)
					|| projectModule.equals(ProjectMoulde.TASKLIST)) {
				if (typeRender.equals(TypeRender.HYPERLINK)) {
					lstBuilder.add(cmp.image(new ImagePriorityExpression())
							.setFixedDimension(12, 12));
				}
			}
			ConditionalStyleBuilder overDueStyle = stl.conditionalStyle(
					new OverDueExpression(projectModule)).setForegroundColor(
					Color.RED);
			ConditionalStyleBuilder isCompleteStyle = stl.conditionalStyle(
					new IsCompleteExpression(projectModule)).setStrikeThrough(
					true);

			StyleBuilder styleBuilder = stl.style(Templates.underlineStyle)
					.addConditionalStyle(overDueStyle)
					.addConditionalStyle(isCompleteStyle);

			lstBuilder.add(cmp
					.text(new StringFieldUtilExpression(field, null))
					.setHyperLink(
							hyperLink(new ProjectHyperLinkExpression(
									projectModule))).setStyle(styleBuilder));
			return lstBuilder;
		}

		private class OverDueExpression extends
				AbstractSimpleExpression<Boolean> {
			private static final long serialVersionUID = 1L;
			private String projectModule;

			public OverDueExpression(String projectModule) {
				this.projectModule = projectModule;
			}

			@Override
			public Boolean evaluate(ReportParameters param) {
				if (projectModule.equals(ProjectMoulde.BUG)) {
					String status = param.getFieldValue("status");
					Date duedate = param.getFieldValue("duedate");
					if (BugStatusConstants.RESOLVED.equals(status)
							|| BugStatusConstants.VERIFIED.equals(status)) {
						return false;
					}
					if (duedate != null) {
						Calendar today = Calendar.getInstance();
						today.set(Calendar.HOUR_OF_DAY, 0);
						Date todayDate = today.getTime();

						return todayDate.after(duedate);
					} else {
						return false;
					}
				} else {
					Date datedue = null;
					if (projectModule.equals(ProjectMoulde.TASKLIST)) {
						datedue = param.getFieldValue("deadline");
					} else {
						param.getFieldValue("datedue");
					}
					if (datedue != null
							&& (datedue.before(new GregorianCalendar()
									.getTime()))) {
						return true;
					} else
						return false;
				}
			}

		}

		private class IsCompleteExpression extends
				AbstractSimpleExpression<Boolean> {
			private static final long serialVersionUID = 1L;

			private String projectModule;

			public IsCompleteExpression(String projectModule) {
				this.projectModule = projectModule;
			}

			@Override
			public Boolean evaluate(ReportParameters param) {
				String status = param.getFieldValue("status");
				if (projectModule.equals(ProjectMoulde.BUG)) {
					return BugStatusConstants.VERIFIED.equals(status);
				} else {
					return "Closed".equals(status);
				}
			}
		}

		private static class ProjectHyperLinkExpression extends
				AbstractSimpleExpression<String> {
			private static final long serialVersionUID = 1L;

			private String projectModule;

			public ProjectHyperLinkExpression(String projectModule) {
				this.projectModule = projectModule;
			}

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer id = reportParameters.getFieldValue("id");
				Integer projectId = reportParameters.getFieldValue("projectid");
				ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
						projectId);

				if (projectModule.equals(ProjectMoulde.BUG)) {
					return linkGenerator.generateBugPreviewFullLink(id);
				} else if (projectModule.equals(ProjectMoulde.RISK)) {
					return linkGenerator.generateRiskPreviewFullLink(id);
				} else if (projectModule.equals(ProjectMoulde.PROBLEM)) {
					return linkGenerator.generateProblemPreviewFullLink(id);
				} else if (projectModule.equals(ProjectMoulde.TASKLIST)) {
					return linkGenerator.generateTaskPreviewFullLink(id);
				}
				return "";
			}
		}

		private static class ImagePriorityExpression extends
				AbstractSimpleExpression<InputStream> {
			private static final long serialVersionUID = 1L;

			@Override
			public InputStream evaluate(ReportParameters reportParameters) {
				String priority = reportParameters.getFieldValue("priority");

				if (priority.equals(BugPriorityStatusConstants.MAJOR)
						|| priority
								.equals(TaskPriorityStatusContants.PRIORITY_MEDIUM)) {
					return Templates.class.getClassLoader()
							.getResourceAsStream("images/priority_medium.png");
				} else if (priority
						.equals(BugPriorityStatusConstants.MINOR)
						|| priority
								.equals(TaskPriorityStatusContants.PRIORITY_LOW)) {
					return Templates.class.getClassLoader()
							.getResourceAsStream("images/priority_low.png");
				} else if (priority
						.equals(BugPriorityStatusConstants.CRITICAL)
						|| priority
								.equals(TaskPriorityStatusContants.PRIORITY_HIGHT)) {
					return Templates.class.getClassLoader()
							.getResourceAsStream("images/priority_high.png");
				} else if (priority
						.equals(BugPriorityStatusConstants.BLOCKER)
						|| priority
								.equals(TaskPriorityStatusContants.PRIORITY_URGENT)) {
					return Templates.class.getClassLoader()
							.getResourceAsStream("images/priority_urgent.png");
				} else
					return null;
			}
		}
	}

	public static class RatingComponentBuilder implements
			ColumnFieldComponentBuilder {

		private String field;
		private String classType;

		public RatingComponentBuilder(String field, String classType) {
			this.field = field;
			this.classType = classType;
		}

		private class RatingComponentBuilderExpression extends
				AbstractSimpleExpression<String> {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters param) {
				Double level = param.getFieldValue(field);
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
		}

		@Override
		public String getFieldName() {
			return field;
		}

		@Override
		public DRIExpression getDriExpression() {
			return new RatingComponentBuilderExpression();
		}

		@Override
		public ComponentBuilder getComponentBuilder() {
			HorizontalListBuilder componentBuilder = cmp.horizontalList()
					.setFixedWidth(120);
			ImageBuilder imgBuilder = cmp.image(this.getDriExpression())
					.setFixedDimension(80, 15);
			componentBuilder.add(imgBuilder);
			return componentBuilder;
		}

	}

	/**
	 * ------------------------------------------------------------------------
	 * --------- Common Field Factory ----------------------------
	 * ------------------------------------------
	 * ----------------------------------------
	 */
	public static class StringFieldUtilExpression extends
			AbstractSimpleExpression<String> {

		private static final long serialVersionUID = 1L;

		private String field;
		private String typeRender;

		public StringFieldUtilExpression(String field, String typeRender) {
			this.field = field;
			this.typeRender = typeRender;
		}

		@Override
		public String evaluate(ReportParameters param) {
			if (typeRender == null)
				return param.getFieldValue(field).toString();
			if (typeRender.equals(TypeRender.DATE)) {
				Date date = param.getFieldValue(field);
				return AppContext.formatDate(date);
			}
			if (typeRender.equals(TypeRender.ASSIGNEE)) {
				String stringValue = param.getFieldValue(field).toString();
				UserService service = ApplicationContextUtil
						.getSpringBean(UserService.class);
				User user = service.findUserByUserName(stringValue);
				if (user != null) {
					return user.getFirstname()
							+ ((user.getMiddlename() != null) ? " "
									+ user.getMiddlename() : "") + " "
							+ user.getLastname();
				} else
					return stringValue;
			} else if (typeRender.equals(TypeRender.EMAILTYPE)) {
				String stringValue = param.getFieldValue(field).toString();
				return "mailto:" + stringValue;
			} else if (typeRender.equals(TypeRender.PERCENT)) {
				DecimalFormat df = new DecimalFormat("#");
				df.setRoundingMode(RoundingMode.HALF_EVEN);
				Double percentValue = param.getValue(field);
				return df.format(percentValue) + "%";
			} else
				return "";
		}
	}
}
