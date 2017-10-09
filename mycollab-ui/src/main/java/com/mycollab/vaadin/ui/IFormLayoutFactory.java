package com.mycollab.vaadin.ui;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;

import java.util.Set;

/**
 * @author MyCollab Ltd
 * @since 5.3.2
 */
public interface IFormLayoutFactory {
    AbstractComponent getLayout();

    Component attachField(Object propertyId, Field<?> field);

    Set<String> bindFields();
}
