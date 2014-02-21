package com.esofthead.mycollab.mobile.ui;

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
public abstract class AbstractPagedBeanList<S extends SearchCriteria, B> extends CssLayout implements IPagedBeanList<S, B> {
	private static final long serialVersionUID = 1504984093640864283L;

	protected int displayNumItems = SearchRequest.DEFAULT_NUMBER_SEARCH_ITEMS;
	protected List<B> currentListData;

	protected SearchRequest<S> searchRequest;
	protected int currentPage = 1;
	protected int totalPage = 1;
	protected int currentViewCount;
	protected int totalCount;

	protected Table tableItem;

	protected String displayColumnId;
	protected final Class<B> type;

	protected Map<Class<? extends ApplicationEvent>, Set<ApplicationEventListener<?>>> mapEventListener;

	protected final Map<Object, ColumnGenerator> columnGenerators = new HashMap<Object, Table.ColumnGenerator>();

	public AbstractPagedBeanList(Class<B> type,
			String displayColumnId) {
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

	@Override
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

	@Override
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

	@Override
	@SuppressWarnings("unchecked")
	public B getBeanByIndex(final Object itemId) {
		final Container container = this.tableItem.getContainerDataSource();
		final BeanItem<B> item = (BeanItem<B>) container.getItem(itemId);
		return (item == null) ? null : item.getBean();
	}

	@Override
	public void refresh() {
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

		final BeanItemContainer<B> container = new BeanItemContainer<B>(
				this.type, this.currentListData);
		this.tableItem.setPageLength(0);
		this.tableItem.setContainerDataSource(container);
		this.tableItem.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		this.tableItem.setVisibleColumns(new String[]{this.displayColumnId});
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

	public String getDisplayColumnId() {
		return this.displayColumnId;
	}
}
