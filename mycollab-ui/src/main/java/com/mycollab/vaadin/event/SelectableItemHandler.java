package com.mycollab.vaadin.event;

/**
 * @param <T>
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface SelectableItemHandler<T> {
    /**
     * @param item
     */
    void onSelect(T item);
}
