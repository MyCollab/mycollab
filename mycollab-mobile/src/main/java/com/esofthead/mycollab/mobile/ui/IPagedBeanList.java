package com.esofthead.mycollab.mobile.ui;

import java.util.List;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

public interface IPagedBeanList<S extends SearchCriteria, T> extends Component {
	void setSearchCriteria(S searchCriteria);

	List<T> getCurrentDataList();

	void addTableListener(
			ApplicationEventListener<? extends ApplicationEvent> listener);

	void addGeneratedColumn(Object id, Table.ColumnGenerator generatedColumn);

	T getBeanByIndex(Object itemId);

	void refresh();
}
