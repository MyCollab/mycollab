package com.mycollab.vaadin.web.ui;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class SortButton extends Button {
    private static final long serialVersionUID = 6899070243378436412L;

    private boolean isDesc = true;

    public SortButton() {
        this.setIcon(FontAwesome.CARET_DOWN);
        this.addClickListener(clickEvent -> toggleSortOrder());
    }

    public SortButton(String caption, ClickListener listener) {
        this();
        this.setCaption(caption);
        this.addClickListener(listener);
    }

    public void toggleSortOrder() {
        this.isDesc = !this.isDesc;
        if (this.isDesc) {
            this.setIcon(FontAwesome.CARET_DOWN);
        } else {
            this.setIcon(FontAwesome.CARET_UP);
        }
    }

    public boolean isDesc() {
        return this.isDesc;
    }

}
