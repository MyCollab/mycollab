package com.mycollab.vaadin.ui.field;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class I18nFormViewField extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private Label label;

    public I18nFormViewField(final String key, Class<? extends Enum> enumCls) {
        Class<? extends Enum> enumClass = enumCls;
        label = new Label();
        label.setContentMode(ContentMode.TEXT);
        label.setWidthUndefined();
        label.addStyleName(UIConstants.LABEL_WORD_WRAP);

        if (StringUtils.isNotBlank(key)) {
            try {
                String value = UserUIContext.getMessage(enumClass, key);
                label.setValue(value);
            } catch (Exception e) {
                label.setValue("");
            }
        } else {
            label.setValue("");
        }
    }

    public I18nFormViewField withStyleName(String styleName) {
        label.addStyleName(styleName);
        return this;
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
