package com.mycollab.vaadin.event;

/**
 * Interface denote to have at least one instance of class
 * <code>PreviewFormHandler</code> in its concrete class
 *
 * @param <T>
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface HasPreviewFormHandlers<T> {

    /**
     * @param handler
     */
    void addFormHandler(PreviewFormHandler<T> handler);
}
