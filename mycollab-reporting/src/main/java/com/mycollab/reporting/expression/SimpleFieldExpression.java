package com.mycollab.reporting.expression;

import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
public abstract class SimpleFieldExpression<T> extends AbstractSimpleExpression<T> {
    private static final long serialVersionUID = 1L;
    protected String field;

    public SimpleFieldExpression(String field) {
        this.field = field;
    }
}
