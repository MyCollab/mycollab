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
package com.mycollab.vaadin.web.ui;

import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.json.QueryAnalyzer;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.query.SearchFieldInfo;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.ApplicationEventListener;
import com.mycollab.shell.event.ShellEvent;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.Page;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.3.2
 */
public class QueryParamHandler {
    public static <S extends SearchCriteria> ApplicationEventListener<ShellEvent.AddQueryParam> queryParamHandler() {
        return new ApplicationEventListener<ShellEvent.AddQueryParam>() {
            @Subscribe
            @Override
            public void handle(ShellEvent.AddQueryParam event) {
                List<SearchFieldInfo<S>> searchFieldInfos = (List<SearchFieldInfo<S>>) event.getData();
                String query = QueryAnalyzer.toQueryParams(searchFieldInfos);
                String fragment = Page.getCurrent().getUriFragment();
                int index = fragment.indexOf("?");
                if (index > 0) {
                    fragment = fragment.substring(0, index);
                }

                if (StringUtils.isNotBlank(query)) {
                    fragment += "?" + UrlEncodeDecoder.encode(query);
                    Page.getCurrent().setUriFragment(fragment, false);
                }
            }
        };
    }
}
