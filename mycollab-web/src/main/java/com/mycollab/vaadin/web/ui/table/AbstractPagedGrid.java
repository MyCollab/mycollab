/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui.table;

import com.mycollab.common.GridFieldMeta;
import com.mycollab.common.domain.CustomViewStore;
import com.mycollab.common.domain.NullCustomViewStore;
import com.mycollab.common.json.FieldDefAnalyzer;
import com.mycollab.common.service.CustomViewStoreService;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.PageableHandler;
import com.mycollab.vaadin.event.SelectableItemHandler;
import com.vaadin.data.ValueProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.AbstractRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.*;

import static com.mycollab.vaadin.web.ui.WebThemes.SCROLLABLE_CONTAINER;

/**
 * @param <S>
 * @param <B>
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class AbstractPagedGrid<S extends SearchCriteria, B> extends VerticalLayout implements IPagedGrid<S, B> {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPagedGrid.class);

    private int displayNumItems = BasicSearchRequest.DEFAULT_NUMBER_SEARCH_ITEMS;
    private Collection<B> currentListData;
    protected BasicSearchRequest<S> searchRequest;

    private boolean isAscending = true;
    private Object sortColumnId;

    private int currentPage = 1;
    private int totalPage = 1;
    private int currentViewCount;
    protected int totalCount;

    //    protected Table gridItem;
    private HorizontalLayout controlBarWrapper;

    private Set<SelectableItemHandler<B>> selectableHandlers;
    private Set<PageableHandler> pageableHandlers;

    protected Class<B> type;
    protected Grid<B> gridItem;

    private GridFieldMeta requiredColumn;
    private List<GridFieldMeta> displayColumns;
    private List<GridFieldMeta> defaultSelectedColumns;

    private final Map<ValueProvider, AbstractRenderer> columnGenerators = new HashMap<>();

    public AbstractPagedGrid(Class<B> type, List<GridFieldMeta> displayColumns) {
        this(type, null, displayColumns);
    }

    public AbstractPagedGrid(Class<B> type, GridFieldMeta requiredColumn, List<GridFieldMeta> displayColumns) {
        this(type, null, requiredColumn, displayColumns);
    }

    public AbstractPagedGrid(Class<B> type, String viewId, GridFieldMeta requiredColumn, List<GridFieldMeta> displayColumns) {
        if (viewId != null) {
            CustomViewStoreService customViewStoreService = AppContextUtil.getSpringBean(CustomViewStoreService.class);
            CustomViewStore viewLayoutDef = customViewStoreService.getViewLayoutDef(AppUI.getAccountId(),
                    UserUIContext.getUsername(), viewId);
            if (!(viewLayoutDef instanceof NullCustomViewStore)) {
                try {
                    this.displayColumns = FieldDefAnalyzer.toGridFields(viewLayoutDef.getViewinfo());
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
        addStyleName(SCROLLABLE_CONTAINER);
    }

    public void setDisplayColumns(List<GridFieldMeta> viewFields) {
        this.displayColumns = viewFields;
        setDisplayColumns();
    }

    private void setDisplayColumns() {
        Set<String> visibleColumnsCol = new LinkedHashSet<>();
        Set<String> columnHeadersCol = new LinkedHashSet<>();

//        if (requiredColumn != null) {
//            visibleColumnsCol.add(requiredColumn.getField());
//            columnHeadersCol.add(UserUIContext.getMessage(requiredColumn.getDescKey()));
//            gridItem.getColumn(requiredColumn.getField()).setWidth(requiredColumn.getDefaultWidth());
//        }

        for (int i = 0; i < displayColumns.size(); i++) {
            GridFieldMeta viewField = displayColumns.get(i);
            visibleColumnsCol.add(viewField.getField());
            columnHeadersCol.add(UserUIContext.getMessage(viewField.getDescKey()));

//            if (i == 0) {
//                gridItem.getColumn(viewField.getField()).setExpandRatio(1);
//            } else {
//                gridItem.getColumn(viewField.getField()).setWidth(viewField.getDefaultWidth());
//            }
        }

        String[] visibleColumns = visibleColumnsCol.toArray(new String[visibleColumnsCol.size()]);
        String[] columnHeaders = columnHeadersCol.toArray(new String[columnHeadersCol.size()]);

//        gridItem.setVisibleColumns(visibleColumns);
//        gridItem.setColumnHeaders(columnHeaders);
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
    public void addPageableHandler(final PageableHandler handler) {
        if (pageableHandlers == null) {
            pageableHandlers = new HashSet<>();
        }
        pageableHandlers.add(handler);

    }

    @Override
    public Collection<B> getCurrentDataList() {
        return currentListData;
    }

    public void setCurrentDataList(Collection<B> list) {
        currentListData = list;
        currentViewCount = list.size();
        createGrid();
    }

    @Override
    public void addTableListener(TableClickListener listener) {
        addListener(TableClickEvent.TABLE_CLICK_IDENTIFIER, TableClickEvent.class, listener, TableClickListener.itemClickMethod);
    }

    protected void fireTableEvent(TableClickEvent event) {
        fireEvent(event);
    }

    @Override
    public <V> void addGeneratedColumn(ValueProvider<B, V> id, AbstractRenderer generatedColumn) {
        this.columnGenerators.put(id, generatedColumn);
    }

    @Override
    public int setSearchCriteria(final S searchCriteria) {
        searchRequest = new BasicSearchRequest<>(searchCriteria, currentPage, displayNumItems);
        doSearch();
        return totalCount;
    }

    public void setDisplayNumItems(int displayNumItems) {
        this.displayNumItems = displayNumItems;
    }

    @Override
    public B getBeanByIndex(final Object itemId) {
//        return (B)gridItem.getDataProvider().get(itemId);
        return null;
    }

    private void pageChange(final int currentPage) {
        if (searchRequest != null) {
            this.currentPage = currentPage;
            searchRequest.setCurrentPage(currentPage);
            doSearch();

            if (pageableHandlers != null) {
                pageableHandlers.forEach(handler -> handler.move(currentPage));
            }
        }
    }

    public void fireSelectItemEvent(final B item) {
        if (this.selectableHandlers != null) {
            selectableHandlers.forEach(handler -> handler.onSelect(item));
        }
    }

    private ComponentContainer createPagingControls() {
        controlBarWrapper = new HorizontalLayout();
        controlBarWrapper.setWidth("100%");
        controlBarWrapper.setStyleName("listControl");

        MHorizontalLayout pageManagement = new MHorizontalLayout();

        // defined layout here ---------------------------

        if (currentPage > 1) {
            MButton firstLink = new MButton("1", clickEvent -> pageChange(1)).withStyleName("buttonPaging");
            pageManagement.addComponent(firstLink);
        }
        if (currentPage >= 5) {
            Label ss1 = new Label("...");
            ss1.addStyleName("buttonPaging");
            pageManagement.addComponent(ss1);
        }

        if (currentPage > 3) {
            MButton previous2 = new MButton("" + (currentPage - 2), clickEvent -> pageChange(currentPage - 2))
                    .withStyleName("buttonPaging");
            pageManagement.addComponent(previous2);
        }
        if (currentPage > 2) {
            MButton previous1 = new MButton("" + (currentPage - 1), clickEvent -> pageChange(currentPage - 1))
                    .withStyleName("buttonPaging");
            pageManagement.addComponent(previous1);
        }
        // Here add current ButtonLinkLegacy
        MButton current = new MButton("" + currentPage, clickEvent -> pageChange(currentPage))
                .withStyleName("buttonPaging", "current");

        pageManagement.addComponent(current);
        final int range = totalPage - currentPage;
        if (range >= 1) {
            MButton next1 = new MButton("" + (currentPage + 1), clickEvent -> pageChange(currentPage + 1))
                    .withStyleName("buttonPaging");
            pageManagement.addComponent(next1);
        }
        if (range >= 2) {
            MButton next2 = new MButton("" + (currentPage + 2), clickEvent -> pageChange(currentPage + 2))
                    .withStyleName("buttonPaging");
            pageManagement.addComponent(next2);
        }
        if (range >= 4) {
            final Label ss2 = new Label("...");
            ss2.addStyleName("buttonPaging");
            pageManagement.addComponent(ss2);
        }
        if (range >= 3) {
            MButton last = new MButton("" + totalPage, clickEvent -> pageChange(totalPage)).withStyleName("buttonPaging");
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

        createGrid();
    }

    private void createGrid() {
        gridItem = new Grid<>();
        gridItem.setWidth("100%");

        gridItem.setItems(currentListData);

        // set column generator
        for (Map.Entry<ValueProvider, AbstractRenderer> entry : columnGenerators.entrySet()) {
            gridItem.addColumn(entry.getKey()).setRenderer(entry.getValue());
        }

//        gridItem.addHeaderClickListener(headerClickEvent -> {
//            String propertyId = (String) headerClickEvent.getPropertyId();
//
//            if (propertyId.equals("selected")) {
//                return;
//            }
//
//            if (searchRequest != null) {
//                S searchCriteria = searchRequest.getSearchCriteria();
//                if (sortColumnId == null) {
//                    sortColumnId = propertyId;
//                    searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField(propertyId, SearchCriteria.DESC)));
//                    isAscending = false;
//                } else if (propertyId.equals(sortColumnId)) {
//                    isAscending = !isAscending;
//                    String direction = (isAscending) ? SearchCriteria.ASC : SearchCriteria.DESC;
//                    searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField(propertyId, direction)));
//                } else {
//                    sortColumnId = propertyId;
//                    searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField(propertyId, SearchCriteria.DESC)));
//                    isAscending = false;
//                }
//
//                setSearchCriteria(searchCriteria);
//            }
//        });

        setDisplayColumns();

        if (this.getComponentCount() > 0) {
            final Component component0 = this.getComponent(0);
            if (component0 instanceof Grid) {
                this.replaceComponent(component0, gridItem);
            } else {
                this.addComponent(gridItem, 0);
            }
        } else {
            this.addComponent(gridItem, 0);
        }
        this.setExpandRatio(gridItem, 1);
    }

    public List<GridFieldMeta> getDefaultSelectedColumns() {
        return defaultSelectedColumns;
    }

    public Grid<B> getGrid() {
        return gridItem;
    }

    @Override
    public List<GridFieldMeta> getDisplayColumns() {
        return displayColumns;
    }

//    public Object[] getVisibleColumns() {
//        return gridItem.getVisibleColumns();
//    }
}
