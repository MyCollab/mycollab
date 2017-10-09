package com.mycollab.vaadin.web.ui;

import com.mycollab.vaadin.UserUIContext;

import java.util.Arrays;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.3.0
 */
public class I18nValueComboBox extends ValueComboBox {
    private static final long serialVersionUID = 1L;

    public I18nValueComboBox() {
        super();
    }

    public I18nValueComboBox(boolean nullIsAllowable, Enum<?>... keys) {
        this();
        setNullSelectionAllowed(nullIsAllowable);
        loadData(Arrays.asList(keys));
    }

    public final void loadData(List<? extends Enum<?>> values) {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);

        if (values.size() > 0) {
            for (Enum<?> entry : values) {
                this.addItem(entry.name());
                this.setItemCaption(entry.name(), UserUIContext.getMessage(entry));
            }

            if (!this.isNullSelectionAllowed()) {
                this.select(this.getItemIds().iterator().next());
            }
        }
    }
}