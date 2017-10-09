package com.mycollab.vaadin.event;

import com.mycollab.db.arguments.SearchCriteria;

/**
 * @param <S>
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface SearchHandler<S extends SearchCriteria> {
    /**
     * @param criteria
     */
    void onSearch(S criteria);
}
