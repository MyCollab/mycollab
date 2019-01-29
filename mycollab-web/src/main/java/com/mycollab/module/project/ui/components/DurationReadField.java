package com.mycollab.module.project.ui.components;

import com.mycollab.core.utils.HumanTime;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
public class DurationReadField extends CustomField<Long> {

    private Label label = new Label();

    @Override
    protected Component initContent() {
        return label;
    }

    @Override
    protected void doSetValue(Long value) {
        if (value != null) {
            HumanTime humanTime = new HumanTime(value);
            label.setValue(humanTime.getApproximately());
        }
    }

    @Override
    public Long getValue() {
        return null;
    }
}
