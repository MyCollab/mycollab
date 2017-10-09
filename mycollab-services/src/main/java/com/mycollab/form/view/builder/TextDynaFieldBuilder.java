package com.mycollab.form.view.builder;

import com.mycollab.form.view.builder.type.TextDynaField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TextDynaFieldBuilder extends AbstractDynaFieldBuilder<TextDynaField> {
    public TextDynaFieldBuilder() {
        field = new TextDynaField();
    }

    public TextDynaFieldBuilder maxLength(int maxLength) {
        field.setMaxLength(maxLength);
        return this;
    }
}
