package com.mycollab.vaadin.web.ui;

import com.vaadin.ui.ComboBox;

import java.util.stream.Stream;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ValueComboBox extends ComboBox {
    private static final long serialVersionUID = 1L;

    public ValueComboBox() {
        this.setPageLength(20);
    }

    /**
     * @param nullIsAllowable
     * @param values
     */
    public ValueComboBox(boolean nullIsAllowable, String... values) {
        this();
        this.setNullSelectionAllowed(nullIsAllowable);
        this.setImmediate(true);
        this.loadData(values);

        this.select(this.getItemIds().iterator().next());
    }

    public final void loadData(String... values) {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
        Stream.of(values).forEach(value -> addItem(value));

        if (!this.isNullSelectionAllowed()) {
            this.select(this.getItemIds().iterator().next());
        }
    }
}
