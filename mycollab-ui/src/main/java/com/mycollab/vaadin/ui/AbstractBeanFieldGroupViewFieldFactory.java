package com.mycollab.vaadin.ui;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class AbstractBeanFieldGroupViewFieldFactory<B> extends AbstractBeanFieldGroupFieldFactory<B> {
    private static final long serialVersionUID = 1L;

    public AbstractBeanFieldGroupViewFieldFactory(GenericBeanForm<B> form) {
        super(form, true, true);
    }

}
