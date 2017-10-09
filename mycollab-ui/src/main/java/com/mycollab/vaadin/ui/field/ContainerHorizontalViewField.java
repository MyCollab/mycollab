package com.mycollab.vaadin.ui.field;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class ContainerHorizontalViewField extends CustomField {
    private static final long serialVersionUID = 1L;

    private MHorizontalLayout layout = new MHorizontalLayout().withFullWidth();

    public void addComponentField(Component component) {
        layout.addComponent(component);
    }

    public HorizontalLayout getLayout() {
        return layout;
    }

    @Override
    public Class<?> getType() {
        return Object.class;
    }

    @Override
    protected Component initContent() {
        return layout;
    }
}
