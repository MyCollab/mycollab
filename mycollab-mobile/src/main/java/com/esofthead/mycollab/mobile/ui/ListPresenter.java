package com.esofthead.mycollab.mobile.ui;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.mobile.mvp.AbstractPresenter;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 * @param <V>
 * @param <S>
 * @param <B>
 */
public abstract class ListPresenter<V extends ListView<S, B>, S extends SearchCriteria, B extends ValuedBean>
		extends AbstractPresenter<V> {
	private static final long serialVersionUID = -8152581262119621387L;

	protected S searchCriteria;

	public ListPresenter(Class<V> viewClass) {
		super(viewClass);
	}

	public void doSearch(S searchCriteria) {
		this.searchCriteria = searchCriteria;
		view.getPagedBeanTable().setSearchCriteria(searchCriteria);
	}

}
