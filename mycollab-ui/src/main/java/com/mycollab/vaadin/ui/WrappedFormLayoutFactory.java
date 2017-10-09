package com.mycollab.vaadin.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import java.util.Set;

/**
 * @author MyCollab Ltd
 * @since 5.2.1
 */
public abstract class WrappedFormLayoutFactory implements IFormLayoutFactory {
    protected IFormLayoutFactory wrappedLayoutFactory;

    IFormLayoutFactory getWrappedFactory() {
        return wrappedLayoutFactory;
    }

    @Override
    public final Component attachField(Object propertyId, Field<?> field) {
        return wrappedLayoutFactory.attachField(propertyId, field);
    }

    @Override
    public final Set<String> bindFields() {
        return wrappedLayoutFactory.bindFields();
    }
}
