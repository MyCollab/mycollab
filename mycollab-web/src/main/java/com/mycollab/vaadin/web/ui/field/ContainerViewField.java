package com.mycollab.vaadin.web.ui.field;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class ContainerViewField extends CustomField<Object> {
    private static final long serialVersionUID = 1L;
    private CssLayout layout;

    public ContainerViewField() {
        layout = new CssLayout();
        layout.setWidth("100%");
    }

    public void addComponentField(Component component) {
        layout.addComponent(component);
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
