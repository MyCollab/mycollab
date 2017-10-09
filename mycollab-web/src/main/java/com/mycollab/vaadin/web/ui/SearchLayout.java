package com.mycollab.vaadin.web.ui;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.web.CustomLayoutExt;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
abstract public class SearchLayout<S extends SearchCriteria> extends CustomLayoutExt {
    protected GenericSearchPanel<S> searchPanel;

    public SearchLayout(GenericSearchPanel<S> parent, String layoutName) {
        super(layoutName);
        this.searchPanel = parent;
    }

    public void callSearchAction() {
        final S searchCriteria = this.fillUpSearchCriteria();
        searchPanel.notifySearchHandler(searchCriteria);
    }

    abstract protected S fillUpSearchCriteria();

    abstract protected void addHeaderRight(Component c);

}
