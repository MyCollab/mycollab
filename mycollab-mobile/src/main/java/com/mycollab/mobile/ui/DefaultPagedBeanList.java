package com.mycollab.mobile.ui;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.persistence.service.ISearchableService;

import java.util.List;

/**
 * @param <SearchService>
 * @param <S>
 * @param <B>
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class DefaultPagedBeanList<SearchService extends ISearchableService<S>, S extends SearchCriteria, B> extends AbstractPagedBeanList<S, B> {
    private static final long serialVersionUID = 1L;

    private final SearchService searchService;

    public DefaultPagedBeanList(final SearchService searchService, RowDisplayHandler<B> rowDisplayHandler) {
        super(rowDisplayHandler);
        this.searchService = searchService;
    }

    @Override
    protected int queryTotalCount() {
        return searchService.getTotalCount(searchRequest.getSearchCriteria());
    }

    @Override
    protected List<B> queryCurrentData() {
        return (List<B>) searchService.findPageableListByCriteria(searchRequest);
    }
}
