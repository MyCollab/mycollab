package com.mycollab.vaadin.web.ui.grid;

import com.mycollab.vaadin.web.ui.MultiSelectComp;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.RichTextArea;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.0.8
 */
class GridCellWrapper extends MHorizontalLayout {
    private static final long serialVersionUID = 1L;

    GridCellWrapper() {
        this.withMargin(true).withFullWidth().withStyleName("gridform-field");
    }

    public void addComponent(Component component) {
        if (!(component instanceof Button))
            component.setCaption(null);

        if (component instanceof MultiSelectComp) {
            component.setWidth("200px");
        } else if (component instanceof AbstractTextField || component instanceof RichTextArea) {
            component.setWidth("100%");
        }
        super.addComponent(component);
    }
}