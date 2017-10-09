package com.mycollab.reporting.expression;

import com.mycollab.core.utils.HumanTime;
import net.sf.dynamicreports.report.definition.ReportParameters;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public class HumanTimeExpression extends SimpleFieldExpression {
    public HumanTimeExpression(String field) {
        super(field);
    }

    @Override
    public String evaluate(ReportParameters reportParameters) {
        Double longValue = reportParameters.getFieldValue(field);
        if (longValue != null) {
            Double milis = longValue * 1000 * 60 * 60;
            return HumanTime.exactly(milis.longValue());
        } else {
            return "O d";
        }
    }
}
