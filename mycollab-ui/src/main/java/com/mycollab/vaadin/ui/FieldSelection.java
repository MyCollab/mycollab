package com.mycollab.vaadin.ui;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface FieldSelection<B> {
    void fireValueChange(B data);
}
