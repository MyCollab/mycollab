package com.mycollab.vaadin.web.ui.table;

import com.mycollab.common.TableViewField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.persistence.service.ISearchableService;

import java.util.List;

/**
 * @param <SearchService>
 * @param <S>
 * @param <T>
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public class DefaultPagedBeanTable<SearchService extends ISearchableService<S>, S extends SearchCriteria, T>
        extends AbstractPagedBeanTable<S, T> {
    private static final long serialVersionUID = 1L;

    private final SearchService searchService;

    public DefaultPagedBeanTable(SearchService searchService, Class<T> type, List<TableViewField> displayColumns) {
        super(type, displayColumns);
        this.searchService = searchService;
    }

    public DefaultPagedBeanTable(SearchService searchService, Class<T> type, TableViewField requiredColumn,
                                 List<TableViewField> displayColumns) {
        this(searchService, type, null, requiredColumn, displayColumns);
    }

    public DefaultPagedBeanTable(SearchService searchService, Class<T> type, String viewId,
                                 TableViewField requiredColumn, List<TableViewField> displayColumns) {
        super(type, viewId, requiredColumn, displayColumns);
        this.searchService = searchService;
    }

    @Override
    protected int queryTotalCount() {
        return searchService.getTotalCount(searchRequest.getSearchCriteria());
    }

    @Override
    protected List<T> queryCurrentData() {
        return (List<T>) searchService.findPageableListByCriteria(searchRequest);
    }

}
