/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
