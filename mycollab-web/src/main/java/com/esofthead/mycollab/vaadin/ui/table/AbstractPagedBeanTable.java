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

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.common.domain.CustomViewStore;
import com.esofthead.mycollab.common.domain.NullCustomViewStore;
import com.esofthead.mycollab.common.service.CustomViewStoreService;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.utils.XStreamJsonDeSerializer;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.PagableHandler;
import com.esofthead.mycollab.vaadin.events.SelectableItemHandler;
import com.esofthead.mycollab.vaadin.ui.ButtonLinkLegacy;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.ColumnGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 * @param <S>
 * @param <B>
 */
public abstract class AbstractPagedBeanTable<S extends SearchCriteria, B>
		extends VerticalLayout implements IPagedBeanTable<S, B> {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(AbstractPagedBeanTable.class);

	protected int displayNumItems = SearchRequest.DEFAULT_NUMBER_SEARCH_ITEMS;
	protected Collection<B> currentListData;

	protected HorizontalLayout pageManagement;

	protected boolean isAscending = true;
	protected Object sortColumnId;

	protected SearchRequest<S> searchRequest;
	protected int currentPage = 1;
	protected int totalPage = 1;
	protected int currentViewCount;
	protected int totalCount;

	protected Table tableItem;
	protected CssLayout controlBarWrapper;

	protected Set<SelectableItemHandler<B>> selectableHandlers;
	protected Set<PagableHandler> pagableHandlers;

	protected Class<B> type;

	private TableViewField requiredColumn;
	private List<TableViewField> displayColumns;
	private List<TableViewField> defaultSelectedColumns;

	protected final Map<Object, ColumnGenerator> columnGenerators = new HashMap<>();

	public AbstractPagedBeanTable(Class<B> type,
			List<TableViewField> displayColumns) {
		this(type, null, displayColumns);
	}

	public AbstractPagedBeanTable(Class<B> type, TableViewField requiredColumn,
			List<TableViewField> displayColumns) {
		this(type, null, requiredColumn, displayColumns);
	}

	@SuppressWarnings("unchecked")
	public AbstractPagedBeanTable(Class<B> type, String viewId,
			TableViewField requiredColumn, List<TableViewField> displayColumns) {
		if (viewId != null) {
			CustomViewStoreService customViewStoreService = ApplicationContextUtil
					.getSpringBean(CustomViewStoreService.class);
			CustomViewStore viewLayoutDef = customViewStoreService
					.getViewLayoutDef(AppContext.getAccountId(),
							AppContext.getUsername(), viewId);
			if (!(viewLayoutDef instanceof NullCustomViewStore)) {
				try {
					List<TableViewField> selectedColumns = (List<TableViewField>) XStreamJsonDeSerializer
							.fromJson(viewLayoutDef.getViewinfo());
					// @HACK: the problem in deserialize json cause the list of
					// list
					this.displayColumns = (List<TableViewField>) selectedColumns
							.get(0);
				} catch (Exception e) {
					LOG.error("Error", e);
					this.displayColumns = displayColumns;
				}
			} else {
				this.displayColumns = displayColumns;
			}
		} else {
			this.displayColumns = displayColumns;
		}

		this.defaultSelectedColumns = displayColumns;
		this.requiredColumn = requiredColumn;
		this.type = type;

		this.setStyleName("list-view");
	}

	public void setDisplayColumns(List<TableViewField> viewFields) {
		this.displayColumns = viewFields;
		displayTableColumns();
		this.markAsDirty();
	}

	private void displayTableColumns() {
		List<String> visibleColumnsCol = new ArrayList<>();
		List<String> columnHeadersCol = new ArrayList<>();

		if (requiredColumn != null) {
			visibleColumnsCol.add(requiredColumn.getField());
			columnHeadersCol.add(AppContext.getMessage(requiredColumn
					.getDescKey()));
			this.tableItem.setColumnWidth(requiredColumn.getField(),
					requiredColumn.getDefaultWidth());
		}

		for (int i = 0; i < displayColumns.size(); i++) {
			TableViewField viewField = displayColumns.get(i);
			visibleColumnsCol.add(viewField.getField());
			columnHeadersCol.add(AppContext.getMessage(viewField.getDescKey()));

			if (i == 0) {
				this.tableItem.setColumnExpandRatio(viewField.getField(), 1.0f);
			} else {
				this.tableItem.setColumnWidth(viewField.getField(),
						viewField.getDefaultWidth());
			}
		}

		String[] visibleColumns = visibleColumnsCol.toArray(new String[visibleColumnsCol.size()]);
		String[] columnHeaders = columnHeadersCol.toArray(new String[columnHeadersCol.size()]);

		this.tableItem.setVisibleColumns(visibleColumns);
		this.tableItem.setColumnHeaders(columnHeaders);
	}

	@Override
	public void addSelectableItemHandler(final SelectableItemHandler<B> handler) {
		if (this.selectableHandlers == null) {
			this.selectableHandlers = new HashSet<>();
		}
		this.selectableHandlers.add(handler);
	}

	@Override
	public int currentViewCount() {
		return this.currentViewCount;
	}


	@Override
	public int totalItemsCount() {
		return this.totalCount;
	}

	@Override
	public void addPagableHandler(final PagableHandler handler) {
		if (this.pagableHandlers == null) {
			this.pagableHandlers = new HashSet<>();
		}
		this.pagableHandlers.add(handler);

	}

	@Override
	public Collection<B> getCurrentDataList() {
		return currentListData;
	}

	public void setCurrentDataList(Collection<B> list) {
		this.currentListData = list;
		this.currentViewCount = list.size();
		createTable();
	}

	@Override
	public void addTableListener(TableClickListener listener) {
		addListener(TableClickEvent.TABLE_CLICK_IDENTIFIER,
				TableClickEvent.class, listener,
				TableClickListener.itemClickMethod);
	}

	protected void fireTableEvent(final TableClickEvent event) {
		fireEvent(event);
	}

	@Override
	public void addGeneratedColumn(final Object id,
			final ColumnGenerator generatedColumn) {
		this.columnGenerators.put(id, generatedColumn);
	}

	@Override
	public int setSearchCriteria(final S searchCriteria) {
		this.searchRequest = new SearchRequest<>(searchCriteria, currentPage, displayNumItems);
		this.doSearch();
        return this.totalCount;
	}

    public void setDisplayNumItems(int displayNumItems) {
        this.displayNumItems = displayNumItems;
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
		this.doSearch();
	}

	protected void pageChange(final int currentPage) {
		if (this.searchRequest != null) {
			this.currentPage = currentPage;
			this.searchRequest.setCurrentPage(currentPage);
			this.doSearch();

			if (this.pagableHandlers != null) {
				for (final PagableHandler handler : this.pagableHandlers) {
					handler.move(currentPage);
				}
			}
		}
	}

	public void fireSelectItemEvent(final B item) {
		if (this.selectableHandlers != null) {
			for (final SelectableItemHandler<B> handler : this.selectableHandlers) {
				handler.onSelect(item);
			}
		}
	}

	private CssLayout createControls() {
		this.controlBarWrapper = new CssLayout();
		this.controlBarWrapper.setStyleName("listControl");
		this.controlBarWrapper.setWidth("100%");

		final HorizontalLayout controlBar = new HorizontalLayout();
		controlBar.setWidth("100%");
		this.controlBarWrapper.addComponent(controlBar);

		this.pageManagement = new HorizontalLayout();

		// defined layout here ---------------------------

		if (this.currentPage > 1) {
			final Button firstLink = new ButtonLinkLegacy("1", new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					AbstractPagedBeanTable.this.pageChange(1);
				}
			}, false);
			firstLink.addStyleName("buttonPaging");
			this.pageManagement.addComponent(firstLink);
		}
		if (this.currentPage >= 5) {
			final Label ss1 = new Label("...");
			ss1.addStyleName("buttonPaging");
			this.pageManagement.addComponent(ss1);
		}

		if (this.currentPage > 3) {
			final Button previous2 = new ButtonLinkLegacy(
					"" + (this.currentPage - 2), new ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							AbstractPagedBeanTable.this
									.pageChange(AbstractPagedBeanTable.this.currentPage - 2);
						}
					}, false);
			previous2.addStyleName("buttonPaging");
			this.pageManagement.addComponent(previous2);
		}
		if (this.currentPage > 2) {
			final Button previous1 = new ButtonLinkLegacy(
					"" + (this.currentPage - 1), new ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							AbstractPagedBeanTable.this
									.pageChange(AbstractPagedBeanTable.this.currentPage - 1);
						}
					}, false);
			previous1.addStyleName("buttonPaging");
			this.pageManagement.addComponent(previous1);
		}
		// Here add current ButtonLinkLegacy
		final Button current = new ButtonLinkLegacy("" + this.currentPage,
				new ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						AbstractPagedBeanTable.this
								.pageChange(AbstractPagedBeanTable.this.currentPage);
					}
				}, false);
		current.addStyleName("buttonPaging");
		current.addStyleName("buttonPagingcurrent");

		this.pageManagement.addComponent(current);
		final int range = this.totalPage - this.currentPage;
		if (range >= 1) {
			final Button next1 = new ButtonLinkLegacy("" + (this.currentPage + 1),
					new ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							AbstractPagedBeanTable.this
									.pageChange(AbstractPagedBeanTable.this.currentPage + 1);
						}
					}, false);
			next1.addStyleName("buttonPaging");
			this.pageManagement.addComponent(next1);
		}
		if (range >= 2) {
			final Button next2 = new ButtonLinkLegacy("" + (this.currentPage + 2),
					new ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							AbstractPagedBeanTable.this
									.pageChange(AbstractPagedBeanTable.this.currentPage + 2);
						}
					}, false);
			next2.addStyleName("buttonPaging");
			this.pageManagement.addComponent(next2);
		}
		if (range >= 4) {
			final Label ss2 = new Label("...");
			ss2.addStyleName("buttonPaging");
			this.pageManagement.addComponent(ss2);
		}
		if (range >= 3) {
			final Button last = new ButtonLinkLegacy("" + this.totalPage,
					new ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							AbstractPagedBeanTable.this
									.pageChange(AbstractPagedBeanTable.this.totalPage);
						}
					}, false);
			last.addStyleName("buttonPaging");
			this.pageManagement.addComponent(last);
		}

		this.pageManagement.setWidth(null);
		this.pageManagement.setSpacing(true);
		controlBar.addComponent(this.pageManagement);
		controlBar.setComponentAlignment(this.pageManagement,
				Alignment.MIDDLE_RIGHT);

		return this.controlBarWrapper;
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

		if (this.totalPage > 1) {
			// Define button layout
			if (this.controlBarWrapper != null) {
				this.removeComponent(this.controlBarWrapper);
			}
			this.addComponent(this.createControls());
		} else {
			if (this.getComponentCount() == 2) {
				this.removeComponent(this.getComponent(1));
			}
		}

		this.currentListData = this.queryCurrentData();
		this.currentViewCount = this.currentListData.size();

		createTable();
	}

	private void createTable() {
		this.tableItem = new Table();
		this.tableItem.setWidth("100%");
		this.tableItem.addStyleName("striped");
		this.tableItem.setSortEnabled(false);

		// set column generator
		for (Map.Entry<Object, ColumnGenerator> entry: columnGenerators.entrySet()) {
            tableItem.addGeneratedColumn(entry.getKey(), entry.getValue());
        }

		if (StringUtils.isNotBlank((String) this.sortColumnId)) {
			this.tableItem.setColumnIcon(
					this.sortColumnId,
					this.isAscending ? FontAwesome.CARET_DOWN
							: FontAwesome.CARET_UP);
		}

		this.tableItem.addHeaderClickListener(new Table.HeaderClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void headerClick(final Table.HeaderClickEvent event) {
				final String propertyId = (String) event.getPropertyId();

				if (propertyId.equals("selected")) {
					return;
				}

				if (AbstractPagedBeanTable.this.searchRequest != null) {
					AbstractPagedBeanTable.this.sortColumnId = propertyId;

					final S searchCriteria = AbstractPagedBeanTable.this.searchRequest
							.getSearchCriteria();
					if (searchCriteria.getOrderByField() == null) {
						searchCriteria.setOrderByField(propertyId);
						searchCriteria.setSortDirection(SearchCriteria.DESC);
						AbstractPagedBeanTable.this.isAscending = false;
					} else if (propertyId.equals(searchCriteria
							.getOrderByField())) {
						AbstractPagedBeanTable.this.isAscending = !AbstractPagedBeanTable.this.isAscending;
						searchCriteria
								.setSortDirection(searchCriteria
										.getSortDirection().equals(
												SearchCriteria.ASC) ? SearchCriteria.DESC
										: SearchCriteria.ASC);
					} else {
						searchCriteria.setOrderByField(propertyId);
						searchCriteria.setSortDirection(SearchCriteria.DESC);
						AbstractPagedBeanTable.this.isAscending = false;
					}

					AbstractPagedBeanTable.this
							.setSearchCriteria(searchCriteria);
				}
			}
		});

		final BeanItemContainer<B> container = new BeanItemContainer<>(
				this.type, this.currentListData);
		this.tableItem.setPageLength(0);
		this.tableItem.setContainerDataSource(container);
		displayTableColumns();
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

    public Table getTable() {
        return tableItem;
    }

	public List<TableViewField> getDefaultSelectedColumns() {
		return defaultSelectedColumns;
	}

	public List<TableViewField> getDisplayColumns() {
		return displayColumns;
	}

	public Object[] getVisibleColumns() {
		return tableItem.getVisibleColumns();
	}
}
