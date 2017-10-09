package com.mycollab.vaadin.ui;

import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MCssLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
public class FormContainer extends VerticalLayout {
    public FormContainer() {
        this.addStyleName("form");
        this.setWidth("100%");
        this.setDefaultComponentAlignment(Alignment.TOP_CENTER);
    }

    public void addSection(String sectionName, ComponentContainer container) {
        this.addSection(new MCssLayout(new Label(sectionName)), container);
    }

    public void addSection(Component sectionHeader, ComponentContainer container) {
        sectionHeader.addStyleName("section");
        sectionHeader.setWidth("100%");
        container.setWidth("100%");
        this.addComponent(sectionHeader);
        this.addComponent(container);
    }
}
