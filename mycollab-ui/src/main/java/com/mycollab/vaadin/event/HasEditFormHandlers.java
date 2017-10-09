package com.mycollab.vaadin.event;

import java.io.Serializable;

/**
 * Collection contains all handlers when edit attachForm
 *
 * @param <T>
 * @author MyCollab Ltd
 * @since 1.0.0
 */
public interface HasEditFormHandlers<T> extends Serializable {
    /**
     * Add edit attachForm handler
     *
     * @param handler handler of edit attachForm
     */
    void addFormHandler(IEditFormHandler<T> handler);
}
