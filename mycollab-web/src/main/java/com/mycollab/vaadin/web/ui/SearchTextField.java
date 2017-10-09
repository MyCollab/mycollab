package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import static com.mycollab.core.utils.StringUtils.isNotBlank;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public abstract class SearchTextField extends MHorizontalLayout {
    private TextField innerField;

    public SearchTextField() {
        this.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        ELabel icon = ELabel.fontIcon(FontAwesome.SEARCH);
        innerField = new TextField();
        innerField.setImmediate(true);
        innerField.setInputPrompt(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
        innerField.setWidth("180px");
        this.with(icon, innerField).withStyleName("searchfield");
        ShortcutListener shortcutListener = new ShortcutListener("searchfield", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                String value = ((TextField) target).getValue();
                if (isNotBlank(value)) {
                    doSearch(value);
                } else {
                    emptySearch();
                }
            }
        };
        ShortcutExtension.installShortcutAction(innerField, shortcutListener);
    }

    abstract public void doSearch(String value);

    abstract public void emptySearch();

    public void setInputPrompt(String value) {
        innerField.setInputPrompt(value);
    }
}
