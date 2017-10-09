package com.mycollab.vaadin.ui;

/**
 * @param <B>
 * @author MyCollab Ltd
 * @since 3.0
 */
public abstract class AbstractBeanFieldGroupEditFieldFactory<B> extends AbstractBeanFieldGroupFieldFactory<B> {
    private static final long serialVersionUID = 1L;

    public AbstractBeanFieldGroupEditFieldFactory(GenericBeanForm<B> form) {
        this(form, true);
    }

    public AbstractBeanFieldGroupEditFieldFactory(GenericBeanForm<B> form, boolean isValidateForm) {
        super(form, isValidateForm, false);
    }
}
