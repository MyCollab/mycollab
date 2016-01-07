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

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.vaadin.ui.Hr;
import com.esofthead.vaadin.mobilecomponent.InfiniteScrollLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractPagedBeanList<S extends SearchCriteria, B> extends CssLayout implements IPagedBeanList<S, B> {
    private static final long serialVersionUID = 1504984093640864283L;

    protected int displayNumItems = SearchRequest.DEFAULT_NUMBER_SEARCH_ITEMS;
    protected List<B> currentListData;
    private RowDisplayHandler<B> rowDisplayHandler;

    protected CssLayout listContainer;

    protected SearchRequest<S> searchRequest;
    protected int currentPage = 1;
    protected int totalPage = 1;
    protected int currentViewCount;
    protected int totalCount;

    public AbstractPagedBeanList(RowDisplayHandler<B> rowDisplayHandler) {
        super();
        setSizeFull();
        InfiniteScrollLayout scrollLayout = InfiniteScrollLayout.extend(this);
        scrollLayout.addScrollListener(new InfiniteScrollLayout.ScrollReachBottomListener() {

            @Override
            public void onReachBottom() {
                loadMore();
            }
        });

        this.rowDisplayHandler = rowDisplayHandler;

        listContainer = new CssLayout();
        this.addComponent(listContainer);
    }

    public AbstractPagedBeanList(RowDisplayHandler<B> rowDisplayHandler, int defaultNumberSearchItems) {
        this(rowDisplayHandler);
        this.displayNumItems = defaultNumberSearchItems;
    }

    public int currentViewCount() {
        return this.currentViewCount;
    }

    public int totalItemsCount() {
        return this.totalCount;
    }

    public void setDisplayNumItems(int value) {
        this.displayNumItems = value;
    }

    @Override
    public List<B> getCurrentDataList() {
        return currentListData;
    }

    @Override
    public void setSearchCriteria(final S searchCriteria) {
        this.currentPage = 1;
        this.searchRequest = new SearchRequest<>(searchCriteria, this.currentPage, this.displayNumItems);
        this.doSearch();
    }

    public SearchRequest<S> getSearchRequest() {
        return this.searchRequest;
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
        this.totalPage = (this.totalCount - 1) / this.searchRequest.getNumberOfItems() + 1;
        if (this.searchRequest.getCurrentPage() > this.totalPage) {
            this.searchRequest.setCurrentPage(this.totalPage);
        }

        this.currentListData = this.queryCurrentData();
        this.currentViewCount = this.currentListData.size();

        listContainer.removeAllComponents();

        renderRows();
    }

    protected void renderRows() {
        int i = 0;
        for (final B item : currentListData) {
            final Component row = rowDisplayHandler.generateRow(item, i);
            listContainer.addComponent(row);
            listContainer.addComponent(new Hr());
            i++;
        }
    }

    protected void loadMore() {
        this.currentPage += 1;
        this.searchRequest.setCurrentPage(this.currentPage);
        List<B> currentData = this.queryCurrentData();
        if (this.currentListData == null)
            this.currentListData = new ArrayList<>();
        this.currentListData.addAll(currentData);
        this.currentViewCount += this.currentListData.size();

        int i = currentViewCount + 1;
        for (final B item : currentData) {
            final Component row = rowDisplayHandler.generateRow(item, i);
            listContainer.addComponent(row);
            listContainer.addComponent(new Hr());
            i++;
        }
    }

    public void setRowDisplayHandler(RowDisplayHandler<B> rowDisplayHandler) {
        this.rowDisplayHandler = rowDisplayHandler;
    }

    public RowDisplayHandler<B> getRowDisplayHandler() {
        return this.rowDisplayHandler;
    }

    public CssLayout getListContainer() {
        return this.listContainer;
    }

    public interface RowDisplayHandler<B> {
        Component generateRow(B obj, int rowIndex);
    }
}
