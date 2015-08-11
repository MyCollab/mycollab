/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.core.persistence.service;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @param <S>
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class DefaultSearchService<S extends SearchCriteria> implements ISearchableService<S> {

    public abstract ISearchableDAO<S> getSearchMapper();

    @Override
    public int getTotalCount(S criteria) {
        return getSearchMapper().getTotalCount(criteria);
    }

    @Override
    public List findPagableListByCriteria(SearchRequest<S> searchRequest) {
        return getSearchMapper().findPagableListByCriteria(searchRequest.getSearchCriteria(),
                new RowBounds((searchRequest.getCurrentPage() - 1) * searchRequest.getNumberOfItems(),
                        searchRequest.getNumberOfItems()));
    }

    @Override
    public List findAbsoluteListByCriteria(S searchCriteria, Integer firstIndex, Integer numberOftems) {
        return getSearchMapper().findPagableListByCriteria(searchCriteria,
                new RowBounds(firstIndex, numberOftems));
    }

    @Override
    public void removeByCriteria(S criteria, Integer accountId) {
        getSearchMapper().removeByCriteria(criteria);

    }

    @Override
    public Integer getNextItemKey(S criteria) {
        return getSearchMapper().getNextItemKey(criteria);
    }

    @Override
    public Integer getPreviousItemKey(S criteria) {
        return getSearchMapper().getPreviousItemKey(criteria);
    }
}
