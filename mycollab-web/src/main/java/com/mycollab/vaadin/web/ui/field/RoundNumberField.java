package com.mycollab.vaadin.web.ui.field;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public class RoundNumberField extends CustomField<Number> {
    private static final long serialVersionUID = 1L;

    private Number value;

    public RoundNumberField(final Number value) {
        this.value = value;
    }

    @Override
    public Class<Number> getType() {
        return Number.class;
    }

    @Override
    protected Component initContent() {
        final Label label = new Label();
        label.setWidth("100%");

        if (value != null) {
            double d = value.doubleValue();
            d = Math.round(d * 100);
            d = d / 100;
            label.setValue(d + "");
        } else {
            label.setValue("");
        }

        return label;
    }
}
