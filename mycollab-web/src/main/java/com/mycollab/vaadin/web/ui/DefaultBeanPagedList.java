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
package com.mycollab.vaadin.web.ui;

import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.persistence.service.ISearchableService;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DefaultBeanPagedList<SearchService extends ISearchableService<S>, S extends SearchCriteria, T> extends AbstractBeanPagedList<T> {
    private static final long serialVersionUID = 1L;

    private final SearchService searchService;

    public DefaultBeanPagedList(SearchService searchService, RowDisplayHandler<T> rowDisplayHandler) {
        this(searchService, rowDisplayHandler, 20);
    }

    public DefaultBeanPagedList(SearchService searchService, RowDisplayHandler<T> rowDisplayHandler, int defaultNumberSearchItems) {
        super(rowDisplayHandler, defaultNumberSearchItems);
        this.searchService = searchService;
    }

    public int setSearchCriteria(final S searchCriteria) {
        listContainer.removeAllComponents();
        searchRequest = new BasicSearchRequest<>(searchCriteria, currentPage, defaultNumberSearchItems);
        doSearch();
        return totalCount;
    }

    @Override
    protected QueryHandler<T> buildQueryHandler() {
        return new QueryHandler<T>() {
            @Override
            public int queryTotalCount() {
                return searchService.getTotalCount(((BasicSearchRequest<S>) searchRequest).getSearchCriteria());
            }

            @Override
            public List<T> queryCurrentData() {
                return searchService.findPageableListByCriteria((BasicSearchRequest<S>) searchRequest);
            }
        };
    }
}
