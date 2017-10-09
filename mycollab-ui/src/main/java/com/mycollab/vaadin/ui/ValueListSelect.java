package com.mycollab.vaadin.ui;

import com.vaadin.ui.ListSelect;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ValueListSelect extends ListSelect {
    private static final long serialVersionUID = 1L;

    public void loadData(String[] values) {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);

        Arrays.stream(values).forEach(this::addItem);
        this.setRows(4);
        this.setMultiSelect(true);
    }
}
