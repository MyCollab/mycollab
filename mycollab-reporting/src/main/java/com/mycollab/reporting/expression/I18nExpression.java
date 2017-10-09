package com.mycollab.reporting.expression;

import com.mycollab.i18n.LocalizationHelper;
import net.sf.dynamicreports.report.definition.ReportParameters;

import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 5.0.5
 */
public class I18nExpression extends SimpleFieldExpression {
    private Class keyCls;

    public I18nExpression(String field, Class keyCls) {
        super(field);
        this.keyCls = keyCls;
    }

    @Override
    public String evaluate(ReportParameters reportParameters) {
        Locale locale = reportParameters.getLocale();
        String stringValue = reportParameters.getFieldValue(field).toString();
        try {
            return LocalizationHelper.getMessage(locale, keyCls, stringValue);
        } catch (Exception e) {
            return stringValue;
        }
    }
}
