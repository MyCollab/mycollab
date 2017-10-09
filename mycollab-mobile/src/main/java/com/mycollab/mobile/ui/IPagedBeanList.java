package com.mycollab.mobile.ui;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.ui.IBeanList;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.3.1
 */
public interface IPagedBeanList<S extends SearchCriteria, T> extends Component, IBeanList<T> {
    Integer search(S searchCriteria);

    void setSearchCriteria(S searchCriteria);

    void refresh();
}
