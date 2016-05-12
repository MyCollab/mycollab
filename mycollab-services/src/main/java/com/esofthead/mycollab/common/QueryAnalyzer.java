/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.common;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.db.query.SearchFieldInfo;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class QueryAnalyzer {

    public static String toQueryParams(List<SearchFieldInfo> searchFieldInfos) {
        StringBuilder result = new StringBuilder();
        for (SearchFieldInfo searchFieldInfo : searchFieldInfos) {

        }
        return result.toString();
    }

    public static SearchCriteria fromQueryParams(String query, Class<? extends SearchCriteria> cls) {
        return null;
    }
}
