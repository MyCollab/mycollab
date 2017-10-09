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
