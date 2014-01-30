package com.esofthead.mycollab.mobile.ui;

import java.util.List;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 * @param <SearchService>
 * @param <S>
 * @param <B>
 */
public class DefaultPagedBeanList<SearchService extends ISearchableService<S>, S extends SearchCriteria, B>
		extends AbstractPagedBeanList<S, B> {
	private static final long serialVersionUID = 1L;

	private final SearchService searchService;

	public DefaultPagedBeanList(final SearchService searchService,
			final Class<B> type, String displayColumnId) {
		super(type, displayColumnId);
		this.searchService = searchService;
	}

	@Override
	protected int queryTotalCount() {
		return searchService.getTotalCount(searchRequest.getSearchCriteria());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<B> queryCurrentData() {
		return searchService.findPagableListByCriteria(searchRequest);
	}
}
