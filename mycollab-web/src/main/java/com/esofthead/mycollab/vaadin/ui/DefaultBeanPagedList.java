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

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DefaultBeanPagedList<SearchService extends ISearchableService<S>, S extends SearchCriteria, T>
		extends AbstractBeanPagedList<S, T> {
	private static final long serialVersionUID = 1L;

	private final SearchService searchService;

	public DefaultBeanPagedList(SearchService searchService,
			RowDisplayHandler<T> rowDisplayHandler) {
		this(searchService, rowDisplayHandler, 20);
	}

	public DefaultBeanPagedList(SearchService searchService,
			RowDisplayHandler<T> rowDisplayHandler, int defaultNumberSearchItems) {
		super(rowDisplayHandler, defaultNumberSearchItems);
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
