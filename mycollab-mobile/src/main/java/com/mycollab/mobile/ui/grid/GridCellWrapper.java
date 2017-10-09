package com.mycollab.mobile.ui.grid;

import com.vaadin.ui.*;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
class GridCellWrapper extends CssLayout {
    private static final long serialVersionUID = 1L;

    public GridCellWrapper() {
        this.setStyleName("gridform-field");
        this.setWidth("100%");
    }

    public void addComponent(Component component) {
        if (!(component instanceof Button))
            component.setCaption(null);

        if (component instanceof AbstractTextField || component instanceof RichTextArea) {
            component.setWidth("100%");
        }
        super.addComponent(component);
    }
}
