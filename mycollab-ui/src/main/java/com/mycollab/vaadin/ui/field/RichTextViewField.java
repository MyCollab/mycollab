package com.mycollab.vaadin.ui.field;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class RichTextViewField extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private String value;

    public RichTextViewField(String value) {
        this.value = value;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    protected Component initContent() {
        return ELabel.html(StringUtils.formatRichText(value)).withStyleName(UIConstants.LABEL_WORD_WRAP);
    }
}
