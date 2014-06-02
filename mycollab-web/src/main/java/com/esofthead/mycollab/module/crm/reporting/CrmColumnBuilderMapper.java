package com.esofthead.mycollab.module.crm.reporting;

import java.util.HashMap;
import java.util.Map;

import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.user.AccountLinkUtils;
import com.esofthead.mycollab.reporting.ColumnBuilderClassMapper;
import com.esofthead.mycollab.reporting.ComponentBuilderWrapper;
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

	@Override
	public void afterPropertiesSet() throws Exception {
		ColumnBuilderClassMapper.put(SimpleAccount.class, buildAccountMap());
		ColumnBuilderClassMapper.put(SimpleContact.class, buildContactMap());
	}

	private Map<String, ComponentBuilder> buildAccountMap() {
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
		return map;
	}

	private Map<String, ComponentBuilder> buildContactMap() {
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
}
