package com.esofthead.mycollab.vaadin.ui;

import java.util.List;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.vaadin.ui.Component;

public interface IPagedBeanList<S extends SearchCriteria, T> extends Component {
	void setSearchCriteria(S searchCriteria);

	List<T> getCurrentDataList();

	T getBeanByIndex(Object itemId);

	void refresh();
}
