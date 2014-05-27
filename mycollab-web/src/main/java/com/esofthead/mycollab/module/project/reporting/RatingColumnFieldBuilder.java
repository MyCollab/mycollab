package com.esofthead.mycollab.module.project.reporting;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.ImageBuilder;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.esofthead.mycollab.reporting.AbstractColumnFieldComponentBuilder;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public class RatingColumnFieldBuilder extends
		AbstractColumnFieldComponentBuilder {
	private static final long serialVersionUID = 1L;

	private String field;

	public RatingColumnFieldBuilder(String field) {
		super(field);
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
	public ComponentBuilder getComponentBuilder() {
		HorizontalListBuilder componentBuilder = cmp.horizontalList()
				.setFixedWidth(120);
		ImageBuilder imgBuilder = cmp.image(
				new RatingComponentBuilderExpression()).setFixedDimension(80,
				15);
		componentBuilder.add(imgBuilder);
		return componentBuilder;
	}

}
