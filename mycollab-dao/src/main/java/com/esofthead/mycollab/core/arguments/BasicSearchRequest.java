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
package com.esofthead.mycollab.core.arguments;

/**
 * @param <S>
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BasicSearchRequest<S extends SearchCriteria> extends SearchRequest {
    private static final long serialVersionUID = 1L;

    private S searchCriteria;

    public BasicSearchRequest() {
        this(null, 1, 1);
    }

    public BasicSearchRequest(S searchCriteria) {
        this(searchCriteria, 0, Integer.MAX_VALUE);
    }

    public BasicSearchRequest(S searchCriteria, int currentPage, int numberOfItems) {
        super(currentPage, numberOfItems);
        this.searchCriteria = searchCriteria;
    }

    public S getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(S searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

}
