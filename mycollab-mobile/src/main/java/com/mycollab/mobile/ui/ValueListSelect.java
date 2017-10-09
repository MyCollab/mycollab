package com.mycollab.mobile.ui;

import com.vaadin.ui.ListSelect;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class ValueListSelect extends ListSelect {
    private static final long serialVersionUID = 1L;

    public ValueListSelect() {
        super();
        this.setRows(1);
    }

    /**
     * @param nullIsAllowable
     * @param values
     */
    public ValueListSelect(boolean nullIsAllowable, String... values) {
        super();
        this.setNullSelectionAllowed(nullIsAllowable);
        this.setImmediate(true);
        this.loadData(values);

        this.setRows(1);
    }

    public ValueListSelect(boolean nullIsAllowable, Number... values) {
        super();
        this.setRows(1);
        this.setNullSelectionAllowed(nullIsAllowable);
        this.setImmediate(true);
        this.loadData(values);

        if (!this.isNullSelectionAllowed()) {
            this.select(this.getItemIds().iterator().next());
        }
    }

    public final void loadData(String... values) {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);

        for (String value: values) {
            this.addItem(value);
        }

        if (!this.isNullSelectionAllowed()) {
            this.select(this.getItemIds().iterator().next());
        }
    }

    public final void loadData(Number... values) {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);

        for (Number value: values) {
            this.addItem(value);
        }
    }
}
