package com.mycollab.vaadin.web.ui;

import org.vaadin.viritin.fields.AbstractNumberField;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
public class DoubleField extends AbstractNumberField<Double> {
    private static final long serialVersionUID = 1L;

    @Override
    protected void userInputToValue(String str) {
        try {
            this.setValue(Double.parseDouble(str));
        } catch (Exception e) {
            this.setValue(0d);
        }
    }

    @Override
    public Class<? extends Double> getType() {
        return Double.class;
    }

    @Override
    public void setWidth(String width) {
        tf.setWidth(width);
    }

    @Override
    public Double getValue() {
        Double value = super.getValue();
        return (value == null || value < 0) ? 0d : value;
    }
}
