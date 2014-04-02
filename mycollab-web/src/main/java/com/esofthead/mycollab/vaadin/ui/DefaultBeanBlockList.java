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
