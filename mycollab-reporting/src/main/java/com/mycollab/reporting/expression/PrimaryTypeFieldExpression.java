package com.mycollab.reporting.expression;

import net.sf.dynamicreports.report.definition.ReportParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
public class PrimaryTypeFieldExpression<T> extends SimpleFieldExpression<T> {
    private static Logger LOG = LoggerFactory.getLogger(PrimaryTypeFieldExpression.class);
    private static final long serialVersionUID = 1L;

    public PrimaryTypeFieldExpression(String field) {
        super(field);
    }

    @Override
    public T evaluate(ReportParameters param) {
        try {
            return param.getFieldValue(field);
        } catch (Exception e) {
            LOG.error("Error while do report", e);
            return null;
        }
    }
}