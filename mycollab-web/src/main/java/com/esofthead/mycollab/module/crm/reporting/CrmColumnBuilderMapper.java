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
package com.esofthead.mycollab.module.crm.reporting;

import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.user.AccountLinkUtils;
import com.esofthead.mycollab.reporting.ColumnBuilderClassMapper;
import com.esofthead.mycollab.reporting.ComponentBuilderWrapper;
import com.esofthead.mycollab.reporting.DateExpression;
import com.esofthead.mycollab.reporting.DateTimeExpression;
import com.esofthead.mycollab.reporting.MailExpression;
import com.esofthead.mycollab.reporting.ReportTemplateFactory;
import com.esofthead.mycollab.reporting.StringExpression;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
@Component
public class CrmColumnBuilderMapper implements InitializingBean {
	private static Logger log = LoggerFactory
			.getLogger(CrmColumnBuilderMapper.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		ColumnBuilderClassMapper.put(SimpleAccount.class, buildAccountMap());
		ColumnBuilderClassMapper.put(SimpleContact.class, buildContactMap());
		ColumnBuilderClassMapper.put(SimpleCampaign.class, buildCampaignMap());
		ColumnBuilderClassMapper.put(SimpleLead.class, buildLeadMap());
		ColumnBuilderClassMapper.put(SimpleOpportunity.class,
				buildOpportunityMap());
		ColumnBuilderClassMapper.put(SimpleCase.class, buildCaseMap());
	}

