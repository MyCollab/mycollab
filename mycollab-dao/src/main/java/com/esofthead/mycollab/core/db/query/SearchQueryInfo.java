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
package com.esofthead.mycollab.core.db.query;

import java.util.Arrays;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
public class SearchQueryInfo {
    private String queryId;
    private String queryName;
    private List<SearchFieldInfo> searchFieldInfos;

    public SearchQueryInfo(String queryName, SearchFieldInfo... searchFieldInfoArr) {
        this(queryName, Arrays.asList(searchFieldInfoArr));
    }

    public SearchQueryInfo(String queryName, List<SearchFieldInfo> searchFieldInfos) {
        this("", queryName, searchFieldInfos);
    }

    public SearchQueryInfo(String queryId, String queryName, SearchFieldInfo... searchFieldInfoArr) {
        this(queryId, queryName, Arrays.asList(searchFieldInfoArr));
    }

    public SearchQueryInfo(String queryId, String queryName, List<SearchFieldInfo> searchFieldInfos) {
        this.queryId = queryId;
        this.queryName = queryName;
        this.searchFieldInfos = searchFieldInfos;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public List<SearchFieldInfo> getSearchFieldInfos() {
        return searchFieldInfos;
    }

    public void setSearchFieldInfos(List<SearchFieldInfo> searchFieldInfos) {
        this.searchFieldInfos = searchFieldInfos;
    }

    public String getQueryId() {
        return queryId;
    }
}
