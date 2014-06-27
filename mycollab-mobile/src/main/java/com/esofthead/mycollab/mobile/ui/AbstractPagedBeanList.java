/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.vaadin.ui.IPagedBeanList;
import com.esofthead.vaadin.mobilecomponent.InfiniteScrollLayout;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Table.ColumnHeaderMode;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractPagedBeanList<S extends SearchCriteria, B>
		extends CssLayout implements IPagedBeanList<S, B> {
	private static final long serialVersionUID = 1504984093640864283L;

	protected int displayNumItems = SearchRequest.DEFAULT_NUMBER_SEARCH_ITEMS;
	protected List<B> currentListData;

	protected SearchRequest<S> searchRequest;
	protected int currentPage = 1;
	protected int totalPage = 1;
	protected int currentViewCount;
	protected int totalCount;

	BeanItemContainer<B> container;

	protected Table tableItem;

	protected String displayColumnId;
	protected final Class<B> type;

	protected Map<Class<? extends ApplicationEvent>, Set<ApplicationEventListener<?>>> mapEventListener;

	protected final Map<Object, ColumnGenerator> columnGenerators = new HashMap<Object, Table.ColumnGenerator>();

	public AbstractPagedBeanList(Class<B> type, String displayColumnId) {
		super();
		setWidth("100%");
		setSizeFull();
		InfiniteScrollLayout scrollLayout = InfiniteScrollLayout.extend(this);
		scrollLayout
				.addScrollListener(new InfiniteScrollLayout.ScrollReachBottomListener() {

					@Override
					public void onReachBottom() {
						loadMore();
					}
				});

		this.type = type;
		this.displayColumnId = displayColumnId;
		this.setStyleName("data-list-view");
	}

	public int currentViewCount() {
		return this.currentViewCount;
	}

	public int totalItemsCount() {
		return this.totalCount;
	}

	@Override
	public List<B> getCurrentDataList() {
		return currentListData;
	}

	public void addTableListener(
			final ApplicationEventListener<? extends ApplicationEvent> listener) {
		if (this.mapEventListener == null) {
			this.mapEventListener = new HashMap<Class<? extends ApplicationEvent>, Set<ApplicationEventListener<?>>>();
		}

		Set<ApplicationEventListener<?>> listenerSet = this.mapEventListener
				.get(listener.getEventType());
		if (listenerSet == null) {
			listenerSet = new LinkedHashSet<ApplicationEventListener<?>>();
			this.mapEventListener.put(listener.getEventType(), listenerSet);
		}

		listenerSet.add(listener);
	}

	protected void fireTableEvent(final ApplicationEvent event) {

		final Class<? extends ApplicationEvent> eventType = event.getClass();
		if (this.mapEventListener == null) {
			return;
		}

		final Set<ApplicationEventListener<?>> eventSet = this.mapEventListener
				.get(eventType);
		if (eventSet != null) {
			final Iterator<ApplicationEventListener<?>> listenerSet = this.mapEventListener
					.get(eventType).iterator();

			while (listenerSet.hasNext()) {
				final ApplicationEventListener<?> listener = listenerSet.next();
				@SuppressWarnings("unchecked")
				final ApplicationEventListener<ApplicationEvent> l = (ApplicationEventListener<ApplicationEvent>) listener;
				l.handle(event);
			}
		}
	}

	public void addGeneratedColumn(final Object id,
			final ColumnGenerator generatedColumn) {
		this.columnGenerators.put(id, generatedColumn);
	}

	@Override
	public void setSearchCriteria(final S searchCriteria) {
		this.searchRequest = new SearchRequest<S>(searchCriteria,
				this.currentPage, this.displayNumItems);
		this.doSearch();
	}

	public SearchRequest<S> getSearchRequest() {
		return this.searchRequest;
	}

	@Override
	@SuppressWarnings("unchecked")
	public B getBeanByIndex(final Object itemId) {
		final Container container = this.tableItem.getContainerDataSource();
		final BeanItem<B> item = (BeanItem<B>) container.getItem(itemId);
		return item == null ? null : item.getBean();
	}

	@Override
	public void refresh() {
		this.currentPage = 1;
		this.searchRequest.setCurrentPage(this.currentPage);
		this.doSearch();
	}

	abstract protected int queryTotalCount();

	abstract protected List<B> queryCurrentData();

	protected void doSearch() {
		this.totalCount = this.queryTotalCount();
		this.totalPage = (this.totalCount - 1)
				/ this.searchRequest.getNumberOfItems() + 1;
		if (this.searchRequest.getCurrentPage() > this.totalPage) {
			this.searchRequest.setCurrentPage(this.totalPage);
		}

		this.currentListData = this.queryCurrentData();
		this.currentViewCount = this.currentListData.size();

		this.tableItem = new Table();
		this.tableItem.setWidth("100%");
		this.tableItem.addStyleName("striped");
		this.tableItem.setSortEnabled(false);

		// set column generator
		for (final Object propertyId : this.columnGenerators.keySet()) {
			this.tableItem.addGeneratedColumn(propertyId,
					this.columnGenerators.get(propertyId));
		}

		container = new BeanItemContainer<B>(this.type, this.currentListData);
		this.tableItem.setPageLength(0);
		this.tableItem.setContainerDataSource(container);
		this.tableItem.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		this.tableItem.setVisibleColumns(new String[] { this.displayColumnId });
		this.tableItem.setWidth("100%");

		if (this.getComponentCount() > 0) {
			final Component component0 = this.getComponent(0);
			if (component0 instanceof Table) {
				this.replaceComponent(component0, tableItem);
			} else {
				this.addComponent(tableItem, 0);
			}
		} else {
			this.addComponent(tableItem, 0);
		}

	}

	protected void loadMore() {
		this.currentPage += 1;
		this.searchRequest.setCurrentPage(this.currentPage);
		List<B> currentData = this.queryCurrentData();
		if (this.currentListData == null)
			this.currentListData = new ArrayList<B>();
		this.currentListData.addAll(currentData);
		this.currentViewCount = this.currentListData.size();
		container.addAll(currentData);
	}

	public String getDisplayColumnId() {
		return this.displayColumnId;
	}

	public BeanItemContainer<B> getBeanContainer() {
		return this.container;
	}
}
