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
        this.rowDisplayHandler = rowDisplayHandler;
        InfiniteScrollLayout scrollLayout = InfiniteScrollLayout.extend(this);
        scrollLayout.addScrollListener(new InfiniteScrollLayout.ScrollReachBottomListener() {
            @Override
            public void onReachBottom() {
                loadMore();
            }
        });
        listContainer = new CssLayout();
        this.addComponent(listContainer);
    }

    public AbstractPagedBeanList(RowDisplayHandler<B> rowDisplayHandler, int defaultNumberSearchItems) {
        this(rowDisplayHandler);
        displayNumItems = defaultNumberSearchItems;
    }

    @Override
    public void search(final S searchCriteria) {
        currentPage = 1;
        searchRequest = new SearchRequest<>(searchCriteria, currentPage, displayNumItems);
        doSearch();
    }

    @Override
    public void setSearchCriteria(S searchCriteria) {
        currentPage = 1;
        searchRequest = new SearchRequest<>(searchCriteria, currentPage, displayNumItems);
    }

    public SearchRequest<S> getSearchRequest() {
        return searchRequest;
    }

    @Override
    public void refresh() {
        currentPage = 1;
        searchRequest.setCurrentPage(currentPage);
        doSearch();
    }

    abstract protected int queryTotalCount();

    abstract protected List<B> queryCurrentData();

    protected void doSearch() {
        totalCount = this.queryTotalCount();
        this.totalPage = (totalCount - 1) / searchRequest.getNumberOfItems() + 1;
        if (searchRequest.getCurrentPage() > this.totalPage) {
            searchRequest.setCurrentPage(this.totalPage);
        }

        currentListData = this.queryCurrentData();
        currentViewCount = currentListData.size();

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
        currentPage += 1;
        searchRequest.setCurrentPage(currentPage);
        List<B> currentData = this.queryCurrentData();
        if (currentListData == null)
            currentListData = new ArrayList<>();
        currentListData.addAll(currentData);
        currentViewCount += currentListData.size();

        int i = currentViewCount + 1;
        for (final B item : currentData) {
            final Component row = rowDisplayHandler.generateRow(item, i);
            if (row != null) {
                listContainer.addComponent(row);
                listContainer.addComponent(new Hr());
            }

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
