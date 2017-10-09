package com.mycollab.vaadin.web.ui;

import com.vaadin.ui.ComboBox;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class KeyCaptionComboBox extends ComboBox {
    private static final long serialVersionUID = 1L;

    public KeyCaptionComboBox(boolean nullSelectionAllowed) {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        this.setNullSelectionAllowed(nullSelectionAllowed);
    }

    public void addItem(Object itemId, String caption) {
        this.addItem(itemId);
        this.setItemCaption(itemId, caption);
    }
}
