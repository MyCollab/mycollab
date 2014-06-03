package com.esofthead.mycollab.reporting;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import net.sf.dynamicreports.report.definition.ReportParameters;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public class PercentageNumberExpression extends AbstractFieldExpression {
	private static final long serialVersionUID = 1L;

	public PercentageNumberExpression(String field) {
		super(field);
	}

	@Override
	public String evaluate(ReportParameters reportParameters) {
		DecimalFormat df = new DecimalFormat("#");
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		Double percentValue = reportParameters.getValue(field);
		return df.format(percentValue) + "%";
	}

}
