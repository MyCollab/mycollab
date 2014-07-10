package com.esofthead.mycollab.reporting.expression;

import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.1
 *
 */
public class HyperlinkValue implements MValue {
	private DRIExpression title;
	private DRIExpression href;

	private StyleBuilder style;

	public HyperlinkValue(DRIExpression title, DRIExpression href) {
		this.title = title;
		this.href = href;
	}

	public DRIExpression getTitle() {
		return title;
	}

	public DRIExpression getHref() {
		return href;
	}

	public StyleBuilder getStyle() {
		return style;
	}

	public HyperlinkValue setStyle(StyleBuilder style) {
		this.style = style;
		return this;
	}
}
