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

import com.mycollab.module.crm.CrmLinkGenerator
import com.mycollab.module.crm.domain.*
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.reporting.ColumnBuilderClassMapper
import com.mycollab.reporting.ReportStyles
import com.mycollab.reporting.expression.DateExpression
import com.mycollab.reporting.expression.DateTimeExpression
import com.mycollab.reporting.expression.MailExpression
import com.mycollab.reporting.expression.PrimaryTypeFieldExpression
import com.mycollab.reporting.generator.ComponentBuilderGenerator
import com.mycollab.reporting.generator.HyperlinkBuilderGenerator
import com.mycollab.reporting.generator.SimpleExpressionBuilderGenerator
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression
import net.sf.dynamicreports.report.builder.DynamicReports
import net.sf.dynamicreports.report.definition.ReportParameters
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.awt.Color
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
@Component
class CrmColumnBuilderMapper : InitializingBean {

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        ColumnBuilderClassMapper.put(SimpleAccount::class.java, buildAccountMap())
        ColumnBuilderClassMapper.put(SimpleContact::class.java, buildContactMap())
        ColumnBuilderClassMapper.put(SimpleCampaign::class.java, buildCampaignMap())
        ColumnBuilderClassMapper.put(SimpleLead::class.java, buildLeadMap())
        ColumnBuilderClassMapper.put(SimpleOpportunity::class.java, buildOpportunityMap())
        ColumnBuilderClassMapper.put(SimpleCase::class.java, buildCaseMap())
    }

    private fun buildAccountMap(): Map<String, ComponentBuilderGenerator> {
        LOG.debug("Build report mapper for crm::account module")

        val map = HashMap<String, ComponentBuilderGenerator>()
        val assigneeTitleExpr = PrimaryTypeFieldExpression<String>(SimpleAccount.Field.assignUserFullName.name)
        val assigneeHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val assignUser = reportParameters.getFieldValue<String>("assignuser")
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                return if (assignUser != null) {
                    AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, assignUser)
                } else ""

            }
        }

        map.put(SimpleAccount.Field.assignUserFullName.name, HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr))

        val accountTitleExpr = PrimaryTypeFieldExpression<String>(Account.Field.accountname.name)
        val accountHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val accountId = reportParameters.getFieldValue<Int>("id")
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                return CrmLinkGenerator.generateAccountPreviewFullLink(siteUrl, accountId)
            }
        }
        map.put(Account.Field.accountname.name, HyperlinkBuilderGenerator(accountTitleExpr, accountHrefExpr))
        return map
    }

    private fun buildContactMap(): Map<String, ComponentBuilderGenerator> {
        LOG.debug("Build report mapper for crm::contact module")
        val map = HashMap<String, ComponentBuilderGenerator>()

        val accountTitleExpr = PrimaryTypeFieldExpression<String>("accountName")
        val accountHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val accountId = reportParameters.getFieldValue<Int>("accountid")
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                return CrmLinkGenerator.generateAccountPreviewFullLink(siteUrl, accountId)
            }
        }
        map.put("accountName", HyperlinkBuilderGenerator(accountTitleExpr, accountHrefExpr))

        val contactTitleExpr = PrimaryTypeFieldExpression<String>("contactName")
        val contactHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val contactid = reportParameters.getFieldValue<Int>("id")
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                return CrmLinkGenerator.generateContactPreviewFullLink(siteUrl, contactid)
            }
        }
        map.put("contactName", HyperlinkBuilderGenerator(contactTitleExpr, contactHrefExpr))

        val assigneeTitleExpr = PrimaryTypeFieldExpression<String>("assignUserFullName")
        val assigneeHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val assignUser = reportParameters.getFieldValue<String>("assignuser")
                return when {
                    assignUser != null -> {
                        val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                        AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, assignUser)
                    }
                    else -> ""
                }

            }
        }

        map.put("assignUserFullName", HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr))
        return map
    }

    private fun buildCampaignMap(): Map<String, ComponentBuilderGenerator> {
        LOG.debug("Build report mapper for crm::campaign module")
        val map = HashMap<String, ComponentBuilderGenerator>()
        val assigneeTitleExpr = PrimaryTypeFieldExpression<String>("assignUserFullName")
        val assigneeHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val assignUser = reportParameters.getFieldValue<String>("assignuser")
                return when {
                    assignUser != null -> {
                        val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                        AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, assignUser)
                    }
                    else -> ""
                }

            }
        }

        map.put("assignUserFullName", HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr))

        val campaignTitleExpr = PrimaryTypeFieldExpression<String>("campaignname")
        val campaignHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val campaignId = reportParameters.getFieldValue<Int>("id")
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                return CrmLinkGenerator.generateCampaignPreviewFullLink(siteUrl, campaignId)
            }
        }
        map.put("campaignname", HyperlinkBuilderGenerator(campaignTitleExpr, campaignHrefExpr))
        map.put("enddate", SimpleExpressionBuilderGenerator(DateExpression("enddate")))
        return map
    }

    private fun buildLeadMap(): Map<String, ComponentBuilderGenerator> {
        LOG.debug("Build report mapper for crm::lead module")
        val map = HashMap<String, ComponentBuilderGenerator>()
        val assigneeTitleExpr = PrimaryTypeFieldExpression<String>("assignUserFullName")
        val assigneeHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val assignUser = reportParameters.getFieldValue<String>("assignuser")
                return when {
                    assignUser != null -> {
                        val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                        AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, assignUser)
                    }
                    else -> ""
                }

            }
        }

        map.put("assignUserFullName", HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr))

        val leadTitleExpr = PrimaryTypeFieldExpression<String>("leadName")
        val leadHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val leadId = reportParameters.getFieldValue<Int>("id")
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                return CrmLinkGenerator.generateLeadPreviewFullLink(siteUrl, leadId)
            }
        }
        map.put("leadName", HyperlinkBuilderGenerator(leadTitleExpr, leadHrefExpr))
        map.put("email", SimpleExpressionBuilderGenerator(MailExpression("email")))
        return map
    }

    private fun buildOpportunityMap(): Map<String, ComponentBuilderGenerator> {
        LOG.debug("Build report mapper for crm::opportunity module")
        val map = HashMap<String, ComponentBuilderGenerator>()
        val assigneeTitleExpr = PrimaryTypeFieldExpression<String>("assignUserFullName")
        val assigneeHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val assignUser = reportParameters.getFieldValue<String>("assignuser")
                return when {
                    assignUser != null -> {
                        val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                        AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, assignUser)
                    }
                    else -> ""
                }

            }
        }

        map.put("assignUserFullName", HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr))

        val opportunityTitleExpr = PrimaryTypeFieldExpression<String>("opportunityname")
        val opportunityHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val opportunityId = reportParameters.getFieldValue<Int>("id")
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                return CrmLinkGenerator.generateOpportunityPreviewFullLink(siteUrl, opportunityId)
            }
        }

        val overDueExpr = object : AbstractSimpleExpression<Boolean>() {

            override fun evaluate(reportParameters: ReportParameters): Boolean? {
                val expectedCloseDate = reportParameters.getFieldValue<Date>("expectedcloseddate")
                if (expectedCloseDate != null && expectedCloseDate.before(GregorianCalendar().time)) {
                    val saleStage = reportParameters.getFieldValue<String>("salesstage")
                    return !("Closed Won" == saleStage || "Closed Lost" == saleStage)
                }
                return false
            }
        }

        val isCompleteExpr = object : AbstractSimpleExpression<Boolean>() {

            override fun evaluate(reportParameters: ReportParameters): Boolean {
                val saleStage = reportParameters.getFieldValue<String>("salesstage")
                return "Closed Won" == saleStage || "Closed Lost" == saleStage
            }
        }

        val overDueStyle = DynamicReports.stl.conditionalStyle(overDueExpr).setForegroundColor(Color.RED)
        val isCompleteStyle = DynamicReports.stl.conditionalStyle(isCompleteExpr).setStrikeThrough(true)

        val styleBuilder = DynamicReports.stl.style(ReportStyles.instance()
                .underlineStyle).addConditionalStyle(overDueStyle)
                .addConditionalStyle(isCompleteStyle)

        map.put("opportunityname", HyperlinkBuilderGenerator(opportunityTitleExpr, opportunityHrefExpr).setStyle(styleBuilder))
        map.put("expectedcloseddate", SimpleExpressionBuilderGenerator(DateExpression("expectedcloseddate")))
        return map
    }

    private fun buildCaseMap(): Map<String, ComponentBuilderGenerator> {
        LOG.debug("Build report mapper for crm::case module")
        val map = HashMap<String, ComponentBuilderGenerator>()
        val assigneeTitleExpr = PrimaryTypeFieldExpression<String>("assignUserFullName")
        val assigneeHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val assignUser = reportParameters.getFieldValue<String>("assignuser")
                if (assignUser != null) {
                    val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                    return AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, assignUser)
                }

                return ""
            }
        }

        map.put("assignUserFullName", HyperlinkBuilderGenerator(assigneeTitleExpr, assigneeHrefExpr))

        val caseTitleExpr = PrimaryTypeFieldExpression<String>("subject")
        val caseHrefExpr = object : AbstractSimpleExpression<String>() {

            override fun evaluate(reportParameters: ReportParameters): String {
                val caseId = reportParameters.getFieldValue<Int>("id")
                val siteUrl = reportParameters.getParameterValue<String>("siteUrl")
                return CrmLinkGenerator.generateCasePreviewFullLink(siteUrl, caseId)
            }
        }
        map.put("subject", HyperlinkBuilderGenerator(caseTitleExpr, caseHrefExpr))
        map.put("createdtime", SimpleExpressionBuilderGenerator(DateTimeExpression("createdtime")))
        return map
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CrmColumnBuilderMapper::class.java)
    }
}
