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
package com.esofthead.mycollab.vaadin.ui.table;

import java.util.List;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;

/**
 * 
 * @author MyCollab Ltd.
 *
 * @param <SearchService>
 * @param <S>
 * @param <T>
 */
public class DefaultPagedBeanTable<SearchService extends ISearchableService<S>, S extends SearchCriteria, T>
		extends AbstractPagedBeanTable<S, T> {

	private static final long serialVersionUID = 1L;

	private final SearchService searchService;

	public DefaultPagedBeanTable(final SearchService searchService,
			final Class<T> type, List<TableViewField> displayColumns) {
		super(type, displayColumns);
		this.searchService = searchService;
	}

	public DefaultPagedBeanTable(final SearchService searchService,
			final Class<T> type, TableViewField requiredColumn,
			List<TableViewField> displayColumns) {
		this(searchService, type, null, requiredColumn, displayColumns);
	}

	public DefaultPagedBeanTable(final SearchService searchService,
			final Class<T> type, String viewId, TableViewField requiredColumn,
			List<TableViewField> displayColumns) {
		super(type, viewId, requiredColumn, displayColumns);
		this.searchService = searchService;
	}

	@Override
	protected int queryTotalCount() {
		return searchService.getTotalCount(searchRequest.getSearchCriteria());
	}

	@Override
	protected List<T> queryCurrentData() {
		return searchService.findPagableListByCriteria(searchRequest);
	}

}
