package com.mycollab.mobile.ui;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.mobile.mvp.AbstractPresenter;

/**
 * @param <V>
 * @param <S>
 * @param <B>
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public abstract class AbstractListPresenter<V extends IListView<S, B>, S extends SearchCriteria, B extends ValuedBean> extends AbstractPresenter<V> {
    private static final long serialVersionUID = -2202567598255893303L;

    protected S searchCriteria;

    public AbstractListPresenter(Class<V> viewClass) {
        super(viewClass);
    }

    public void doSearch(S searchCriteria) {
        this.searchCriteria = searchCriteria;
        getView().getPagedBeanTable().search(searchCriteria);
    }
}
