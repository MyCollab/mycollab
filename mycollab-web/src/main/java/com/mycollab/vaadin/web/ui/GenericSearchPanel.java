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

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.event.SearchHandler;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class GenericSearchPanel<S extends SearchCriteria> extends CustomComponent implements HasSearchHandlers<S> {
    private List<SearchHandler<S>> searchHandlers;
    private Component headerRight;

    @Override
    public void addSearchHandler(final SearchHandler<S> handler) {
        if (searchHandlers == null) {
            searchHandlers = new ArrayList<>();
        }
        searchHandlers.add(handler);
    }

    @Override
    public void notifySearchHandler(final S criteria) {
        if (searchHandlers != null) {
            for (SearchHandler<S> handler : searchHandlers) {
                handler.onSearch(criteria);
            }
        }
    }

    @Override
    protected void setCompositionRoot(Component compositionRoot) {
        super.setCompositionRoot(compositionRoot);
        addHeaderRight(headerRight);
    }

    public void addHeaderRight(Component c) {
        if (c != null)
            this.headerRight = c;
        else
            return;

        Component root = getCompositionRoot();
        if (root != null) {
            ((SearchLayout<?>) root).addHeaderRight(this.headerRight);
        }
    }

}
