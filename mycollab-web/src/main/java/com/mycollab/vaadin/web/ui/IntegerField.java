package com.mycollab.vaadin.web.ui;

import org.vaadin.viritin.fields.AbstractNumberField;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class IntegerField extends AbstractNumberField<Integer> {
    @Override
    protected void userInputToValue(String str) {
        try {
            this.setValue(Integer.parseInt(str));
        } catch (Exception e) {
            this.setValue(0);
        }
    }

    @Override
    public Class<? extends Integer> getType() {
        return Integer.class;
    }
}
