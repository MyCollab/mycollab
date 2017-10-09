package com.mycollab.vaadin.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * @param <T>
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class DummyCustomField<T> extends CustomField<T> {
    private static final long serialVersionUID = 1L;

    @Override
    protected Component initContent() {
        return null;
    }

    @Override
    public Class<? extends T> getType() {
        return null;
    }

}
