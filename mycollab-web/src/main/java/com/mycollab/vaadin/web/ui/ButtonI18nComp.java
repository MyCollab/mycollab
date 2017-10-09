package com.mycollab.vaadin.web.ui;

import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.Button;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public class ButtonI18nComp extends MButton {
    private static final long serialVersionUID = 1L;

    private String key;

    public ButtonI18nComp(String key) {
        this.key = key;
    }

    public ButtonI18nComp(String key, Enum<?> caption, Button.ClickListener listener) {
        this.key = key;
        this.withCaption(UserUIContext.getMessage(caption)).withDescription(UserUIContext.getMessage(caption))
                .withListener(listener);
    }

    public String getKey() {
        return key;
    }
}
