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
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
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
		ColumnBuilderClassMapper.put(SimpleContact.class, buildContactMap());
	}

	private Map<String, ComponentBuilder> buildContactMap() {
		Map<String, ComponentBuilder> map = new HashMap<String, ComponentBuilder>();

		DRIExpression titleExpr = new StringExpression("accountName");
		DRIExpression hrefExpr = new AbstractSimpleExpression() {

			@Override
			public Object evaluate(ReportParameters reportParameters) {
				Integer accountid = reportParameters.getFieldValue("accountid");
				return CrmLinkGenerator.generateAccountPreviewFullLink(
						AppContext.getSiteUrl(), accountid);
			}
		};
		map.put("accountName",
				ComponentBuilderWrapper.buildHyperLink(titleExpr, hrefExpr));
		return map;
	}
}
