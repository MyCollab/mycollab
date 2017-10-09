package com.mycollab.reporting.expression;

import com.mycollab.core.utils.DateTimeUtils;
import net.sf.dynamicreports.report.definition.ReportParameters;

import java.util.Date;
import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
public class DateTimeExpression extends SimpleFieldExpression {
    private static final long serialVersionUID = 1L;

    public DateTimeExpression(String field) {
        super(field);
    }

    @Override
    public String evaluate(ReportParameters reportParameters) {
        Date date = reportParameters.getFieldValue(field);
        return DateTimeUtils.formatDate(date, "DD/mm/yyyy", Locale.US);
    }

}
