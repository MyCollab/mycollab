package com.mycollab.vaadin.event;

import java.io.Serializable;

/**
 * Page handler of view list or table.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface PageableHandler extends Serializable {
    /**
     * Move current view to new page
     *
     * @param newPageNumber
     */
    void move(int newPageNumber);
}
