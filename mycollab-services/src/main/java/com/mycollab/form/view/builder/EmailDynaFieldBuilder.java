package com.mycollab.form.view.builder;

import com.mycollab.form.view.builder.type.EmailDynaField;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public class EmailDynaFieldBuilder extends AbstractDynaFieldBuilder<EmailDynaField> {
    public EmailDynaFieldBuilder() {
        field = new EmailDynaField();
    }
}
