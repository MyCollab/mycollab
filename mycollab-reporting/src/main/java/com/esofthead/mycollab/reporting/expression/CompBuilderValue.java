package com.esofthead.mycollab.reporting.expression;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.1
 *
 */
public class CompBuilderValue implements MValue {

	private ComponentBuilder compBuilder;

	public CompBuilderValue(ComponentBuilder compBuilder) {
		this.compBuilder = compBuilder;
	}

	public ComponentBuilder getCompBuilder() {
		return compBuilder;
	}
}