	private Map<String, ComponentBuilder> buildAccountMap() {
		log.debug("Build report mapper for crm::account module");

		Map<String, ComponentBuilder> map = new HashMap<String, ComponentBuilder>();
		DRIExpression<String> assigneeTitleExpr = new StringExpression(
				"assignUserFullName");
		DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				String assignUser = reportParameters
						.getFieldValue("assignuser");
				if (assignUser != null) {
					return AccountLinkUtils.generatePreviewFullUserLink(
							AppContext.getSiteUrl(), assignUser);
				}

				return "";
			}
		};

		map.put("assignUserFullName", ComponentBuilderWrapper.buildHyperLink(
				assigneeTitleExpr, assigneeHrefExpr));

		DRIExpression<String> accountTitleExpr = new StringExpression(
				"accountname");
		DRIExpression<String> accountHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer accountid = reportParameters.getFieldValue("id");
				return CrmLinkGenerator.generateAccountPreviewFullLink(
						AppContext.getSiteUrl(), accountid);
			}
		};
		map.put("accountname", ComponentBuilderWrapper.buildHyperLink(
				accountTitleExpr, accountHrefExpr));
		return map;
	}

	private Map<String, ComponentBuilder> buildContactMap() {
		log.debug("Build report mapper for crm::contact module");
		Map<String, ComponentBuilder> map = new HashMap<String, ComponentBuilder>();

		DRIExpression<String> accountTitleExpr = new StringExpression(
				"accountName");
		DRIExpression<String> accountHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer accountid = reportParameters.getFieldValue("accountid");
				return CrmLinkGenerator.generateAccountPreviewFullLink(
						AppContext.getSiteUrl(), accountid);
			}
		};
		map.put("accountName", ComponentBuilderWrapper.buildHyperLink(
				accountTitleExpr, accountHrefExpr));

		DRIExpression<String> contactTitleExpr = new StringExpression(
				"contactName");
		DRIExpression<String> contactHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer contactid = reportParameters.getFieldValue("id");
				return CrmLinkGenerator.generateContactPreviewFullLink(
						AppContext.getSiteUrl(), contactid);
			}
		};
		map.put("contactName", ComponentBuilderWrapper.buildHyperLink(
				contactTitleExpr, contactHrefExpr));

		DRIExpression<String> assigneeTitleExpr = new StringExpression(
				"assignUserFullName");
		DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				String assignUser = reportParameters
						.getFieldValue("assignuser");
				if (assignUser != null) {
					return AccountLinkUtils.generatePreviewFullUserLink(
							AppContext.getSiteUrl(), assignUser);
				}

				return "";
			}
		};

		map.put("assignUserFullName", ComponentBuilderWrapper.buildHyperLink(
				assigneeTitleExpr, assigneeHrefExpr));
		return map;
	}

	private Map<String, ComponentBuilder> buildCampaignMap() {
		log.debug("Build report mapper for crm::campaign module");
		Map<String, ComponentBuilder> map = new HashMap<String, ComponentBuilder>();
		DRIExpression<String> assigneeTitleExpr = new StringExpression(
				"assignUserFullName");
		DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				String assignUser = reportParameters
						.getFieldValue("assignuser");
				if (assignUser != null) {
					return AccountLinkUtils.generatePreviewFullUserLink(
							AppContext.getSiteUrl(), assignUser);
				}

				return "";
			}
		};

		map.put("assignUserFullName", ComponentBuilderWrapper.buildHyperLink(
				assigneeTitleExpr, assigneeHrefExpr));

		DRIExpression<String> campaignTitleExpr = new StringExpression(
				"campaignname");
		DRIExpression<String> campaignHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer campaignid = reportParameters.getFieldValue("id");
				return CrmLinkGenerator.generateCampaignPreviewFullLink(
						AppContext.getSiteUrl(), campaignid);
			}
		};
		map.put("campaignname", ComponentBuilderWrapper.buildHyperLink(
				campaignTitleExpr, campaignHrefExpr));

		map.put("enddate", ComponentBuilderWrapper
				.buildDateText(new DateExpression("enddate")));
		return map;
	}

	private Map<String, ComponentBuilder> buildLeadMap() {
		log.debug("Build report mapper for crm::lead module");
		Map<String, ComponentBuilder> map = new HashMap<String, ComponentBuilder>();
		DRIExpression<String> assigneeTitleExpr = new StringExpression(
				"assignUserFullName");
		DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				String assignUser = reportParameters
						.getFieldValue("assignuser");
				if (assignUser != null) {
					return AccountLinkUtils.generatePreviewFullUserLink(
							AppContext.getSiteUrl(), assignUser);
				}

				return "";
			}
		};

		map.put("assignUserFullName", ComponentBuilderWrapper.buildHyperLink(
				assigneeTitleExpr, assigneeHrefExpr));

		DRIExpression<String> leadTitleExpr = new StringExpression("leadName");
		DRIExpression<String> leadHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer leadid = reportParameters.getFieldValue("id");
				return CrmLinkGenerator.generateLeadPreviewFullLink(
						AppContext.getSiteUrl(), leadid);
			}
		};
		map.put("leadName", ComponentBuilderWrapper.buildHyperLink(
				leadTitleExpr, leadHrefExpr));

		map.put("email",
				ComponentBuilderWrapper.buildEmail(new MailExpression("email")));
		return map;
	}

	private Map<String, ComponentBuilder> buildOpportunityMap() {
		log.debug("Build report mapper for crm::opportunity module");
		Map<String, ComponentBuilder> map = new HashMap<String, ComponentBuilder>();
		DRIExpression<String> assigneeTitleExpr = new StringExpression(
				"assignUserFullName");
		DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				String assignUser = reportParameters
						.getFieldValue("assignuser");
				if (assignUser != null) {
					return AccountLinkUtils.generatePreviewFullUserLink(
							AppContext.getSiteUrl(), assignUser);
				}

				return "";
			}
		};

		map.put("assignUserFullName", ComponentBuilderWrapper.buildHyperLink(
				assigneeTitleExpr, assigneeHrefExpr));

		DRIExpression<String> opportunityTitleExpr = new StringExpression(
				"opportunityname");
		DRIExpression<String> opportunityHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer opportunityid = reportParameters.getFieldValue("id");
				return CrmLinkGenerator.generateOpportunityPreviewFullLink(
						AppContext.getSiteUrl(), opportunityid);
			}
		};

		AbstractSimpleExpression<Boolean> overDueExpr = new AbstractSimpleExpression<Boolean>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean evaluate(ReportParameters reportParameters) {
				Date expectedCloseDate = reportParameters
						.getFieldValue("expectedcloseddate");
				if (expectedCloseDate != null
						&& (expectedCloseDate.before(new GregorianCalendar()
								.getTime()))) {
					String saleStage = reportParameters
							.getFieldValue("salesstage");
					if ("Closed Won".equals(saleStage)
							|| "Closed Lost".equals(saleStage)) {
						return false;
					}
					return true;
				}
				return false;
			}
		};

		AbstractSimpleExpression<Boolean> isCompleteExpr = new AbstractSimpleExpression<Boolean>() {
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
		};

		ConditionalStyleBuilder overDueStyle = stl
				.conditionalStyle(overDueExpr).setForegroundColor(Color.RED);
		ConditionalStyleBuilder isCompleteStyle = stl.conditionalStyle(
				isCompleteExpr).setStrikeThrough(true);

		StyleBuilder styleBuilder = stl
				.style(ReportTemplateFactory.getTemplate().getUnderlineStyle())
				.addConditionalStyle(overDueStyle)
				.addConditionalStyle(isCompleteStyle);

		map.put("opportunityname",
				ComponentBuilderWrapper.buildHyperLink(opportunityTitleExpr,
						opportunityHrefExpr).setStyle(styleBuilder));

		map.put("expectedcloseddate", ComponentBuilderWrapper
				.buildDateText(new DateExpression("expectedcloseddate")));
		return map;
	}

	private Map<String, ComponentBuilder> buildCaseMap() {
		log.debug("Build report mapper for crm::case module");
		Map<String, ComponentBuilder> map = new HashMap<String, ComponentBuilder>();
		DRIExpression<String> assigneeTitleExpr = new StringExpression(
				"assignUserFullName");
		DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				String assignUser = reportParameters
						.getFieldValue("assignuser");
				if (assignUser != null) {
					return AccountLinkUtils.generatePreviewFullUserLink(
							AppContext.getSiteUrl(), assignUser);
				}

				return "";
			}
		};

		map.put("assignUserFullName", ComponentBuilderWrapper.buildHyperLink(
				assigneeTitleExpr, assigneeHrefExpr));

		DRIExpression<String> caseTitleExpr = new StringExpression("subject");
		DRIExpression<String> caseHrefExpr = new AbstractSimpleExpression<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String evaluate(ReportParameters reportParameters) {
				Integer caseid = reportParameters.getFieldValue("id");
				return CrmLinkGenerator.generateCasePreviewFullLink(
						AppContext.getSiteUrl(), caseid);
			}
		};
		map.put("subject", ComponentBuilderWrapper.buildHyperLink(
				caseTitleExpr, caseHrefExpr));

		map.put("createdtime", ComponentBuilderWrapper
				.buildDateTimeText(new DateTimeExpression("createdtime")));
		return map;
	}
}
