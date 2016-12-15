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
package com.mycollab.mobile.ui;

import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.touchkit.InfiniteScrollLayout;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractPagedBeanList<S extends SearchCriteria, B> extends CssLayout implements IPagedBeanList<S, B> {
    private static final long serialVersionUID = 1504984093640864283L;

    protected int displayNumItems = BasicSearchRequest.DEFAULT_NUMBER_SEARCH_ITEMS();
    protected List<B> currentListData;
    private RowDisplayHandler<B> rowDisplayHandler;

    protected VerticalLayout listContainer;

    protected BasicSearchRequest<S> searchRequest;
    protected int currentPage = 1;
    protected int totalPage = 1;
    protected int currentViewCount;
    protected int totalCount;

    public AbstractPagedBeanList(RowDisplayHandler<B> rowDisplayHandler) {
        super();
        setSizeFull();
        this.rowDisplayHandler = rowDisplayHandler;
        listContainer = new VerticalLayout();
        this.addComponent(listContainer);
        InfiniteScrollLayout scrollLayout = InfiniteScrollLayout.extend(this);
        scrollLayout.addScrollListener(this::loadMore);
    }

    public AbstractPagedBeanList(RowDisplayHandler<B> rowDisplayHandler, int defaultNumberSearchItems) {
        this(rowDisplayHandler);
        displayNumItems = defaultNumberSearchItems;
    }

    @Override
    public Integer search(final S searchCriteria) {
        currentPage = 1;
        searchRequest = new BasicSearchRequest<>(searchCriteria, currentPage, displayNumItems);
        return doSearch();
    }

    @Override
    public void setSearchCriteria(S searchCriteria) {
        currentPage = 1;
        searchRequest = new BasicSearchRequest<>(searchCriteria, currentPage, displayNumItems);
    }

    public BasicSearchRequest<S> getSearchRequest() {
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

    protected Integer doSearch() {
        totalCount = this.queryTotalCount();
        this.totalPage = (totalCount - 1) / searchRequest.getNumberOfItems() + 1;
        if (searchRequest.getCurrentPage() > this.totalPage) {
            searchRequest.setCurrentPage(this.totalPage);
        }

        currentListData = this.queryCurrentData();
        currentViewCount = currentListData.size();

        listContainer.removeAllComponents();
        renderRows();
        return totalCount;
    }

    protected void renderRows() {
        int i = 0;
        for (final B item : currentListData) {
            final Component row = rowDisplayHandler.generateRow(this, item, i);
            row.addStyleName("row");
            listContainer.addComponent(row);
            i++;
        }
    }

    private void loadMore() {
        currentPage += 1;
        searchRequest.setCurrentPage(currentPage);
        List<B> currentData = this.queryCurrentData();
        if (currentListData == null)
            currentListData = new ArrayList<>();
        currentListData.addAll(currentData);
        currentViewCount += currentListData.size();

        int i = currentViewCount + 1;
        for (final B item : currentData) {
            final Component row = rowDisplayHandler.generateRow(this, item, i);
            row.addStyleName("row");
            if (row != null) {
                listContainer.addComponent(row);
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

    public void addComponentAtTop(Component component) {
        component.addStyleName("row");
        listContainer.addComponentAsFirst(component);
    }
}
