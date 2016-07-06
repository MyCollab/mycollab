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
package com.mycollab.db.arguments;

import java.io.Serializable;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
public class SearchRequest implements Serializable {
    public static final int DEFAULT_NUMBER_SEARCH_ITEMS = 25;

    private Integer currentPage = 1;
    private Integer numberOfItems = 1;
    private String requestedUser;

    public SearchRequest(int currentPage, int numberOfItems) {
        this.currentPage = currentPage;
        this.numberOfItems = numberOfItems;
        this.requestedUser = GroupIdProvider.getRequestedUser();
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(Integer numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public String getRequestedUser() {
        return requestedUser;
    }

    public void setRequestedUser(String requestedUser) {
        this.requestedUser = requestedUser;
    }
}
