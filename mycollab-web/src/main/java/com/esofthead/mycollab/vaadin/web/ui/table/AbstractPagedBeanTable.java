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
package com.esofthead.mycollab.vaadin.web.ui.table;

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
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.*;

/**
 * @param <S>
 * @param <B>
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class AbstractPagedBeanTable<S extends SearchCriteria, B> extends VerticalLayout implements IPagedBeanTable<S, B> {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPagedBeanTable.class);

    protected int displayNumItems = SearchRequest.DEFAULT_NUMBER_SEARCH_ITEMS;
    protected Collection<B> currentListData;

    protected MHorizontalLayout pageManagement;

    protected boolean isAscending = true;
    protected Object sortColumnId;

    protected SearchRequest<S> searchRequest;
    protected int currentPage = 1;
    protected int totalPage = 1;
    protected int currentViewCount;
    protected int totalCount;

    protected Table tableItem;
    protected HorizontalLayout controlBarWrapper;

    protected Set<SelectableItemHandler<B>> selectableHandlers;
    protected Set<PagableHandler> pagableHandlers;

    protected Class<B> type;

    private TableViewField requiredColumn;
    private List<TableViewField> displayColumns;
    private List<TableViewField> defaultSelectedColumns;

    protected final Map<Object, ColumnGenerator> columnGenerators = new HashMap<>();

    public AbstractPagedBeanTable(Class<B> type, List<TableViewField> displayColumns) {
        this(type, null, displayColumns);
    }

    public AbstractPagedBeanTable(Class<B> type, TableViewField requiredColumn, List<TableViewField> displayColumns) {
        this(type, null, requiredColumn, displayColumns);
    }

    public AbstractPagedBeanTable(Class<B> type, String viewId, TableViewField requiredColumn, List<TableViewField> displayColumns) {
        if (viewId != null) {
            CustomViewStoreService customViewStoreService = ApplicationContextUtil.getSpringBean(CustomViewStoreService.class);
            CustomViewStore viewLayoutDef = customViewStoreService.getViewLayoutDef(AppContext.getAccountId(),
                    AppContext.getUsername(), viewId);
            if (!(viewLayoutDef instanceof NullCustomViewStore)) {
                try {
                    List<TableViewField> selectedColumns = (List<TableViewField>) XStreamJsonDeSerializer
                            .fromJson(viewLayoutDef.getViewinfo());
                    // @HACK: the problem in deserialize json cause the list of
                    // list
                    this.displayColumns = (List<TableViewField>) selectedColumns.get(0);
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
            columnHeadersCol.add(AppContext.getMessage(requiredColumn.getDescKey()));
            tableItem.setColumnWidth(requiredColumn.getField(), requiredColumn.getDefaultWidth());
        }

        for (int i = 0; i < displayColumns.size(); i++) {
            TableViewField viewField = displayColumns.get(i);
            visibleColumnsCol.add(viewField.getField());
            columnHeadersCol.add(AppContext.getMessage(viewField.getDescKey()));

            if (i == 0) {
                tableItem.setColumnExpandRatio(viewField.getField(), 1.0f);
            } else {
                tableItem.setColumnWidth(viewField.getField(), viewField.getDefaultWidth());
            }
        }

        String[] visibleColumns = visibleColumnsCol.toArray(new String[visibleColumnsCol.size()]);
        String[] columnHeaders = columnHeadersCol.toArray(new String[columnHeadersCol.size()]);

        tableItem.setVisibleColumns(visibleColumns);
        tableItem.setColumnHeaders(columnHeaders);
    }

    @Override
    public void addSelectableItemHandler(final SelectableItemHandler<B> handler) {
        if (selectableHandlers == null) {
            selectableHandlers = new HashSet<>();
        }
        selectableHandlers.add(handler);
    }

    @Override
    public int currentViewCount() {
        return this.currentViewCount;
    }


    @Override
    public int totalItemsCount() {
        return totalCount;
    }

    @Override
    public void addPagableHandler(final PagableHandler handler) {
        if (pagableHandlers == null) {
            pagableHandlers = new HashSet<>();
        }
        pagableHandlers.add(handler);

    }

    @Override
    public Collection<B> getCurrentDataList() {
        return currentListData;
    }

    public void setCurrentDataList(Collection<B> list) {
        currentListData = list;
        currentViewCount = list.size();
        createTable();
    }

    @Override
    public void addTableListener(TableClickListener listener) {
        addListener(TableClickEvent.TABLE_CLICK_IDENTIFIER, TableClickEvent.class, listener, TableClickListener.itemClickMethod);
    }

    protected void fireTableEvent(TableClickEvent event) {
        fireEvent(event);
    }

    @Override
    public void addGeneratedColumn(Object id, ColumnGenerator generatedColumn) {
        this.columnGenerators.put(id, generatedColumn);
    }

    @Override
    public int setSearchCriteria(final S searchCriteria) {
        searchRequest = new SearchRequest<>(searchCriteria, currentPage, displayNumItems);
        doSearch();
        return totalCount;
    }

    public void setDisplayNumItems(int displayNumItems) {
        this.displayNumItems = displayNumItems;
    }

    @Override
    public B getBeanByIndex(final Object itemId) {
        final Container container = tableItem.getContainerDataSource();
        final BeanItem<B> item = (BeanItem<B>) container.getItem(itemId);
        return item == null ? null : item.getBean();
    }

    @Override
    public void refresh() {
        doSearch();
    }

    protected void pageChange(final int currentPage) {
        if (searchRequest != null) {
            this.currentPage = currentPage;
            searchRequest.setCurrentPage(currentPage);
            doSearch();

            if (pagableHandlers != null) {
                for (final PagableHandler handler : pagableHandlers) {
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

    private ComponentContainer createPagingControls() {
        controlBarWrapper = new HorizontalLayout();
        controlBarWrapper.setWidth("100%");
        controlBarWrapper.setStyleName("listControl");

        pageManagement = new MHorizontalLayout();

        // defined layout here ---------------------------

        if (currentPage > 1) {
            Button firstLink = new Button("1", new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    pageChange(1);
                }
            });
            firstLink.addStyleName("buttonPaging");
            pageManagement.addComponent(firstLink);
        }
        if (currentPage >= 5) {
            Label ss1 = new Label("...");
            ss1.addStyleName("buttonPaging");
            pageManagement.addComponent(ss1);
        }

        if (currentPage > 3) {
            Button previous2 = new Button("" + (currentPage - 2), new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    pageChange(currentPage - 2);
                }
            });
            previous2.addStyleName("buttonPaging");
            pageManagement.addComponent(previous2);
        }
        if (currentPage > 2) {
            Button previous1 = new Button("" + (currentPage - 1), new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    pageChange(currentPage - 1);
                }
            });
            previous1.addStyleName("buttonPaging");
            pageManagement.addComponent(previous1);
        }
        // Here add current ButtonLinkLegacy
        Button current = new Button("" + currentPage, new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                pageChange(currentPage);
            }
        });
        current.addStyleName("buttonPaging");
        current.addStyleName("current");

        pageManagement.addComponent(current);
        final int range = totalPage - currentPage;
        if (range >= 1) {
            Button next1 = new Button("" + (currentPage + 1), new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    pageChange(currentPage + 1);
                }
            });
            next1.addStyleName("buttonPaging");
            pageManagement.addComponent(next1);
        }
        if (range >= 2) {
            Button next2 = new Button("" + (currentPage + 2), new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    pageChange(currentPage + 2);
                }
            });
            next2.addStyleName("buttonPaging");
            pageManagement.addComponent(next2);
        }
        if (range >= 4) {
            final Label ss2 = new Label("...");
            ss2.addStyleName("buttonPaging");
            pageManagement.addComponent(ss2);
        }
        if (range >= 3) {
            Button last = new Button("" + totalPage, new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    pageChange(totalPage);
                }
            });
            last.addStyleName("buttonPaging");
            pageManagement.addComponent(last);
        }

        pageManagement.setWidth(null);
        controlBarWrapper.addComponent(pageManagement);
        controlBarWrapper.setComponentAlignment(pageManagement, Alignment.MIDDLE_RIGHT);

        return controlBarWrapper;
    }

    abstract protected int queryTotalCount();

    abstract protected List<B> queryCurrentData();

    protected void doSearch() {
        totalCount = this.queryTotalCount();
        totalPage = (totalCount - 1) / searchRequest.getNumberOfItems() + 1;
        if (searchRequest.getCurrentPage() > totalPage) {
            searchRequest.setCurrentPage(totalPage);
        }

        if (totalPage > 1) {
            // Define button layout
            if (controlBarWrapper != null) {
                removeComponent(this.controlBarWrapper);
            }
            this.addComponent(this.createPagingControls());
        } else {
            if (getComponentCount() == 2) {
                removeComponent(getComponent(1));
            }
        }

        currentListData = queryCurrentData();
        currentViewCount = currentListData.size();

        createTable();
    }

    private void createTable() {
        tableItem = new Table();
        tableItem.setWidth("100%");
        tableItem.addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
        tableItem.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tableItem.setSortEnabled(false);

        // set column generator
        for (Map.Entry<Object, ColumnGenerator> entry : columnGenerators.entrySet()) {
            tableItem.addGeneratedColumn(entry.getKey(), entry.getValue());
        }

        if (StringUtils.isNotBlank((String) sortColumnId)) {
            tableItem.setColumnIcon(sortColumnId, isAscending ? FontAwesome.CARET_DOWN : FontAwesome.CARET_UP);
        }

        tableItem.addHeaderClickListener(new Table.HeaderClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void headerClick(final Table.HeaderClickEvent event) {
                String propertyId = (String) event.getPropertyId();

                if (propertyId.equals("selected")) {
                    return;
                }

                if (searchRequest != null) {
                    S searchCriteria = searchRequest.getSearchCriteria();
                    if (sortColumnId == null) {
                        sortColumnId = propertyId;
                        searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField(propertyId, SearchCriteria.DESC)));
                        isAscending = false;
                    } else if (propertyId.equals(sortColumnId)) {
                        isAscending = !isAscending;
                        String direction = (isAscending) ? SearchCriteria.ASC : SearchCriteria.DESC;
                        searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField(propertyId, direction)));
                    } else {
                        sortColumnId = propertyId;
                        searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField(propertyId, SearchCriteria.DESC)));
                        isAscending = false;
                    }

                    setSearchCriteria(searchCriteria);
                }
            }
        });

        BeanItemContainer<B> container = new BeanItemContainer<>(type, currentListData);
        tableItem.setPageLength(0);
        tableItem.setContainerDataSource(container);
        displayTableColumns();

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
        this.setExpandRatio(tableItem, 1);
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
