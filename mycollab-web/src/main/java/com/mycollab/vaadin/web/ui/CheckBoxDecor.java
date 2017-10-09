package com.mycollab.vaadin.web.ui;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class CheckBoxDecor extends CheckBox {
    private static final long serialVersionUID = 1L;

    public CheckBoxDecor(String title, boolean value) {
        super(title, value);
        this.addStyleName(ValoTheme.CHECKBOX_SMALL);
    }

    public void setValueWithoutNotifyListeners(boolean value) {
        this.setInternalValue(value);
    }
}
