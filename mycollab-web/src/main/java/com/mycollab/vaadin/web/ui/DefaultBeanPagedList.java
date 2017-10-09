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
                return (List<T>) searchService.findPageableListByCriteria((BasicSearchRequest<S>) searchRequest);
            }
        };
    }
}
