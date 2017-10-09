package com.mycollab.vaadin.web.ui;

import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.TextField;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public class ShortcutExtension {
    public static TextField installShortcutAction(final TextField textField, final ShortcutListener listener) {
        textField.addFocusListener(focusEvent -> textField.addShortcutListener(listener));
        textField.addBlurListener(blurEvent -> textField.removeShortcutListener(listener));
        return textField;
    }
}
