package com.mycollab.vaadin.ui;

import java.util.EventObject;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class PropertyChangedEvent  extends EventObject {
    private String bindProperty;

    public PropertyChangedEvent(Object source, String bindProperty) {
        super(source);
        this.bindProperty = bindProperty;
    }

    public String getBindProperty() {
        return bindProperty;
    }
}
