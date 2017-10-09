package com.mycollab.reporting.expression;

import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.module.user.domain.SimpleUser;
import net.sf.dynamicreports.report.definition.ReportParameters;

import java.util.Date;
import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
public class DateExpression extends SimpleFieldExpression {
    private static final long serialVersionUID = 1L;

    public DateExpression(String field) {
        super(field);
    }

    @Override
    public String evaluate(ReportParameters reportParameters) {
        Date date = reportParameters.getFieldValue(field);
        SimpleUser user = reportParameters.getParameterValue("user");
        return DateTimeUtils.formatDate(date, user.getDateFormat(), Locale.US);
    }
}
