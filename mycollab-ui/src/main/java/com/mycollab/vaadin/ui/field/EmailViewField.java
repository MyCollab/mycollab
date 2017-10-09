package com.mycollab.vaadin.ui.field;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class EmailViewField extends UrlLinkViewField {
    private static final long serialVersionUID = 1L;

    public EmailViewField(String email) {
        super("mailto:" + email, email);
    }
}
