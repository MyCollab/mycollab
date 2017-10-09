package com.mycollab.vaadin.event;

import com.mycollab.db.arguments.SearchCriteria;

/**
 * @param <S>
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface HasSearchHandlers<S extends SearchCriteria> {
    /**
     * @param handler
     */
    void addSearchHandler(SearchHandler<S> handler);

    /**
     * @param criteria
     */
    void notifySearchHandler(final S criteria);

    /**
     * @param totalCountNumber
     */
    void setTotalCountNumber(Integer totalCountNumber);
}
