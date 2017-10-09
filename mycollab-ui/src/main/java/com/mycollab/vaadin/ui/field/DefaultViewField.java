package com.mycollab.vaadin.ui.field;

import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public final class DefaultViewField extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private ELabel label;
    private String value;

    public DefaultViewField(final String value) {
        this(value, ContentMode.TEXT);
    }

    public DefaultViewField(final String value, final ContentMode contentMode) {
        this.value = value;
        label = new ELabel(value, contentMode).withFullWidth().withStyleName(UIConstants.LABEL_WORD_WRAP)
                .withWidthUndefined().withDescription(value);
    }

    public DefaultViewField withStyleName(String styleName) {
        label.addStyleName(styleName);
        return this;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    protected Component initContent() {
        return label;
    }
}
