package com.mycollab.vaadin.event;

/**
 * @param <T>
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface HasSelectableItemHandlers<T> {
    /**
     * @param handler
     */
    void addSelectableItemHandler(SelectableItemHandler<T> handler);

    /**
     * @return
     */
    int currentViewCount();

    /**
     * @return
     */
    int totalItemsCount();
}
