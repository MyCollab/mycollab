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
package com.esofthead.mycollab.module.crm.reporting.spring;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.domain.*;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.reporting.ColumnBuilderClassMapper;
import com.esofthead.mycollab.reporting.ReportTemplateFactory;
import com.esofthead.mycollab.reporting.expression.*;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
@Component
public class CrmColumnBuilderMapper implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(CrmColumnBuilderMapper.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        ColumnBuilderClassMapper.put(SimpleAccount.class, buildAccountMap());
        ColumnBuilderClassMapper.put(SimpleContact.class, buildContactMap());
        ColumnBuilderClassMapper.put(SimpleCampaign.class, buildCampaignMap());
        ColumnBuilderClassMapper.put(SimpleLead.class, buildLeadMap());
        ColumnBuilderClassMapper.put(SimpleOpportunity.class, buildOpportunityMap());
        ColumnBuilderClassMapper.put(SimpleCase.class, buildCaseMap());
    }

    private Map<String, MValue> buildAccountMap() {
        LOG.debug("Build report mapper for crm::account module");

        Map<String, MValue> map = new HashMap<>();
        DRIExpression<String> assigneeTitleExpr = new PrimaryTypeFieldExpression(SimpleAccount.Field.assignUserFullName.name());
        DRIExpression<String> assigneeHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                String assignUser = reportParameters.getFieldValue("assignuser");
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                if (assignUser != null) {
                    return AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, assignUser);
                }

                return "";
            }
        };

        map.put(SimpleAccount.Field.assignUserFullName.name(), new HyperlinkValue(assigneeTitleExpr, assigneeHrefExpr));

        DRIExpression<String> accountTitleExpr = new PrimaryTypeFieldExpression(Account.Field.accountname.name());
        DRIExpression<String> accountHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer accountId = reportParameters.getFieldValue("id");
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return CrmLinkGenerator.generateAccountPreviewFullLink(siteUrl, accountId);
            }
        };
        map.put(Account.Field.accountname.name(), new HyperlinkValue(accountTitleExpr, accountHrefExpr));
        return map;
    }

    private Map<String, MValue> buildContactMap() {
        LOG.debug("Build report mapper for crm::contact module");
        Map<String, MValue> map = new HashMap<>();

        DRIExpression<String> accountTitleExpr = new PrimaryTypeFieldExpression("accountName");
        DRIExpression<String> accountHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer accountId = reportParameters.getFieldValue("accountid");
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return CrmLinkGenerator.generateAccountPreviewFullLink(siteUrl, accountId);
            }
        };
        map.put("accountName", new HyperlinkValue(accountTitleExpr, accountHrefExpr));

        DRIExpression<String> contactTitleExpr = new PrimaryTypeFieldExpression("contactName");
        DRIExpression<String> contactHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer contactid = reportParameters.getFieldValue("id");
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return CrmLinkGenerator.generateContactPreviewFullLink(siteUrl, contactid);
            }
        };
        map.put("contactName", new HyperlinkValue(contactTitleExpr, contactHrefExpr));

        DRIExpression<String> assigneeTitleExpr = new PrimaryTypeFieldExpression("assignUserFullName");
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

        map.put("assignUserFullName", new HyperlinkValue(assigneeTitleExpr, assigneeHrefExpr));
        return map;
    }

    private Map<String, MValue> buildCampaignMap() {
        LOG.debug("Build report mapper for crm::campaign module");
        Map<String, MValue> map = new HashMap<>();
        DRIExpression<String> assigneeTitleExpr = new PrimaryTypeFieldExpression<>("assignUserFullName");
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

        map.put("assignUserFullName", new HyperlinkValue(assigneeTitleExpr, assigneeHrefExpr));

        DRIExpression<String> campaignTitleExpr = new PrimaryTypeFieldExpression("campaignname");
        DRIExpression<String> campaignHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer campaignId = reportParameters.getFieldValue("id");
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return CrmLinkGenerator.generateCampaignPreviewFullLink(siteUrl, campaignId);
            }
        };
        map.put("campaignname", new HyperlinkValue(campaignTitleExpr, campaignHrefExpr));
        map.put("enddate", new DateExpression("enddate"));
        return map;
    }

    private Map<String, MValue> buildLeadMap() {
        LOG.debug("Build report mapper for crm::lead module");
        Map<String, MValue> map = new HashMap<>();
        DRIExpression<String> assigneeTitleExpr = new PrimaryTypeFieldExpression("assignUserFullName");
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

        map.put("assignUserFullName", new HyperlinkValue(assigneeTitleExpr, assigneeHrefExpr));

        DRIExpression<String> leadTitleExpr = new PrimaryTypeFieldExpression("leadName");
        DRIExpression<String> leadHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer leadId = reportParameters.getFieldValue("id");
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return CrmLinkGenerator.generateLeadPreviewFullLink(siteUrl, leadId);
            }
        };
        map.put("leadName", new HyperlinkValue(leadTitleExpr, leadHrefExpr));
        map.put("email", new MailExpression("email"));
        return map;
    }

    private Map<String, MValue> buildOpportunityMap() {
        LOG.debug("Build report mapper for crm::opportunity module");
        Map<String, MValue> map = new HashMap<>();
        DRIExpression<String> assigneeTitleExpr = new PrimaryTypeFieldExpression("assignUserFullName");
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

        map.put("assignUserFullName", new HyperlinkValue(assigneeTitleExpr, assigneeHrefExpr));

        DRIExpression<String> opportunityTitleExpr = new PrimaryTypeFieldExpression("opportunityname");
        DRIExpression<String> opportunityHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer opportunityId = reportParameters.getFieldValue("id");
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return CrmLinkGenerator.generateOpportunityPreviewFullLink(siteUrl, opportunityId);
            }
        };

        AbstractSimpleExpression<Boolean> overDueExpr = new AbstractSimpleExpression<Boolean>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Boolean evaluate(ReportParameters reportParameters) {
                Date expectedCloseDate = reportParameters.getFieldValue("expectedcloseddate");
                if (expectedCloseDate != null && (expectedCloseDate.before(new GregorianCalendar().getTime()))) {
                    String saleStage = reportParameters.getFieldValue("salesstage");
                    return !("Closed Won".equals(saleStage) || "Closed Lost".equals(saleStage));
                }
                return false;
            }
        };

        AbstractSimpleExpression<Boolean> isCompleteExpr = new AbstractSimpleExpression<Boolean>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Boolean evaluate(ReportParameters reportParameters) {
                String saleStage = reportParameters.getFieldValue("salesstage");
                return "Closed Won".equals(saleStage) || "Closed Lost".equals(saleStage);
            }
        };

        ConditionalStyleBuilder overDueStyle = DynamicReports.stl.conditionalStyle(overDueExpr).setForegroundColor(Color.RED);
        ConditionalStyleBuilder isCompleteStyle = DynamicReports.stl.conditionalStyle(isCompleteExpr).setStrikeThrough(true);

        StyleBuilder styleBuilder = DynamicReports.stl.style(ReportTemplateFactory.getTemplate(
                SiteConfiguration.getDefaultLocale())
                .getUnderlineStyle()).addConditionalStyle(overDueStyle)
                .addConditionalStyle(isCompleteStyle);

        map.put("opportunityname", new HyperlinkValue(opportunityTitleExpr, opportunityHrefExpr).setStyle(styleBuilder));
        map.put("expectedcloseddate", new DateExpression("expectedcloseddate"));
        return map;
    }

    private Map<String, MValue> buildCaseMap() {
        LOG.debug("Build report mapper for crm::case module");
        Map<String, MValue> map = new HashMap<>();
        DRIExpression<String> assigneeTitleExpr = new PrimaryTypeFieldExpression("assignUserFullName");
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

        map.put("assignUserFullName", new HyperlinkValue(assigneeTitleExpr, assigneeHrefExpr));

        DRIExpression<String> caseTitleExpr = new PrimaryTypeFieldExpression("subject");
        DRIExpression<String> caseHrefExpr = new AbstractSimpleExpression<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String evaluate(ReportParameters reportParameters) {
                Integer caseId = reportParameters.getFieldValue("id");
                String siteUrl = reportParameters.getParameterValue("siteUrl");
                return CrmLinkGenerator.generateCasePreviewFullLink(siteUrl, caseId);
            }
        };
        map.put("subject", new HyperlinkValue(caseTitleExpr, caseHrefExpr));
        map.put("createdtime", new DateTimeExpression("createdtime"));
        return map;
    }
}
