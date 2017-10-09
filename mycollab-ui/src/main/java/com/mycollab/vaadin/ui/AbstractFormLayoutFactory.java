package com.mycollab.vaadin.ui;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class AbstractFormLayoutFactory implements IFormLayoutFactory {
    private Set<String> fields = new HashSet<>();

    public abstract AbstractComponent getLayout();

    protected abstract Component onAttachField(Object propertyId, Field<?> field);

    public Component attachField(Object propertyId, Field<?> field) {
        Component component = onAttachField(propertyId, field);
        if (component != null) {
            fields.add((String) propertyId);
            return component;
        }
        return null;
    }

    public Set<String> bindFields() {
        return fields;
    }
}
