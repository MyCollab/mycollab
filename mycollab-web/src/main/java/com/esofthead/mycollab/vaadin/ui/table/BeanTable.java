/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.ui.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Table;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 * @param <SearchService>
 * @param <S>
 * @param <T>
 */
public class BeanTable<SearchService extends ISearchableService<S>, S extends SearchCriteria, T>
		extends Table implements IBeanTable {

	private static final long serialVersionUID = 1L;
	private TableViewField requiredColumn;
	private List<TableViewField> displayColumns;

	private Class typeClass;
	private SearchService searchService;
	private Map<Class<? extends ApplicationEvent>, Set<ApplicationEventListener<?>>> mapEventListener;

	public BeanTable(SearchService searchService, Class typeClass,
			TableViewField requiredColumn, List<TableViewField> displayColumns) {
		super();
		this.searchService = searchService;
		this.typeClass = typeClass;
		this.requiredColumn = requiredColumn;
		this.displayColumns = displayColumns;
		this.setStyleName("list-view");
	}

	public void setSearchCriteria(S searchCriteria) {
		List itemsCol = searchService
				.findPagableListByCriteria(new SearchRequest<S>(searchCriteria,
						0, Integer.MAX_VALUE));
		setItems(itemsCol);
	}

	private void displayTableColumns() {
		List<String> visibleColumnsCol = new ArrayList<String>();
		List<String> columnHeadersCol = new ArrayList<String>();

		if (requiredColumn != null) {
			visibleColumnsCol.add(requiredColumn.getField());
			columnHeadersCol.add(requiredColumn.getDesc());
			this.setColumnWidth(requiredColumn.getField(),
					requiredColumn.getDefaultWidth());
		}

		for (int i = 0; i < displayColumns.size(); i++) {
			TableViewField viewField = displayColumns.get(i);
			visibleColumnsCol.add(viewField.getField());
			columnHeadersCol.add(viewField.getDesc());

			if (i == 0) {
				this.setColumnExpandRatio(viewField.getField(), 1.0f);
			} else {
				this.setColumnWidth(viewField.getField(),
						viewField.getDefaultWidth());
			}
		}

		String[] visibleColumns = visibleColumnsCol.toArray(new String[0]);
		String[] columnHeaders = columnHeadersCol.toArray(new String[0]);

		this.setVisibleColumns(visibleColumns);
		this.setColumnHeaders(columnHeaders);
	}

	public void setItems(Collection<T> itemsCol) {
		BeanItemContainer<T> container = new BeanItemContainer<T>(typeClass,
				itemsCol);
		this.setContainerDataSource(container);
		displayTableColumns();
		this.setPageLength(itemsCol.size());
	}

	@SuppressWarnings("unchecked")
	public T getBeanByIndex(Object itemId) {
		Container container = this.getContainerDataSource();
		BeanItem<T> item = (BeanItem<T>) container.getItem(itemId);
		return (item == null) ? null : item.getBean();
	}

	public void addTableListener(
			ApplicationEventListener<? extends ApplicationEvent> listener) {
		if (mapEventListener == null) {
			mapEventListener = new HashMap<Class<? extends ApplicationEvent>, Set<ApplicationEventListener<?>>>();
		}

		Set<ApplicationEventListener<?>> listenerSet = mapEventListener
				.get(listener.getEventType());
		if (listenerSet == null) {
			listenerSet = new LinkedHashSet<ApplicationEventListener<?>>();
			mapEventListener.put(listener.getEventType(), listenerSet);
		}

		listenerSet.add(listener);
	}

	protected void fireTableEvent(ApplicationEvent event) {

		Class<? extends ApplicationEvent> eventType = event.getClass();

		Set<ApplicationEventListener<?>> eventSet = mapEventListener
				.get(eventType);
		if (eventSet != null) {
			Iterator<ApplicationEventListener<?>> listenerSet = mapEventListener
					.get(eventType).iterator();

			while (listenerSet.hasNext()) {
				ApplicationEventListener<?> listener = listenerSet.next();
				@SuppressWarnings("unchecked")
				ApplicationEventListener<ApplicationEvent> l = (ApplicationEventListener<ApplicationEvent>) listener;
				l.handle(event);
			}
		}
	}
}
