package com.mycollab.vaadin.ui.field;

import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
public class StyleViewField extends CustomField<Object> {
    private ELabel label;
    private String value;

    public StyleViewField(String value) {
        this.value = value;
        label = new ELabel(value, ContentMode.HTML).withFullWidth().withStyleName(WebThemes.LABEL_WORD_WRAP)
                .withUndefinedWidth().withDescription(value);
    }

    public StyleViewField withStyleName(String styleName) {
        label.addStyleName(styleName);
        return this;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    protected Component initContent() {
        return label;
    }

    @Override
    protected void doSetValue(Object value) {

    }
}
