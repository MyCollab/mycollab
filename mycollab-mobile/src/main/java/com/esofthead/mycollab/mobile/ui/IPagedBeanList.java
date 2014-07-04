package com.esofthead.mycollab.mobile.ui;

import java.util.List;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.vaadin.ui.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.1
 * 
 */
public interface IPagedBeanList<S extends SearchCriteria, T> extends Component {
	void setSearchCriteria(S searchCriteria);

	List<T> getCurrentDataList();

	void refresh();
}
