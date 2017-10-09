package com.mycollab.vaadin.web.ui;

import com.vaadin.ui.Button;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class CommonUIFactory {
    public static MButton createButtonTooltip(String caption, String description) {
        return new MButton(caption).withDescription(description);
    }

    public static MButton createButtonTooltip(String caption, String description, Button.ClickListener listener) {
        return new MButton(caption).withDescription(description).withListener(listener);
    }
}
