package com.esofthead.mycollab.vaadin.ui;

import java.util.List;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;

public abstract class DefaultBeanBlockList<SearchService extends ISearchableService<S>, S extends SearchCriteria, T> extends AbstractBeanBlockList<S, T> {
	private static final long serialVersionUID = 1L;
	private final SearchService searchService;

	public DefaultBeanBlockList(SearchService searchService,
			BlockDisplayHandler<T> blockDisplayHandler) {
		this(searchService, blockDisplayHandler, 20);
	}

	public DefaultBeanBlockList(SearchService searchService,
			BlockDisplayHandler<T> blockDisplayHandler, int defaultNumberSearchItems) {
		super(blockDisplayHandler, defaultNumberSearchItems);
		this.searchService = searchService;
	}

	@Override
	protected int queryTotalCount() {
		return searchService.getTotalCount(searchRequest.getSearchCriteria());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<T> queryCurrentData() {
		return searchService.findPagableListByCriteria(searchRequest);
	}
}
