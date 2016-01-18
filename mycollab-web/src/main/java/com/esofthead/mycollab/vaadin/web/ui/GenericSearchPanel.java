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
package com.esofthead.mycollab.vaadin.web.ui;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.web.CustomLayoutExt;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
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

    abstract public static class SearchLayout<S extends SearchCriteria> extends CustomLayoutExt {
        protected GenericSearchPanel<S> searchPanel;

        public SearchLayout(GenericSearchPanel<S> parent, String layoutName) {
            super(layoutName);
            this.searchPanel = parent;
        }

        public void callSearchAction() {
            final S searchCriteria = this.fillUpSearchCriteria();
            this.searchPanel.notifySearchHandler(searchCriteria);
        }

        abstract protected S fillUpSearchCriteria();

        abstract protected void addHeaderRight(Component c);

    }

    abstract public static class BasicSearchLayout<S extends SearchCriteria> extends SearchLayout<S> {
        private static final long serialVersionUID = 1L;
        protected ComponentContainer header;
        protected ComponentContainer body;

        public BasicSearchLayout(final GenericSearchPanel<S> parent) {
            super(parent, "basicSearch");
            this.setStyleName("basicSearchLayout");
            this.initLayout();
        }

        protected void initLayout() {
            this.header = this.constructHeader();
            this.body = this.constructBody();
            if (header != null) {
                this.addComponent(this.header, "basicSearchHeader");
            }

            this.addComponent(this.body, "basicSearchBody");
        }

        @Override
        protected void addHeaderRight(Component c) {
            if (this.header == null)
                return;

            this.header.addComponent(c);
        }

        abstract public ComponentContainer constructHeader();

        abstract public ComponentContainer constructBody();
    }
}
