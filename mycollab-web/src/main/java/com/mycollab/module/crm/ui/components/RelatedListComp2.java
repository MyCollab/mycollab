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
package com.mycollab.module.crm.ui.components;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.vaadin.ui.IRelatedListHandlers;
import com.mycollab.vaadin.ui.RelatedListHandler;
import com.mycollab.vaadin.web.ui.AbstractBeanBlockList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @param <SearchService>
 * @param <S>
 * @param <T>
 * @author MyCollab Ltd.
 * @since 4.0
 */
public abstract class RelatedListComp2<SearchService extends ISearchableService<S>, S extends SearchCriteria, T>
        extends AbstractBeanBlockList<S, T> implements IRelatedListHandlers<T> {
    private static final long serialVersionUID = -5854451222908978059L;
    private final SearchService searchService;

    protected Set<RelatedListHandler<T>> handlers;

    public RelatedListComp2(SearchService searchService, int defaultNumberSearchItems) {
        super(defaultNumberSearchItems);
        this.searchService = searchService;
    }

    public RelatedListComp2(SearchService searchService, BlockDisplayHandler<T> blockDisplayHandler, int defaultNumberSearchItems) {
        super(blockDisplayHandler, defaultNumberSearchItems);
        this.searchService = searchService;
    }

    @Override
    protected int queryTotalCount() {
        return searchService.getTotalCount(searchRequest.getSearchCriteria());
    }

    @Override
    protected List<T> queryCurrentData() {
        return searchService.findPageableListByCriteria(searchRequest);
    }

    @Override
    public void addRelatedListHandler(final RelatedListHandler<T> handler) {
        if (handlers == null) {
            handlers = new HashSet<>();
        }

        handlers.add(handler);
    }

    protected void fireNewRelatedItem(String itemId) {
        if (handlers != null) {
            handlers.forEach(handler -> handler.createNewRelatedItem(itemId));
        }
    }

    public void fireSelectedRelatedItems(Set selectedItems) {
        if (handlers != null) {
            handlers.forEach(handler -> handler.selectAssociateItems(selectedItems));
        }
    }
}