package com.mycollab.vaadin.web.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class MassUpdateLayout extends VerticalLayout {
    private static final long serialVersionUID = 1L;

    public MassUpdateLayout() {
        this.setWidth("100%");
        this.setStyleName("massupdate-layout");
    }

    public void addBody(final Component content) {
        this.addComponent(content);
    }
}
